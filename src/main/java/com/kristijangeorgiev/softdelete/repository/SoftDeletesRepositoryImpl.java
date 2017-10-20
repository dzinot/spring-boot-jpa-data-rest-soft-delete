package com.kristijangeorgiev.softdelete.repository;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.CaseFormat;

/**
 * 
 * @author Kristijan Georgiev
 *
 * @param <T>
 *            the class of the entity
 * @param <ID>
 *            the ID class of the entity
 * 
 *            Custom implementation for soft deleting
 */
public class SoftDeletesRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements SoftDeletesRepository<T, ID> {

	private final JpaEntityInformation<T, ?> entityInformation;
	private final EntityManager em;
	private final Class<T> domainClass;
	private static final String DELETED_FIELD = "deletedOn";

	public SoftDeletesRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
		this.domainClass = domainClass;
		this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
	}

	@Override
	public Iterable<T> findAllActive() {
		return super.findAll(notDeleted());
	}

	@Override
	public Iterable<T> findAllActive(Sort sort) {
		return super.findAll(notDeleted(), sort);
	}

	@Override
	public Page<T> findAllActive(Pageable pageable) {
		return super.findAll(notDeleted(), pageable);
	}

	@Override
	public Iterable<T> findAllActive(Iterable<ID> ids) {
		if (ids == null || !ids.iterator().hasNext())
			return Collections.emptyList();

		if (entityInformation.hasCompositeId()) {
			List<T> results = new ArrayList<T>();

			for (ID id : ids)
				results.add(findOneActive(id));

			return results;
		}

		ByIdsSpecification<T> specification = new ByIdsSpecification<T>(entityInformation);
		TypedQuery<T> query = getQuery(Specifications.where(specification).and(notDeleted()), (Sort) null);

		return query.setParameter(specification.parameter, ids).getResultList();
	}

	@Override
	public T findOneActive(ID id) {
		return super.findOne(
				Specifications.where(new ByIdSpecification<T, ID>(entityInformation, id)).and(notDeleted()));
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public <S extends T> S save(S entity) {
		Set<ConstraintViolation<S>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator()
				.validate(entity);

		if (constraintViolations.size() > 0)
			throw new ConstraintViolationException(constraintViolations.toString(), constraintViolations);

		Class<?> entityClass = entity.getClass();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<?> root = criteriaQuery.from(entityClass);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (entityInformation.hasCompositeId()) {
			for (String s : entityInformation.getIdAttributeNames())
				predicates.add(criteriaBuilder.equal(root.<ID>get(s),
						entityInformation.getCompositeIdAttributeValue(entityInformation.getId(entity), s)));

			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DELETED_FIELD), LocalDateTime.now()));

			criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
			TypedQuery<Object> typedQuery = em.createQuery(criteriaQuery);
			List<Object> resultSet = typedQuery.getResultList();

			if (resultSet.size() > 0) {
				S result = (S) resultSet.get(0);
				BeanUtils.copyProperties(entity, result, getNullPropertyNames(entity));
				return (S) super.save(result);
			}
		}

		if (entity.getClass().isAnnotationPresent(Table.class)) {
			Annotation a = entity.getClass().getAnnotation(Table.class);

			try {
				UniqueConstraint[] uniqueConstraints = (UniqueConstraint[]) a.annotationType()
						.getMethod("uniqueConstraints").invoke(a);

				if (uniqueConstraints != null) {
					for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
						Map<String, Object> data = new HashMap<>();

						for (String name : uniqueConstraint.columnNames()) {
							if (name.endsWith("_id"))
								name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
										name.substring(0, name.length() - 3));

							PropertyDescriptor pd = new PropertyDescriptor(name, entityClass);
							Object value = pd.getReadMethod().invoke(entity);

							if (value == null) {
								data.clear();
								break;
							}

							data.put(name, value);
						}

						if (!data.isEmpty())
							for (Map.Entry<String, Object> entry : data.entrySet())
								predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
					}

					if (predicates.isEmpty())
						return super.save(entity);

					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DELETED_FIELD), LocalDateTime.now()));

					criteriaQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
					TypedQuery<Object> typedQuery = em.createQuery(criteriaQuery);
					List<Object> resultSet = typedQuery.getResultList();

					if (resultSet.size() > 0) {
						S result = (S) resultSet.get(0);

						BeanUtils.copyProperties(entity,
								result, Stream
										.concat(Arrays.stream(getNullPropertyNames(entity)),
												Arrays.stream(
														new String[] { entityInformation.getIdAttribute().getName() }))
										.toArray(String[]::new));

						return (S) super.save(result);
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | IntrospectionException e) {
				e.printStackTrace();
			}
		}
		return super.save(entity);
	}

	@Override
	@Transactional
	public <S extends T> S saveAndFlush(S entity) {

		S result = this.save(entity);
		flush();

		return result;
	}

	@Override
	@Transactional
	public <S extends T> List<S> save(Iterable<S> entities) {

		List<S> result = new ArrayList<S>();

		if (entities == null) {
			return result;
		}

		for (S entity : entities) {
			result.add(this.save(entity));
		}

		return result;
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> propertyNames = new HashSet<String>();
		for (PropertyDescriptor pd : pds)
			if (!pd.getName().equals(DELETED_FIELD) && src.getPropertyValue(pd.getName()) == null)
				propertyNames.add(pd.getName());

		return propertyNames.toArray(new String[propertyNames.size()]);
	}

	@Override
	@Transactional
	public void softDelete(ID id) {
		Assert.notNull(id, "The given id must not be null!");
		softDelete(id, LocalDateTime.now());
	}

	@Override
	@Transactional
	public void softDelete(T entity) {
		Assert.notNull(entity, "The entity must not be null!");
		softDelete(entity, LocalDateTime.now());
	}

	@Override
	@Transactional
	public void softDelete(Iterable<? extends T> entities) {
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		for (T entity : entities)
			softDelete(entity);
	}

	@Override
	@Transactional
	public void softDeleteAll() {
		for (T entity : findAllActive())
			softDelete(entity);
	}

	@Override
	@Transactional
	public void scheduleSoftDelete(ID id, LocalDateTime localDateTime) {
		softDelete(id, localDateTime);
	}

	@Override
	@Transactional
	public void scheduleSoftDelete(T entity, LocalDateTime localDateTime) {
		softDelete(entity, localDateTime);
	}

	private void softDelete(ID id, LocalDateTime localDateTime) {
		Assert.notNull(id, "The given id must not be null!");

		T entity = findOneActive(id);

		if (entity == null)
			throw new EmptyResultDataAccessException(
					String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);

		softDelete(entity, localDateTime);
	}

	private void softDelete(T entity, LocalDateTime localDateTime) {
		Assert.notNull(entity, "The entity must not be null!");

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaUpdate<T> update = cb.createCriteriaUpdate((Class<T>) domainClass);

		Root<T> root = update.from((Class<T>) domainClass);

		update.set(DELETED_FIELD, localDateTime);

		final List<Predicate> predicates = new ArrayList<Predicate>();

		if (entityInformation.hasCompositeId()) {
			for (String s : entityInformation.getIdAttributeNames())
				predicates.add(cb.equal(root.<ID>get(s),
						entityInformation.getCompositeIdAttributeValue(entityInformation.getId(entity), s)));
			update.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		} else
			update.where(cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()),
					entityInformation.getId(entity)));

		em.createQuery(update).executeUpdate();
	}

	public long countActive() {
		return super.count(notDeleted());
	}

	@Override
	public boolean existsActive(ID id) {
		Assert.notNull(id, "The entity must not be null!");
		return findOneActive(id) != null ? true : false;
	}

	private static final class ByIdSpecification<T, ID extends Serializable> implements Specification<T> {

		private final JpaEntityInformation<T, ?> entityInformation;
		private final ID id;

		public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
			this.entityInformation = entityInformation;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			final List<Predicate> predicates = new ArrayList<Predicate>();
			if (entityInformation.hasCompositeId()) {
				for (String s : entityInformation.getIdAttributeNames())
					predicates.add(cb.equal(root.<ID>get(s), entityInformation.getCompositeIdAttributeValue(id, s)));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			return cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), id);
		}
	}

	@SuppressWarnings("rawtypes")
	private static final class ByIdsSpecification<T> implements Specification<T> {

		private final JpaEntityInformation<T, ?> entityInformation;

		ParameterExpression<Iterable> parameter;

		public ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
			this.entityInformation = entityInformation;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Iterable.class);
			return path.in(parameter);
		}
	}

	private static final class DeletedIsNull<T> implements Specification<T> {
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.isNull(root.<LocalDateTime>get(DELETED_FIELD));
		}
	}

	private static final class DeletedTimeGreatherThanNow<T> implements Specification<T> {
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.greaterThan(root.<LocalDateTime>get(DELETED_FIELD), LocalDateTime.now());
		}
	}

	private static final <T> Specification<T> notDeleted() {
		return Specifications.where(new DeletedIsNull<T>()).or(new DeletedTimeGreatherThanNow<T>());
	}
}
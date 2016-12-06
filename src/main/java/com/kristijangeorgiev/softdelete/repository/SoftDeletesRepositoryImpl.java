package com.kristijangeorgiev.softdelete.repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

/**
 * 
 * @author Kristijan Georgiev
 *
 * @param <T> the class of the entity
 * @param <ID> the ID class of the entity
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

		update.where(
				cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), entityInformation.getId(entity)));

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

	private static final class ByIdSpecification<T, ID> implements Specification<T> {

		private final JpaEntityInformation<T, ?> entityInformation;
		private final ID id;

		public ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
			this.entityInformation = entityInformation;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

	/*
	 * Specification to check if the DELETED_FIELD is null
	 */
	private static final class DeletedIsNull<T> implements Specification<T> {
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.isNull(root.<LocalDateTime>get(DELETED_FIELD));
		}
	}

	/*
	 * Specification to check if the DELETED_FIELD is greather than the current
	 * LocalDateTime
	 */
	private static final class DeletedTimeGreatherThanNow<T> implements Specification<T> {
		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			return cb.greaterThan(root.<LocalDateTime>get(DELETED_FIELD), LocalDateTime.now());
		}
	}

	/*
	 * Combined Specification from DeletedIsNull and DeletedTimeGreatherThanNow
	 * to check if the entity is soft deleted or not
	 */
	private static final <T> Specification<T> notDeleted() {
		return Specifications.where(new DeletedIsNull<T>()).or(new DeletedTimeGreatherThanNow<T>());
	}
}

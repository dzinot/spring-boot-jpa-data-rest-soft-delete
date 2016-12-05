package com.kristijangeorgiev.softdelete.util;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.kristijangeorgiev.softdelete.repository.SoftDeletesRepositoryImpl;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Returns the custom repository implementation for the soft deletes
 */

public class CustomJpaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
		extends JpaRepositoryFactoryBean<T, S, ID> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CustomJpaRepositoryFactory<T, ID>(entityManager);
	}

	private static class CustomJpaRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {

		private final EntityManager entityManager;

		public CustomJpaRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryInformation information) {
			return new SoftDeletesRepositoryImpl<T, ID>((Class<T>) information.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return SoftDeletesRepositoryImpl.class;
		}
	}
}

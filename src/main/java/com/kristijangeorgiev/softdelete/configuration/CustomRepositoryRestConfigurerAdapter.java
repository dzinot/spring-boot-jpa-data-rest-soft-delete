package com.kristijangeorgiev.softdelete.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.kristijangeorgiev.softdelete.model.entity.PermissionRole;
import com.kristijangeorgiev.softdelete.model.entity.RoleUser;
import com.kristijangeorgiev.softdelete.model.entity.pk.PermissionRolePK;
import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;
import com.kristijangeorgiev.softdelete.repository.PermissionRoleRepository;
import com.kristijangeorgiev.softdelete.repository.RoleUserRepository;

/**
 * 
 * <h2>CustomRepositoryRestConfigurerAdapter</h2>
 * 
 * @author Kristijan Georgiev
 *
 */

@Configuration
public class CustomRepositoryRestConfigurerAdapter extends RepositoryRestConfigurerAdapter {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		// PermissionRole Entity
		config.withEntityLookup().forRepository(PermissionRoleRepository.class, (PermissionRole entity) -> {
			return new PermissionRolePK(entity.getPermissionId(), entity.getRoleId());
		}, PermissionRoleRepository::findOne);

		// RoleUser Entity
		config.withEntityLookup().forRepository(RoleUserRepository.class, (RoleUser entity) -> {
			return new RoleUserPK(entity.getRoleId(), entity.getUserId());
		}, RoleUserRepository::findOne);

		List<Class<?>> entityClasses = getAllManagedEntityTypes(entityManagerFactory);

		// Expose id's for all entity classes
		for (Class<?> entityClass : entityClasses)
			config.exposeIdsFor(entityClass);

		// Return newly created entities in the response
		config.setReturnBodyOnCreate(true);

		// Return updated entities in the response
		config.setReturnBodyOnUpdate(true);
	}

	// Find all classes that are annotated with @Entity
	private List<Class<?>> getAllManagedEntityTypes(EntityManagerFactory entityManagerFactory) {
		List<Class<?>> entityClasses = new ArrayList<>();
		Metamodel metamodel = entityManagerFactory.getMetamodel();

		for (ManagedType<?> managedType : metamodel.getManagedTypes())
			if (managedType.getJavaType().isAnnotationPresent(Entity.class))
				entityClasses.add(managedType.getJavaType());

		return entityClasses;
	}

}

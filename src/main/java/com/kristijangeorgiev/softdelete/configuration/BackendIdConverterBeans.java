package com.kristijangeorgiev.softdelete.configuration;

import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;

import com.kristijangeorgiev.softdelete.model.entity.PermissionRole;
import com.kristijangeorgiev.softdelete.model.entity.RoleUser;
import com.kristijangeorgiev.softdelete.model.entity.pk.PermissionRolePK;
import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;

/**
 * 
 * <h2>BackendIdConverterBeans</h2>
 * 
 * @author Kristijan Georgiev
 *
 */

@Configuration
public class BackendIdConverterBeans {

	// The delimiter to be used in the URL
	private static final String DELIMITER = "_";

	@Bean
	public BackendIdConverter roleUserIdConverter() {
		return new BackendIdConverter() {

			@Override
			public boolean supports(Class<?> delimiter) {
				return RoleUser.class.equals(delimiter);
			}

			@Override
			public String toRequestId(Serializable id, Class<?> entityType) {
				RoleUserPK pk = (RoleUserPK) id;
				return parseUrl(pk.getRoleId(), pk.getUserId());
			}

			@Override
			public Serializable fromRequestId(String id, Class<?> entityType) {
				if (id == null)
					return null;

				String[] parts = id.split(DELIMITER);

				return new RoleUserPK(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
			}
		};
	}

	@Bean
	public BackendIdConverter permissionRoleIdConverter() {
		return new BackendIdConverter() {

			@Override
			public boolean supports(Class<?> delimiter) {
				return PermissionRole.class.equals(delimiter);
			}

			@Override
			public String toRequestId(Serializable id, Class<?> entityType) {
				PermissionRolePK pk = (PermissionRolePK) id;
				return parseUrl(pk.getPermissionId(), pk.getRoleId());
			}

			@Override
			public Serializable fromRequestId(String id, Class<?> entityType) {
				if (id == null)
					return null;

				String[] parts = id.split(DELIMITER);

				return new PermissionRolePK(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
			}
		};
	}

	// Parse the Spring Data Rest URL
	private String parseUrl(Serializable id1, Serializable id2) {
		return String.format("%s_%s", id1, id2);
	}
}

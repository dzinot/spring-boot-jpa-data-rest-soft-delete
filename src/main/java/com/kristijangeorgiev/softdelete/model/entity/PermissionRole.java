package com.kristijangeorgiev.softdelete.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kristijangeorgiev.softdelete.model.entity.id.PermissionRoleId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <h2>PermissionRole</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         PermissionRole association entity
 *
 */

@Entity
@NoArgsConstructor
@Setter
@Getter
public class PermissionRole extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PermissionRoleId id;

	@ManyToOne
	@JoinColumn(name = "permission_id", insertable = false, updatable = false)
	private Permission permission;

	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;

}

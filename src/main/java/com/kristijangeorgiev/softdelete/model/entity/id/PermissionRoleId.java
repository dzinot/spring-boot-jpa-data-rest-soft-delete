package com.kristijangeorgiev.softdelete.model.entity.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.kristijangeorgiev.softdelete.model.entity.PermissionRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <h2>PermissionRoleId</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         Embeddable ID class for the {@link PermissionRole} entity
 *
 */

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRoleId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "permission_id")
	private Long permissionId;

	@Column(name = "role_id")
	private Long roleId;

}

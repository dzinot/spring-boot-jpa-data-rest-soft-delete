package com.kristijangeorgiev.softdelete.model.entity.pk;

import java.io.Serializable;

import com.kristijangeorgiev.softdelete.model.entity.PermissionRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <h2>PermissionRolePK</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         PK class for the {@link PermissionRole} entity
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRolePK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long permissionId;

	private Long roleId;

}

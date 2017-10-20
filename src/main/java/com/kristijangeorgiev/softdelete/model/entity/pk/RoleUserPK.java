package com.kristijangeorgiev.softdelete.model.entity.pk;

import java.io.Serializable;

import com.kristijangeorgiev.softdelete.model.entity.RoleUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <h2>RoleUserPK</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         PK class for the {@link RoleUser} entity
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserPK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long roleId;

	private Long userId;

}

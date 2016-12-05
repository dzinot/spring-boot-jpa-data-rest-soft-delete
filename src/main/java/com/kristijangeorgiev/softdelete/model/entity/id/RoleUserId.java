package com.kristijangeorgiev.softdelete.model.entity.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.kristijangeorgiev.softdelete.model.entity.RoleUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <h2>RoleUserId</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         Embeddable ID class for the {@link RoleUser} entity
 *
 */

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RoleUserId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "user_id")
	private Long userId;

}

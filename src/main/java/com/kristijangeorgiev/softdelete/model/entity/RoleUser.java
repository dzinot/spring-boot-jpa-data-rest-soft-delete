package com.kristijangeorgiev.softdelete.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kristijangeorgiev.softdelete.model.entity.id.RoleUserId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <h2>RoleUser</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         RoleUser association entity
 *
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RoleUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RoleUserId id;

	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

}

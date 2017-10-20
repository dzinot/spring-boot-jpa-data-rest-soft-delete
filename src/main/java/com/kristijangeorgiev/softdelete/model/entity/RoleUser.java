package com.kristijangeorgiev.softdelete.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Lazy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * <h2>RoleUser</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         RoleUser association entity
 *
 */

@Data
@Entity
@NoArgsConstructor
@IdClass(RoleUserPK.class)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = { "role", "user" })
public class RoleUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "role_id")
	private Long roleId;

	@Id
	@Column(name = "user_id")
	private Long userId;

	@Lazy
	@ManyToOne
	@Where(clause = NOT_DELETED)
	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Role role;

	@Lazy
	@ManyToOne
	@Where(clause = NOT_DELETED)
	@JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
	private User user;

}

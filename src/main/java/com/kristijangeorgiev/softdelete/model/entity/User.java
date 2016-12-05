package com.kristijangeorgiev.softdelete.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.hibernate.validator.constraints.Email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <h2>User</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         User entity
 *
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User extends BaseIdEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 4, max = 24)
	private String username;

	@NotNull
	private String password;

	@Email
	@NotNull
	private String email;

	/*
	 * Get all roles associated with the User that are not deleted
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "role_user", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id") })
	@WhereJoinTable(clause = NOT_DELETED)
	@Where(clause = NOT_DELETED)
	private List<Role> roles;

}

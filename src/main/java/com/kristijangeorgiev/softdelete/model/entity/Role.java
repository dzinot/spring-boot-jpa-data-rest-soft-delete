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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <h2>Role</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         Role entity
 *
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Role extends BaseIdEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 6, max = 60)
	private String name;

	/*
	 * Get all permissions associated with the Role that are not deleted
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "permission_role", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "permission_id", referencedColumnName = "id") })
	@WhereJoinTable(clause = NOT_DELETED)
	@Where(clause = NOT_DELETED)
	private List<Permission> permissions;

}

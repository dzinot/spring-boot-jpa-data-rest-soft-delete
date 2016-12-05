package com.kristijangeorgiev.softdelete.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * <h2>BaseIdEntity</h2>
 * 
 * @author Kristijan Georgiev
 * 
 *         MappedSuperclass that extends the {@link BaseEntity} class and is
 *         extended by entity classes that have ID field of type Long
 *
 */

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseIdEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;

}

package com.kristijangeorgiev.softdelete.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kristijangeorgiev.softdelete.model.entity.Role;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Plain repository for the Role object that has all the methods from
 *         the {@link SoftDeletesRepository}
 *
 */

@Repository
@Transactional
public interface RoleRepository extends SoftDeletesRepository<Role, Long> {

}

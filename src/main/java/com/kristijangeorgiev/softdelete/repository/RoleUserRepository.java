package com.kristijangeorgiev.softdelete.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kristijangeorgiev.softdelete.model.entity.RoleUser;
import com.kristijangeorgiev.softdelete.model.entity.id.RoleUserId;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Plain repository for the RoleUser object that has all the methods
 *         from the {@link SoftDeletesRepository}
 *
 */

@Repository
@Transactional
public interface RoleUserRepository extends SoftDeletesRepository<RoleUser, RoleUserId> {

}

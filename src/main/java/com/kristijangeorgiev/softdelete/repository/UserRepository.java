package com.kristijangeorgiev.softdelete.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kristijangeorgiev.softdelete.model.entity.User;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Plain repository for the user object that has all the methods from
 *         the {@link SoftDeletesRepository}
 *
 */

@Repository
@Transactional
public interface UserRepository extends SoftDeletesRepository<User, Long> {

}

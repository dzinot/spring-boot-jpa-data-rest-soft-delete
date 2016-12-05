package com.kristijangeorgiev.softdelete.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kristijangeorgiev.softdelete.model.entity.Permission;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Plain repository for the Permission object that has all the methods
 *         from the {@link SoftDeletesRepository}
 *
 */

@Repository
@Transactional
public interface PermissionRepository extends SoftDeletesRepository<Permission, Long> {

}

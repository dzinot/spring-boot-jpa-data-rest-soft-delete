package com.kristijangeorgiev.softdelete.service;

import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.model.entity.pk.PermissionRolePK;

@Service
public interface PermissionRoleService extends BaseService {
	public void softDelete(PermissionRolePK id);
}

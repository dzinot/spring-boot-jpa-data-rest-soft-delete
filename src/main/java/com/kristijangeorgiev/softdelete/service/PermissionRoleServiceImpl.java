package com.kristijangeorgiev.softdelete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.model.entity.pk.PermissionRolePK;
import com.kristijangeorgiev.softdelete.repository.PermissionRoleRepository;

@Service
public class PermissionRoleServiceImpl extends BaseServiceImpl implements PermissionRoleService {

	@Autowired
	private PermissionRoleRepository permissionRoleServiceRepository;

	@Override
	public void softDelete(PermissionRolePK id) {
		permissionRoleServiceRepository.softDelete(id);
	}

}

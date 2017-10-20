package com.kristijangeorgiev.softdelete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.repository.PermissionRepository;

@Service
public class PermissionServiceImpl extends BaseServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public void softDelete(Long id) {
		permissionRepository.softDelete(id);
	}

}

package com.kristijangeorgiev.softdelete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.repository.RoleRepository;

@Service
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void softDelete(Long id) {
		roleRepository.softDelete(id);
	}

}

package com.kristijangeorgiev.softdelete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;
import com.kristijangeorgiev.softdelete.repository.RoleUserRepository;

@Service
public class RoleUserServiceImpl extends BaseServiceImpl implements RoleUserService {

	@Autowired
	private RoleUserRepository roleUserServiceRepository;

	@Override
	public void softDelete(RoleUserPK id) {
		roleUserServiceRepository.softDelete(id);
	}

}

package com.kristijangeorgiev.softdelete.service;

import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;

@Service
public interface RoleUserService extends BaseService {
	public void softDelete(RoleUserPK id);
}

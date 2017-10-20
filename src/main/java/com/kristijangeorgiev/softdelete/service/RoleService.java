package com.kristijangeorgiev.softdelete.service;

import org.springframework.stereotype.Service;

@Service
public interface RoleService extends BaseService {
	public void softDelete(Long id);
}

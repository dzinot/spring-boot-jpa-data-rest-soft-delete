package com.kristijangeorgiev.softdelete.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kristijangeorgiev.softdelete.repository.UserRepository;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void softDelete(Long id) {
		userRepository.softDelete(id);
	}

}

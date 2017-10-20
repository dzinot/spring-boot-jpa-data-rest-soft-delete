package com.kristijangeorgiev.softdelete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kristijangeorgiev.softdelete.model.entity.pk.RoleUserPK;
import com.kristijangeorgiev.softdelete.service.RoleUserService;

@RestController
@RepositoryRestController
@RequestMapping("/roleUsers")
public class RoleUserController {

	@Autowired
	private RoleUserService roleUserService;

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/softDelete/{roleId}_{userId}", method = RequestMethod.DELETE)
	public void softDelete(@PathVariable("roleId") Long roleId, @PathVariable("userId") Long userId) {
		roleUserService.softDelete(new RoleUserPK(roleId, userId));
	}

}

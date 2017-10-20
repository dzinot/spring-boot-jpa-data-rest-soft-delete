package com.kristijangeorgiev.softdelete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kristijangeorgiev.softdelete.model.entity.pk.PermissionRolePK;
import com.kristijangeorgiev.softdelete.service.PermissionRoleService;

@RestController
@RepositoryRestController
@RequestMapping("/permissionRoles")
public class PermissionRoleController {

	@Autowired
	private PermissionRoleService permissionRoleService;

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/softDelete/{permissionId}_{roleId}", method = RequestMethod.DELETE)
	public void softDelete(@PathVariable("permissionId") Long permissionId, @PathVariable("roleId") Long roleId) {
		permissionRoleService.softDelete(new PermissionRolePK(permissionId, roleId));
	}

}

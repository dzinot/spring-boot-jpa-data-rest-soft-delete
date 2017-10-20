package com.kristijangeorgiev.softdelete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kristijangeorgiev.softdelete.service.PermissionService;

@RestController
@RepositoryRestController
@RequestMapping("/permissions")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/softDelete/{id}", method = RequestMethod.DELETE)
	public void softDelete(@PathVariable("id") Long id) {
		permissionService.softDelete(id);
	}

}

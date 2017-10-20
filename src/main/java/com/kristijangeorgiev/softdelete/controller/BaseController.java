package com.kristijangeorgiev.softdelete.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	protected class GenericNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public GenericNotFoundException(String message) {
			super(message);
		}
	}
}

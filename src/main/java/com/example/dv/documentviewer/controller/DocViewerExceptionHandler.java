package com.example.dv.documentviewer.controller;


import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.example.dv.documentviewer.exception.InvalidApiRequestException;

@ControllerAdvice
@RestController
public class DocViewerExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleException(final Exception e) {
		HashMap<String, Object> response = new HashMap<>();
		response.put("reason", e.getMessage());

		if (e instanceof InvalidApiRequestException) {
			return new ResponseEntity<>(response, ((InvalidApiRequestException) e).getErrorCode());
		}

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}


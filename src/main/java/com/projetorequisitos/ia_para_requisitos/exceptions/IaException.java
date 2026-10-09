package com.projetorequisitos.ia_para_requisitos.exceptions;

public class IaException extends RuntimeException {

	public IaException(String message) {
		super(message);
	}

	public IaException(String message, Throwable cause) {
		super(message, cause);
	}

}

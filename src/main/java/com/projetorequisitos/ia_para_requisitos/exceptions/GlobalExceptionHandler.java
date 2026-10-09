package com.projetorequisitos.ia_para_requisitos.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.projetorequisitos.ia_para_requisitos.dtos.ErroResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErroResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErroResponseDto(400, "Requisição inválida", ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(IaException.class)
	public ResponseEntity<ErroResponseDto> erroProcessamentoIa(IaException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(new ErroResponseDto(503, "Erro no serviço da IA.", ex.getMessage(), LocalDateTime.now()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponseDto> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErroResponseDto(500, "Erro interno do servidor", ex.getMessage(), LocalDateTime.now()));
	}

}

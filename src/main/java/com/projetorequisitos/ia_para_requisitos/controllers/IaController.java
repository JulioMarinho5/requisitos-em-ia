package com.projetorequisitos.ia_para_requisitos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetorequisitos.ia_para_requisitos.dtos.RequisitoResponseDto;
import com.projetorequisitos.ia_para_requisitos.dtos.RequisitosRequestDto;
import com.projetorequisitos.ia_para_requisitos.services.IaService;

@RestController
@RequestMapping("/ia")
public class IaController {
	private final IaService iaService;

	public IaController(IaService iaService) {
		this.iaService = iaService;
	}

	@PostMapping("/envio")
	public ResponseEntity<List<RequisitoResponseDto>> enviarRequisitos(@RequestBody RequisitosRequestDto dto) {
		return ResponseEntity.ok(iaService.enviarRequisitos(dto));
	}

}

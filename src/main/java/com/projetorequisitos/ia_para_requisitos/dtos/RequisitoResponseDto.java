package com.projetorequisitos.ia_para_requisitos.dtos;

import java.util.List;

import com.projetorequisitos.ia_para_requisitos.enums.TipoRequisito;

public record RequisitoResponseDto(String requisitoOriginal, TipoRequisito tipo, AvaliacaoDto avaliacaoOriginal,
		List<String> deficiencias, String requisitoAprimorado, AvaliacaoDto avaliacaoAprimorada) {
}

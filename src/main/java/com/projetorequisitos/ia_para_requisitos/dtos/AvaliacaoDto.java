package com.projetorequisitos.ia_para_requisitos.dtos;

public record AvaliacaoDto(int clareza, int naoAmbiguidade, int completude, int consistencia, int testabilidade,
		int mensurabilidade, int qualidadeGeral) {
}
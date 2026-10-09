package com.projetorequisitos.ia_para_requisitos.dtos;

import java.time.LocalDateTime;

public record ErroResponseDto(int valor, String mensagem, String detalhe, LocalDateTime data) {

}

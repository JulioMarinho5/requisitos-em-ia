package com.projetorequisitos.ia_para_requisitos.services;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.projetorequisitos.ia_para_requisitos.dtos.RequisitoResponseDto;
import com.projetorequisitos.ia_para_requisitos.dtos.RequisitosRequestDto;
import com.projetorequisitos.ia_para_requisitos.exceptions.IaException;

@Service
public class IaService {
	private final ChatClient chatClient;
	private static final String INSTRUCAO = """
			Você é um especialista em Engenharia de Requisitos de Software.

			Para cada requisito recebido:

			1. Classifique como FUNCIONAL ou NAO_FUNCIONAL.
			2. Avalie:
			   - clareza: 0 a 5
			   - não ambiguidade: 0 a 5
			   - completude: 0 a 5
			   - consistência: 0 a 5
			   - testabilidade: 0 a 5
			   - mensurabilidade: 0 a 5
			   - qualidade geral: 0 a 10
			3. Identifique as deficiências.
			4. Explique as deficiências.
			5. Gere uma versão aprimorada do requisito.
			6. Preserve o significado original, não inventando funcionalidades, regras de negócio, valores, métricas, limites, quantidades, usuários, tempos ou condições que não estejam presentes no requisito original.
			7. Quando uma informação necessária para tornar o requisito mensurável ou testável não estiver disponível, indique explicitamente a necessidade dessa informação em vez de inventar um valor. A versão aprimorada deve corrigir os problemas identificados sem introduzir novos requisitos não presentes no original.

			Observeção: Caso a qualidade geral seja maior ou igual a 7, não precisa preencher as deficiências, nem gerar a versão aprimorada e nem a avaliação da versão aprimorada.

			Retorne um objeto para cada requisito.
			""";

	public IaService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public List<RequisitoResponseDto> enviarRequisitos(RequisitosRequestDto dto) {
		try {
			return chatClient.prompt().system(INSTRUCAO).user("""
					 Analise os seguintes requisitos:

					%s
					""".formatted(dto.requisitos())).call()
					.entity(new ParameterizedTypeReference<List<RequisitoResponseDto>>() {
					});
		} catch (Exception ex) {
			System.out.println(
					"Erro ao chamar a IA: {" + ex.getClass().getName() + "}, {" + ex.getMessage() + "}, {" + ex + "}");
			throw new IaException("Não foi possível processar os requisitos com a IA.", ex);
		}

	}

}

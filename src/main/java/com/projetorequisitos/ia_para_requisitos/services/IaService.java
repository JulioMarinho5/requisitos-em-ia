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
			8. Não atribua notas máximas automaticamente a requisitos bem redigidos. Avalie cada critério individualmente. Atribua notas com base em evidências presentes no texto, não presumindo informações que não foram fornecidas.

			Observeção: Caso a qualidade geral seja maior ou igual a 7, retorne:
			- "deficiencias": []
			- "necessitaAprimoramento": false
			- "requisitoAprimorado": null
			- "avaliacaoAprimorada": null
			Assim, deixa a lista de deficiências vazia e o necessitaAprimoramento false. Não atribua notas à avaliação aprimorada nesse caso, nem gere um requisito aprimorado (serão null).

			Quando o requisito precisar de aprimoramento, retorne:
			- "deficiencias": a lista de deficiências identificadas
			- "necessitaAprimoramento": true
			- "requisitoAprimorado": uma versão aprimorada
			- "avaliacaoAprimorada": a avaliação da versão aprimorada

			Retorne um objeto para cada requisito.
			""";

	public IaService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}

	public List<RequisitoResponseDto> enviarRequisitos(RequisitosRequestDto dto) {
		try {
			List<RequisitoResponseDto> requisitosIa = chatClient.prompt().system(INSTRUCAO).user("""
					 Analise os seguintes requisitos:

					%s
					""".formatted(dto.requisitos())).call()
					.entity(new ParameterizedTypeReference<List<RequisitoResponseDto>>() {
					});
			return requisitosIa.stream().map(this::normalizarRequisito).toList();
		} catch (Exception ex) {
			System.out.println(
					"Erro ao chamar a IA: {" + ex.getClass().getName() + "}, {" + ex.getMessage() + "}, {" + ex + "}");
			throw new IaException("Não foi possível processar os requisitos com a IA.", ex);
		}

	}

	private RequisitoResponseDto normalizarRequisito(RequisitoResponseDto requisito) {
		if (!requisito.necessitaAprimoramento()) {
			return new RequisitoResponseDto(requisito.requisitoOriginal(), requisito.tipo(),
					requisito.avaliacaoOriginal(), List.of(), false, null, null);
		}
		return requisito;

	}

}

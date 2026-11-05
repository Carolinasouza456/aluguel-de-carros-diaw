package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AvaliacaoRequisicao(
        @NotNull(message = "informe se o parecer e positivo") Boolean aprovado,
        @Size(max = 500, message = "parecer muito longo") String parecer) {
}

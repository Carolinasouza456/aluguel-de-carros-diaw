package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PedidoRequisicao(
        @NotNull(message = "automovel e obrigatorio") Long automovelId,
        @NotNull(message = "data de inicio e obrigatoria") LocalDate dataInicio,
        @NotNull(message = "data final e obrigatoria") LocalDate dataFim) {
}

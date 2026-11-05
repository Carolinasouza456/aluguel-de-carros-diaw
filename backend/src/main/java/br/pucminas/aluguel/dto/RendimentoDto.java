package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RendimentoDto(
        @NotBlank(message = "entidade empregadora e obrigatoria") String entidadeEmpregadora,
        @NotNull(message = "valor e obrigatorio") @DecimalMin(value = "0.0", message = "valor nao pode ser negativo") BigDecimal valor) {
}

package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreditoDto(
        @NotNull(message = "valor do credito e obrigatorio") @DecimalMin(value = "0.01", message = "valor do credito invalido") BigDecimal valor,
        @NotNull(message = "parcelas e obrigatorio") @Min(value = 1, message = "minimo de 1 parcela") Integer parcelas) {
}

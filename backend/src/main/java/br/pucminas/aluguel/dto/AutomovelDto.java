package br.pucminas.aluguel.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutomovelDto(
        Long id,
        @NotBlank(message = "matricula e obrigatoria") String matricula,
        @NotNull(message = "ano e obrigatorio") @Min(value = 1950, message = "ano invalido") @Max(value = 2100, message = "ano invalido") Integer ano,
        @NotBlank(message = "marca e obrigatoria") String marca,
        @NotBlank(message = "modelo e obrigatorio") String modelo,
        @NotBlank(message = "placa e obrigatoria") String placa) {
}

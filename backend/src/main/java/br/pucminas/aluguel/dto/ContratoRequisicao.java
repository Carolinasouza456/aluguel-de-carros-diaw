package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.TipoProprietario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

// credito e opcional: so um banco pode conceder
public record ContratoRequisicao(
        @NotNull(message = "tipo de proprietario e obrigatorio") TipoProprietario tipoProprietario,
        @Valid CreditoDto credito) {
}

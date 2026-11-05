package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.TipoProprietario;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResposta(
        Long id,
        TipoProprietario tipoProprietario,
        LocalDate dataContrato,
        CreditoResposta credito) {

    public record CreditoResposta(BigDecimal valor, Integer parcelas, String banco) {
    }
}

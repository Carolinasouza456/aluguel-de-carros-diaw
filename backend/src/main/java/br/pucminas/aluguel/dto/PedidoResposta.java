package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.StatusPedido;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResposta(
        Long id,
        Long clienteId,
        String clienteNome,
        List<RendimentoDto> rendimentos,
        AutomovelDto automovel,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusPedido status,
        String parecer,
        String agenteAvaliador,
        LocalDateTime dataCriacao,
        ContratoResposta contrato) {
}

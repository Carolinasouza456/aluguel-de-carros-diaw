package br.pucminas.aluguel.dto;

import java.util.List;

public record ClienteResposta(
        Long id,
        String login,
        String rg,
        String cpf,
        String nome,
        String endereco,
        String profissao,
        List<RendimentoDto> rendimentos) {
}

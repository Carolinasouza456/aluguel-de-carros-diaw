package br.pucminas.aluguel.dto;

import br.pucminas.aluguel.model.Perfil;

// clienteId e agenteId: so um deles vem preenchido, conforme o perfil
public record UsuarioLogadoResposta(Long id, String login, Perfil perfil, String nome, Long clienteId, Long agenteId) {
}

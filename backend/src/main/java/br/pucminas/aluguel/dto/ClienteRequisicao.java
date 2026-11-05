package br.pucminas.aluguel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

// usado no cadastro e na atualizacao. login e senha so sao usados no cadastro
// (na atualizacao, senha preenchida troca a senha)
public record ClienteRequisicao(
        String login,
        String senha,
        @NotBlank(message = "rg e obrigatorio") String rg,
        @NotBlank(message = "cpf e obrigatorio") String cpf,
        @NotBlank(message = "nome e obrigatorio") String nome,
        @NotBlank(message = "endereco e obrigatorio") String endereco,
        String profissao,
        @Valid @Size(max = 3, message = "no maximo 3 rendimentos") List<RendimentoDto> rendimentos) {
}

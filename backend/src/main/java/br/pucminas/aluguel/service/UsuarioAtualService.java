package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.UsuarioLogadoResposta;
import br.pucminas.aluguel.excecao.AcessoNegadoException;
import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.AgenteRepository;
import br.pucminas.aluguel.repository.ClienteRepository;
import br.pucminas.aluguel.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// descobre quem e o usuario que fez a requisicao (o que esta logado)
@Service
public class UsuarioAtualService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;

    public UsuarioAtualService(UsuarioRepository usuarioRepository,
                               ClienteRepository clienteRepository,
                               AgenteRepository agenteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
    }

    public Usuario usuario() {
        var autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao == null) {
            throw new AcessoNegadoException("usuario nao autenticado");
        }
        return usuarioRepository.findByLogin(autenticacao.getName())
                .orElseThrow(() -> new AcessoNegadoException("usuario nao autenticado"));
    }

    public Cliente cliente() {
        return clienteRepository.findByUsuarioLogin(usuario().getLogin())
                .orElseThrow(() -> new AcessoNegadoException("apenas clientes podem fazer isso"));
    }

    public Agente agente() {
        return agenteRepository.findByUsuarioLogin(usuario().getLogin())
                .orElseThrow(() -> new AcessoNegadoException("apenas agentes podem fazer isso"));
    }

    @Transactional(readOnly = true)
    public UsuarioLogadoResposta dadosDoUsuarioLogado() {
        Usuario usuario = usuario();
        if (usuario.getPerfil().ehAgente()) {
            Agente agente = agente();
            return new UsuarioLogadoResposta(usuario.getId(), usuario.getLogin(), usuario.getPerfil(),
                    agente.getNome(), null, agente.getId());
        }
        Cliente cliente = cliente();
        return new UsuarioLogadoResposta(usuario.getId(), usuario.getLogin(), usuario.getPerfil(),
                cliente.getNome(), cliente.getId(), null);
    }
}

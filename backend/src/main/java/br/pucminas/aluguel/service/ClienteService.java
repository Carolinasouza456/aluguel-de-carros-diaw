package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.ClienteRequisicao;
import br.pucminas.aluguel.dto.ClienteResposta;
import br.pucminas.aluguel.dto.RendimentoDto;
import br.pucminas.aluguel.excecao.AcessoNegadoException;
import br.pucminas.aluguel.excecao.NaoEncontradoException;
import br.pucminas.aluguel.excecao.RegraDeNegocioException;
import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.Perfil;
import br.pucminas.aluguel.model.Rendimento;
import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.ClienteRepository;
import br.pucminas.aluguel.repository.PedidoRepository;
import br.pucminas.aluguel.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private static final int TAMANHO_MINIMO_SENHA = 6;

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAtualService usuarioAtualService;

    public ClienteService(ClienteRepository clienteRepository,
                          UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository,
                          PasswordEncoder passwordEncoder,
                          UsuarioAtualService usuarioAtualService) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAtualService = usuarioAtualService;
    }

    // create: cadastro de um novo cliente (nao precisa estar logado)
    @Transactional
    public ClienteResposta cadastrar(ClienteRequisicao requisicao) {
        if (requisicao.login() == null || requisicao.login().isBlank()) {
            throw new RegraDeNegocioException("login e obrigatorio");
        }
        validarSenha(requisicao.senha());
        if (usuarioRepository.existsByLogin(requisicao.login())) {
            throw new RegraDeNegocioException("esse login ja esta em uso");
        }
        if (clienteRepository.existsByCpf(requisicao.cpf())) {
            throw new RegraDeNegocioException("ja existe um cliente com esse cpf");
        }

        Usuario usuario = new Usuario(requisicao.login().trim(),
                passwordEncoder.encode(requisicao.senha()), Perfil.CLIENTE);
        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        preencherDados(cliente, requisicao);
        return paraResposta(clienteRepository.save(cliente));
    }

    // read: lista todos (somente agentes, controlado no controller)
    @Transactional(readOnly = true)
    public List<ClienteResposta> listar() {
        return clienteRepository.findAll().stream().map(this::paraResposta).toList();
    }

    // read: um cliente (agente ou o proprio cliente)
    @Transactional(readOnly = true)
    public ClienteResposta buscar(Long id) {
        Cliente cliente = buscarEntidade(id);
        verificarAcesso(cliente);
        return paraResposta(cliente);
    }

    // update
    @Transactional
    public ClienteResposta atualizar(Long id, ClienteRequisicao requisicao) {
        Cliente cliente = buscarEntidade(id);
        verificarAcesso(cliente);
        if (clienteRepository.existsByCpfAndIdNot(requisicao.cpf(), id)) {
            throw new RegraDeNegocioException("ja existe um cliente com esse cpf");
        }
        preencherDados(cliente, requisicao);
        // senha so troca se vier preenchida
        if (requisicao.senha() != null && !requisicao.senha().isBlank()) {
            validarSenha(requisicao.senha());
            cliente.getUsuario().setSenha(passwordEncoder.encode(requisicao.senha()));
        }
        return paraResposta(clienteRepository.save(cliente));
    }

    // delete: apaga os pedidos do cliente e depois o cliente (e seu usuario)
    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscarEntidade(id);
        verificarAcesso(cliente);
        pedidoRepository.deleteAll(pedidoRepository.findByClienteId(id));
        clienteRepository.delete(cliente);
    }

    private Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("cliente nao encontrado"));
    }

    // agente mexe em qualquer cliente, cliente so mexe em si mesmo
    private void verificarAcesso(Cliente cliente) {
        Usuario logado = usuarioAtualService.usuario();
        if (logado.getPerfil().ehAgente()) {
            return;
        }
        if (!cliente.getUsuario().getId().equals(logado.getId())) {
            throw new AcessoNegadoException("voce so pode acessar o seu proprio cadastro");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            throw new RegraDeNegocioException("a senha precisa ter pelo menos " + TAMANHO_MINIMO_SENHA + " caracteres");
        }
    }

    private void preencherDados(Cliente cliente, ClienteRequisicao requisicao) {
        List<RendimentoDto> rendimentos = requisicao.rendimentos() == null ? List.of() : requisicao.rendimentos();
        if (rendimentos.size() > Cliente.MAXIMO_RENDIMENTOS) {
            throw new RegraDeNegocioException("no maximo " + Cliente.MAXIMO_RENDIMENTOS + " rendimentos");
        }
        cliente.setRg(requisicao.rg().trim());
        cliente.setCpf(requisicao.cpf().trim());
        cliente.setNome(requisicao.nome().trim());
        cliente.setEndereco(requisicao.endereco().trim());
        cliente.setProfissao(requisicao.profissao());
        // troca a lista inteira de rendimentos
        cliente.getRendimentos().clear();
        for (RendimentoDto dto : rendimentos) {
            cliente.adicionarRendimento(new Rendimento(dto.entidadeEmpregadora(), dto.valor()));
        }
    }

    private ClienteResposta paraResposta(Cliente cliente) {
        List<RendimentoDto> rendimentos = cliente.getRendimentos().stream()
                .map(r -> new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor()))
                .toList();
        return new ClienteResposta(cliente.getId(), cliente.getUsuario().getLogin(), cliente.getRg(),
                cliente.getCpf(), cliente.getNome(), cliente.getEndereco(), cliente.getProfissao(), rendimentos);
    }
}

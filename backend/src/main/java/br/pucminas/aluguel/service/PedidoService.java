package br.pucminas.aluguel.service;

import br.pucminas.aluguel.dto.AvaliacaoRequisicao;
import br.pucminas.aluguel.dto.ContratoRequisicao;
import br.pucminas.aluguel.dto.ContratoResposta;
import br.pucminas.aluguel.dto.PedidoRequisicao;
import br.pucminas.aluguel.dto.PedidoResposta;
import br.pucminas.aluguel.dto.RendimentoDto;
import br.pucminas.aluguel.excecao.AcessoNegadoException;
import br.pucminas.aluguel.excecao.NaoEncontradoException;
import br.pucminas.aluguel.excecao.RegraDeNegocioException;
import br.pucminas.aluguel.model.Agente;
import br.pucminas.aluguel.model.Automovel;
import br.pucminas.aluguel.model.Cliente;
import br.pucminas.aluguel.model.Contrato;
import br.pucminas.aluguel.model.ContratoCredito;
import br.pucminas.aluguel.model.Pedido;
import br.pucminas.aluguel.model.StatusPedido;
import br.pucminas.aluguel.model.Usuario;
import br.pucminas.aluguel.repository.AutomovelRepository;
import br.pucminas.aluguel.repository.PedidoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final UsuarioAtualService usuarioAtualService;

    public PedidoService(PedidoRepository pedidoRepository,
                         AutomovelRepository automovelRepository,
                         UsuarioAtualService usuarioAtualService) {
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.usuarioAtualService = usuarioAtualService;
    }

    // cliente introduz um pedido, que comeca em analise
    @Transactional
    public PedidoResposta criar(PedidoRequisicao requisicao) {
        Cliente cliente = usuarioAtualService.cliente();
        validarDatas(requisicao);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setAutomovel(buscarAutomovel(requisicao.automovelId()));
        pedido.setDataInicio(requisicao.dataInicio());
        pedido.setDataFim(requisicao.dataFim());
        pedido.setStatus(StatusPedido.EM_ANALISE);
        return paraResposta(pedidoRepository.save(pedido));
    }

    // cliente ve so os seus pedidos, agente ve todos
    @Transactional(readOnly = true)
    public List<PedidoResposta> listar() {
        Usuario logado = usuarioAtualService.usuario();
        List<Pedido> pedidos;
        if (logado.getPerfil().ehAgente()) {
            pedidos = pedidoRepository.findAllByOrderByDataCriacaoDesc();
        } else {
            pedidos = pedidoRepository.findByClienteIdOrderByDataCriacaoDesc(usuarioAtualService.cliente().getId());
        }
        return pedidos.stream().map(this::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResposta buscar(Long id) {
        Pedido pedido = buscarEntidade(id);
        verificarAcesso(pedido);
        return paraResposta(pedido);
    }

    // cliente ou agente modificam o pedido enquanto ele esta em analise
    @Transactional
    public PedidoResposta modificar(Long id, PedidoRequisicao requisicao) {
        Pedido pedido = buscarEntidade(id);
        verificarAcesso(pedido);
        if (pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new RegraDeNegocioException("so e possivel modificar pedidos em analise");
        }
        validarDatas(requisicao);
        pedido.setAutomovel(buscarAutomovel(requisicao.automovelId()));
        pedido.setDataInicio(requisicao.dataInicio());
        pedido.setDataFim(requisicao.dataFim());
        return paraResposta(pedidoRepository.save(pedido));
    }

    // cliente cancela o proprio pedido
    @Transactional
    public PedidoResposta cancelar(Long id) {
        Pedido pedido = buscarEntidade(id);
        verificarAcesso(pedido);
        if (pedido.getStatus() != StatusPedido.EM_ANALISE && pedido.getStatus() != StatusPedido.APROVADO) {
            throw new RegraDeNegocioException("um pedido " + pedido.getStatus() + " nao pode ser cancelado");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return paraResposta(pedidoRepository.save(pedido));
    }

    // agente da o parecer financeiro
    @Transactional
    public PedidoResposta avaliar(Long id, AvaliacaoRequisicao requisicao) {
        Agente agente = usuarioAtualService.agente();
        Pedido pedido = buscarEntidade(id);
        if (pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new RegraDeNegocioException("so e possivel avaliar pedidos em analise");
        }
        pedido.setStatus(requisicao.aprovado() ? StatusPedido.APROVADO : StatusPedido.REPROVADO);
        pedido.setParecer(requisicao.parecer());
        pedido.setAgenteAvaliador(agente);
        return paraResposta(pedidoRepository.save(pedido));
    }

    // agente executa o contrato de um pedido aprovado
    @Transactional
    public PedidoResposta contratar(Long id, ContratoRequisicao requisicao) {
        Agente agente = usuarioAtualService.agente();
        Pedido pedido = buscarEntidade(id);
        if (pedido.getStatus() != StatusPedido.APROVADO) {
            throw new RegraDeNegocioException("so e possivel contratar pedidos aprovados");
        }

        Contrato contrato = new Contrato();
        contrato.setPedido(pedido);
        contrato.setTipoProprietario(requisicao.tipoProprietario());

        if (requisicao.credito() != null) {
            if (!agente.ehBanco()) {
                throw new RegraDeNegocioException("somente um banco pode conceder credito");
            }
            ContratoCredito credito = new ContratoCredito();
            credito.setContrato(contrato);
            credito.setBanco(agente);
            credito.setValor(requisicao.credito().valor());
            credito.setParcelas(requisicao.credito().parcelas());
            contrato.setContratoCredito(credito);
        }

        pedido.setContrato(contrato);
        pedido.setStatus(StatusPedido.CONTRATADO);
        return paraResposta(pedidoRepository.save(pedido));
    }

    private Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("pedido nao encontrado"));
    }

    private Automovel buscarAutomovel(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("automovel nao encontrado"));
    }

    private void validarDatas(PedidoRequisicao requisicao) {
        if (requisicao.dataInicio().isBefore(LocalDate.now())) {
            throw new RegraDeNegocioException("a data de inicio nao pode estar no passado");
        }
        if (requisicao.dataFim().isBefore(requisicao.dataInicio())) {
            throw new RegraDeNegocioException("a data final nao pode ser anterior a data de inicio");
        }
    }

    // agente ve qualquer pedido, cliente so os dele
    private void verificarAcesso(Pedido pedido) {
        Usuario logado = usuarioAtualService.usuario();
        if (logado.getPerfil().ehAgente()) {
            return;
        }
        if (!pedido.getCliente().getUsuario().getId().equals(logado.getId())) {
            throw new AcessoNegadoException("esse pedido nao e seu");
        }
    }

    private PedidoResposta paraResposta(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        List<RendimentoDto> rendimentos = cliente.getRendimentos().stream()
                .map(r -> new RendimentoDto(r.getEntidadeEmpregadora(), r.getValor()))
                .toList();
        String avaliador = pedido.getAgenteAvaliador() == null ? null : pedido.getAgenteAvaliador().getNome();
        return new PedidoResposta(pedido.getId(), cliente.getId(), cliente.getNome(), rendimentos,
                AutomovelService.paraDto(pedido.getAutomovel()), pedido.getDataInicio(), pedido.getDataFim(),
                pedido.getStatus(), pedido.getParecer(), avaliador, pedido.getDataCriacao(),
                contratoParaResposta(pedido.getContrato()));
    }

    private ContratoResposta contratoParaResposta(Contrato contrato) {
        if (contrato == null) {
            return null;
        }
        ContratoResposta.CreditoResposta credito = null;
        ContratoCredito c = contrato.getContratoCredito();
        if (c != null) {
            credito = new ContratoResposta.CreditoResposta(c.getValor(), c.getParcelas(), c.getBanco().getNome());
        }
        return new ContratoResposta(contrato.getId(), contrato.getTipoProprietario(),
                contrato.getDataContrato(), credito);
    }
}

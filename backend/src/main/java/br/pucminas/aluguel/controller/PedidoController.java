package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.AvaliacaoRequisicao;
import br.pucminas.aluguel.dto.ContratoRequisicao;
import br.pucminas.aluguel.dto.PedidoRequisicao;
import br.pucminas.aluguel.dto.PedidoResposta;
import br.pucminas.aluguel.service.PedidoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResposta criar(@Valid @RequestBody PedidoRequisicao requisicao) {
        return pedidoService.criar(requisicao);
    }

    @GetMapping
    public List<PedidoResposta> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoResposta buscar(@PathVariable Long id) {
        return pedidoService.buscar(id);
    }

    @PutMapping("/{id}")
    public PedidoResposta modificar(@PathVariable Long id, @Valid @RequestBody PedidoRequisicao requisicao) {
        return pedidoService.modificar(id, requisicao);
    }

    @PatchMapping("/{id}/cancelamento")
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResposta cancelar(@PathVariable Long id) {
        return pedidoService.cancelar(id);
    }

    @PatchMapping("/{id}/avaliacao")
    @PreAuthorize("hasAnyRole('EMPRESA', 'BANCO')")
    public PedidoResposta avaliar(@PathVariable Long id, @Valid @RequestBody AvaliacaoRequisicao requisicao) {
        return pedidoService.avaliar(id, requisicao);
    }

    @PostMapping("/{id}/contrato")
    @PreAuthorize("hasAnyRole('EMPRESA', 'BANCO')")
    public PedidoResposta contratar(@PathVariable Long id, @Valid @RequestBody ContratoRequisicao requisicao) {
        return pedidoService.contratar(id, requisicao);
    }
}

package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.ClienteRequisicao;
import br.pucminas.aluguel.dto.ClienteResposta;
import br.pucminas.aluguel.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// crud de cliente
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // rota publica: e o cadastro previo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResposta cadastrar(@Valid @RequestBody ClienteRequisicao requisicao) {
        return clienteService.cadastrar(requisicao);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPRESA', 'BANCO')")
    public List<ClienteResposta> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResposta buscar(@PathVariable Long id) {
        return clienteService.buscar(id);
    }

    @PutMapping("/{id}")
    public ClienteResposta atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequisicao requisicao) {
        return clienteService.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        clienteService.excluir(id);
    }
}

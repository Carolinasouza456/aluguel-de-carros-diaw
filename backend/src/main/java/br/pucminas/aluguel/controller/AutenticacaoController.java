package br.pucminas.aluguel.controller;

import br.pucminas.aluguel.dto.UsuarioLogadoResposta;
import br.pucminas.aluguel.service.UsuarioAtualService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    private final UsuarioAtualService usuarioAtualService;

    public AutenticacaoController(UsuarioAtualService usuarioAtualService) {
        this.usuarioAtualService = usuarioAtualService;
    }

    // o front-end chama essa rota com login e senha para saber se o login e valido
    // (se as credenciais estiverem erradas o spring security responde 401 antes de chegar aqui)
    @GetMapping("/eu")
    public UsuarioLogadoResposta eu() {
        return usuarioAtualService.dadosDoUsuarioLogado();
    }
}

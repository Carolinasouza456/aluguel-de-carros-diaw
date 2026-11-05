package br.pucminas.aluguel.excecao;

// usuario logado nao pode mexer nesse registro: vira resposta 403
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}

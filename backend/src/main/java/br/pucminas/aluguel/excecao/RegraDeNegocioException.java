package br.pucminas.aluguel.excecao;

// erro de regra de negocio: vira resposta 400
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}

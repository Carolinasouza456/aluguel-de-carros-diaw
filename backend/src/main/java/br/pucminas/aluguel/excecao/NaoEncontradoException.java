package br.pucminas.aluguel.excecao;

// registro nao existe: vira resposta 404
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

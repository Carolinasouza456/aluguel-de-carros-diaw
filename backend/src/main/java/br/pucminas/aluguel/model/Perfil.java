package br.pucminas.aluguel.model;

public enum Perfil {
    CLIENTE,
    EMPRESA,
    BANCO;

    // empresas e bancos sao agentes
    public boolean ehAgente() {
        return this == EMPRESA || this == BANCO;
    }
}

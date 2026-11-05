# Diagrama de classes

Os nomes das classes e atributos são os mesmos do código em `backend/src/main/java/br/pucminas/aluguel/model`.

```mermaid
classDiagram
    class Perfil {
        <<enumeration>>
        CLIENTE
        EMPRESA
        BANCO
    }
    class StatusPedido {
        <<enumeration>>
        EM_ANALISE
        APROVADO
        REPROVADO
        CANCELADO
        CONTRATADO
    }
    class TipoProprietario {
        <<enumeration>>
        CLIENTE
        EMPRESA
        BANCO
    }

    class Usuario {
        Long id
        String login
        String senha
        Perfil perfil
    }
    class Cliente {
        Long id
        String rg
        String cpf
        String nome
        String endereco
        String profissao
    }
    class Rendimento {
        Long id
        String entidadeEmpregadora
        BigDecimal valor
    }
    class Agente {
        Long id
        String nome
    }
    class Automovel {
        Long id
        String matricula
        Integer ano
        String marca
        String modelo
        String placa
    }
    class Pedido {
        Long id
        LocalDate dataInicio
        LocalDate dataFim
        StatusPedido status
        String parecer
        LocalDateTime dataCriacao
    }
    class Contrato {
        Long id
        TipoProprietario tipoProprietario
        LocalDate dataContrato
    }
    class ContratoCredito {
        Long id
        BigDecimal valor
        Integer parcelas
    }

    Usuario "1" --> "1" Perfil
    Cliente "1" --> "1" Usuario : acesso
    Agente "1" --> "1" Usuario : acesso
    Cliente "1" *-- "0..3" Rendimento
    Cliente "1" <-- "0..*" Pedido : solicita
    Pedido "0..*" --> "1" Automovel : para
    Agente "0..1" <-- "0..*" Pedido : avaliado por
    Pedido "1" --> "1" StatusPedido
    Pedido "1" *-- "0..1" Contrato
    Contrato "1" --> "1" TipoProprietario
    Contrato "1" *-- "0..1" ContratoCredito
    ContratoCredito "0..*" --> "1" Agente : concedido pelo banco
```

## Decisões de modelagem

- Cliente e Agente (empresa ou banco) têm um Usuario cada, que guarda login, senha e perfil. O perfil decide o que a pessoa pode fazer.
- O enunciado diz que o automóvel pode ser propriedade de clientes, empresas ou bancos conforme o tipo de contrato. Por isso o proprietário fica no Contrato (TipoProprietario) e não no Automovel.
- O contrato de crédito é opcional e só existe dentro de um contrato. O agente dele precisa ter perfil BANCO.
- Máximo de 3 rendimentos por cliente: validado no back-end (DTO e serviço) e no front-end.

# Histórias de usuário

## Cliente

**HU01 - Cadastro**
Como cliente, quero me cadastrar com usuário e senha, meus dados de identificação, profissão e até 3 rendimentos, para poder usar o sistema.
- RG, CPF, nome e endereço são obrigatórios
- cada rendimento tem entidade empregadora e valor
- não é possível informar mais de 3 rendimentos
- login e CPF não podem repetir

**HU02 - Login**
Como cliente, quero entrar com usuário e senha, para que só eu acesse os meus dados.
- sem login, nenhuma tela (a não ser cadastro e login) fica disponível
- senha incorreta mostra mensagem de erro

**HU03 - Manter meus dados**
Como cliente, quero consultar, alterar e excluir o meu cadastro, para manter as informações atualizadas.

**HU04 - Introduzir pedido**
Como cliente, quero escolher um automóvel e informar o período, para pedir o aluguel.
- o pedido começa com status EM_ANALISE
- a data final não pode ser anterior à inicial

**HU05 - Consultar pedidos**
Como cliente, quero ver a lista dos meus pedidos com o status de cada um, para acompanhar a análise.

**HU06 - Modificar pedido**
Como cliente, quero alterar o automóvel ou o período de um pedido, para corrigir o que pedi.
- só é possível enquanto o status for EM_ANALISE

**HU07 - Cancelar pedido**
Como cliente, quero cancelar um pedido, para desistir do aluguel.
- só é possível cancelar pedidos EM_ANALISE ou APROVADO (um pedido CONTRATADO, REPROVADO ou já CANCELADO não pode)

## Agente (empresa ou banco)

**HU08 - Login do agente**
Como agente, quero entrar com usuário e senha, para analisar pedidos.

**HU09 - Consultar pedidos**
Como agente, quero ver todos os pedidos e os dados financeiros do cliente (rendimentos), para fazer a análise.

**HU10 - Modificar pedido**
Como agente, quero ajustar um pedido EM_ANALISE, para corrigir automóvel ou período junto com o cliente.

**HU11 - Avaliar pedido**
Como agente, quero dar parecer positivo ou negativo com uma justificativa, para decidir se o pedido segue para contrato.
- pedido aprovado vira APROVADO, reprovado vira REPROVADO

**HU12 - Executar contrato**
Como agente, quero criar o contrato de um pedido APROVADO, informando quem fica como proprietário do automóvel (cliente, empresa ou banco).
- o pedido passa a CONTRATADO

**HU13 - Contrato de crédito**
Como banco, quero associar um contrato de crédito (valor e parcelas) ao aluguel, para financiar o automóvel.
- só um agente do tipo banco concede crédito

**HU14 - Manter automóveis**
Como agente, quero cadastrar automóveis com matrícula, ano, marca, modelo e placa, para que fiquem disponíveis nos pedidos.

## Requisitos não funcionais

- RNF01: sistema web
- RNF02: back-end em Java (JDK 25) com Spring Boot 4.0.x e Maven, arquitetura MVC
- RNF03: front-end em React + Vite
- RNF04: banco de dados Supabase (PostgreSQL)
- RNF05: autenticação obrigatória com usuário e senha; senha guardada com hash BCrypt

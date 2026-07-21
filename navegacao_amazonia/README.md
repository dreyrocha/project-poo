# Sistema de Embarque - Navegação Amazônia

Sistema para gerenciamento de viagens fluviais, desenvolvido como projeto final da disciplina de Programação Orientada a Objetos (Programação II), sob orientação da Profa. Dra. Flávia Monteiro — UFPA/ICEN/Faculdade de Computação.

## Descrição

O sistema permite que empresas de transporte fluvial cadastrem locais, rotas, embarcações (lanchas e balsas) e agendem viagens. Passageiros podem consultar as viagens disponíveis, filtrar por data ou rota, ver detalhes das embarcações e o contato das empresas.

## Funcionalidades

**Empresa** (requer login):
- Cadastro de locais (portos/terminais)
- Cadastro de embarcações (Lancha ou Balsa)
- Cadastro de rotas
- Agendamento, cancelamento e atualização de horário de viagens
- Listagem de viagens e embarcações cadastradas
- Histórico de acessos (login/logout)

**Passageiro** (sem login):
- Visualização de viagens disponíveis
- Filtro de viagens por data
- Filtro de viagens por rota
- Visualização de detalhes de uma embarcação
- Visualização do contato de uma empresa

## Estrutura do projeto

    src/
    ├── main/java/
    │   ├── app/       -> Main (fluxo de negócio) e ConsoleUI (entrada/saída no terminal)
    │   ├── model/     -> Embarcacao (abstrata), Lancha, Balsa, Local, Rota, Viagem
    │   └── service/   -> Empresa, Passageiro, Autenticavel (interface), HistoricoDeAcessos
    └── test/java/
        └── test/      -> ViagemIdTest (teste de geração de IDs)

## Conceitos de POO aplicados

- **Herança**: `Lancha` e `Balsa` herdam de `Embarcacao`
- **Polimorfismo**: método `descricao()` sobrescrito em cada subclasse
- **Abstração**: `Embarcacao` é uma classe abstrata
- **Interface**: `Autenticavel`, implementada por `Empresa`
- **Encapsulamento**: atributos privados com getters/setters validados
- **Composição**: `Empresa` compõe seu próprio `HistoricoDeAcessos`
- **Associação/Dependência**: `Viagem` associa `Rota`, `Empresa` e `Embarcacao`; `Passageiro` depende de `Viagem`, `Embarcacao` e `Empresa`

## Requisitos

- Java 17 ou superior (JDK)

## Como compilar e executar

### Via terminal (sem Maven/Gradle)

Compilar:

    javac -d out $(find src/main/java -name "*.java")

Executar:

    java -cp out app.Main

### Executando o teste

    javac -d out $(find src/main/java src/test/java -name "*.java")
    java -cp out test.ViagemIdTest

## Dados de demonstração

Ao iniciar, o sistema já cria automaticamente:
- Uma empresa: Navegação Amazônia (e-mail: contato@navamazonia.com, senha: senha123)
- Dois locais: Porto de Belém e Porto de Soure
- Uma rota: Belém → Soure
- Duas embarcações: Lancha Veloz e Balsa Grande
- Duas viagens agendadas

## Observações

- Os dados são armazenados apenas em memória (não há persistência em arquivo ou banco de dados); ao encerrar o programa, todos os cadastros são perdidos.
- As senhas de empresa são armazenadas como hash (SHA-256), nunca em texto puro.



# 🏢 Sistema de Reserva de Salas

Este é um sistema robusto desenvolvido em **Java** para gerenciar a ocupação de espaços acadêmicos em diferentes departamentos (CCOMP, Economia, Zootecnia, etc.). O projeto foca em integridade de dados e usabilidade, utilizando uma interface gráfica para facilitar o gerenciamento de recursos.

## 🚀 Funcionalidades e Diferenciais

* **Gestão Completa (CRUD)**: Controle total sobre o cadastro de Pessoas e Salas, permitindo inserção, listagem, edição e exclusão.
* **Reservas Inteligentes e Múltiplas**: 
    * Permite criar uma única reserva contendo vários itens (diferentes salas e horários) para o mesmo responsável.
    * **Algoritmo de Conflito**: O sistema realiza uma varredura automática em tempo real para garantir que uma sala não seja reservada em períodos sobrepostos.
    * **Edição Flexível**: Possibilidade de atualizar reservas existentes, alterando o responsável ou os itens reservados, com revalidação imediata de horários.
* **Segurança e Validação**: 
    * Tratamento de exceções customizadas para IDs inexistentes ou entradas inválidas.
    * Validação rigorosa de campos (ex: nomes que aceitam apenas letras).
* **Dados Iniciais (Bootstrap)**: O sistema já inicia com uma base de dados pré-carregada com professores e salas da UFSJ para demonstração imediata.

## 🛠️ Tecnologias e Arquitetura:

* **Linguagem**: Java.
* **Arquitetura**: Organizada em pacotes seguindo a separação de responsabilidades (Modelo, Persistência e Visão/Serviço).
* **Camada de Serviço**: Centralização da lógica de negócio na classe `SistemaReservaService`.
* **Persistência**: Implementação de um banco de dados em memória utilizando tipos genéricos (`Persistente<T>`).
* **Interface**: Swing (GUI) para uma interação amigável com o usuário.
* **Testes**: Suporte a testes automatizados com JUnit 4.

## 📂 Estrutura do Projeto

* `sistemareserva.modelo`: Entidades principais como `Pessoa`, `Sala` e `Reserva`.
* `sistemareserva.persistencia`: Gerenciamento do `BancoDeDados` e exceções personalizadas.
* `sistemareserva.visao`: Interface gráfica (`ReservaGUI`) e o motor de regras do sistema (`SistemaReservaService`).

## ⚙️ Como Executar

Certifique-se de estar na raiz do diretório do projeto e execute os comandos abaixo no terminal:

### 1. Compilar
```bash
javac -d out sistemareserva\modelo\*.java sistemareserva\persistencia\*.java sistemareserva\visao\*.java

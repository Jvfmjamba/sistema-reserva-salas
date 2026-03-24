# 🏢 Sistema de Reserva de Salas

Este é um sistema desenvolvido em **Java** para gerenciar a reserva de salas em diferentes prédios (CCOMP, Economia, Zootecnia, etc.). O projeto permite o cadastro de usuários, salas e a gestão de reservas, garantindo que não haja conflitos de horários para a mesma sala.

## 🚀 Funcionalidades

* **Gestão de Pessoas**: Cadastro, listagem, edição e exclusão de usuários.
* **Gestão de Salas**: Cadastro de salas com definição de prédio e capacidade de alunos.
* **Reservas Inteligentes**: 
    * Criação de reservas simples ou múltiplas (várias salas/horários em uma única reserva).
    * Verificação automática de conflitos de horários.
    * Cancelamento e atualização de reservas existentes.
* **Dados Iniciais**: O sistema já inicia com uma lista pré-cadastrada de professores (como Prof. Matheus e Prof. Elverton) e salas da UFSJ para facilitar os testes.

## 🛠️ Tecnologias Utilizadas

* **Linguagem**: Java.
* **Persistência**: Sistema de persistência em memória (Simulação de Banco de Dados).
* **Interface**: Swing (GUI).
* **Testes**: JUnit 4 (bibliotecas inclusas no projeto).

## 📂 Estrutura do Projeto

O código está organizado seguindo padrões de separação de responsabilidades:
* `modelo`: Classes de entidade (Pessoa, Sala, Reserva, ItemReserva).
* `persistencia`: Lógica de armazenamento e tratamento de exceções.
* `visao`: Interface gráfica e a camada de serviço (`SistemaReservaService`) que conecta a interface ao banco.

## ⚙️ Como Executar

Para compilar e rodar o projeto via terminal, siga os passos abaixo (certifique-se de estar na raiz do projeto):

### 1. Compilar
```bash
javac -d out sistemareserva\modelo\*.java sistemareserva\persistencia\*.java sistemareserva\visao\*.java

package sistemareserva.visao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors; // precisa desse import pro novo filtro de atributos

import sistemareserva.modelo.*;
import sistemareserva.persistencia.*;

public class Programa {
    private static BancoDeDados banco = new BancoDeDados();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        carregarDadosIniciais();
        System.out.println("\n=== SISTEMA DE RESERVA DE SALAS ===");
        System.out.println("Bem-vindo ao sistema de gerenciamento de reservas!\n");
        // ... (O resto do main continua igual) ...
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarPessoa();
                    break;
                case 2:
                    criarReserva();
                    break;
                case 3:
                    listarSalas();
                    break;
                case 4:
                    buscarReservasPorPessoa();
                    break;
                case 5:
                    cancelarReserva();
                    break;
                case 6:
                    buscarSalasDisponiveis();
                    break;
                case 7:
                    listarPessoas();
                    break;
                case 8:
                    criarSala();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            System.out.println();
        } while (opcao != 0);

        //fecha a leitura de dados
        scanner.close();
    }

    // ... (carregarDadosIniciais, exibirMenuPrincipal e cadastrarPessoa continuam iguais) ...
    private static void carregarDadosIniciais() {
        System.out.println("Inicializando sistema e carregando dados...");
        banco.getPessoas().inserir(new Pessoa("Professor Girafales"));
        banco.getPessoas().inserir(new Pessoa("Dona Florinda"));
        banco.getPessoas().inserir(new Pessoa("Seu Madruga"));
        banco.getPessoas().inserir(new Pessoa("Professor Matheus"));

        banco.getSalas().inserir(new Sala("CCOMP", 50, true, true, false, true));
        banco.getSalas().inserir(new Sala("CCOMP", 30, true, false, false, true));
        banco.getSalas().inserir(new Sala("CCOMP", 100, true, true, true, true));
        banco.getSalas().inserir(new Sala("CCOMP", 10, true, false, false, false));
        banco.getSalas().inserir(new Sala("ZOOTEC", 25, false, false, false, false));
        banco.getSalas().inserir(new Sala("ARQUIT", 40, true, true, false, true));


    }

    //apenas imprime o menu principal
    private static void exibirMenuPrincipal() {
        System.out.println("===== MENU PRINCIPAL =====");
        System.out.println("1 - Cadastrar Nova Pessoa");
        System.out.println("2 - Criar Reserva");
        System.out.println("3 - Listar Todas as Salas");
        System.out.println("4 - Buscar Minhas Reservas");
        System.out.println("5 - Cancelar Reserva");
        System.out.println("6 - Buscar Salas Disponíveis por Período");
        System.out.println("7 - Listar Todas as Pessoas");
        System.out.println("8 - Criar Nova Sala");
        System.out.println("0 - Sair");
        System.out.println("===========================");
    }

    //cadastra uma pessoa nova com base no persistencia.bancodedados
    private static void cadastrarPessoa() {
        System.out.println("\n--- CADASTRAR PESSOA ---");
        System.out.print("Nome da pessoa: ");
        String nome = scanner.nextLine().trim();
        //checagem anti batata
        if (nome.isEmpty()) {
            System.out.println("Erro: Nome não pode estar vazio!");
            return;
        }
        Pessoa pessoa = new Pessoa(nome);
        //puxa o banco de dados pra iniciar uma pessoa
        banco.getPessoas().inserir(pessoa);
        //diz que cadastrou com sucesso e puxa o id da pessoa do modelo.entidade
        System.out.println("Pessoa cadastrada com sucesso! ID: " + pessoa.getId());
    }

    //alexandre aqui vou ter que mudar toda a logica de criar reserva, por causa da mudança que o matheus pediu

    //*******************************************
    // INÍCIO DA ALTERAÇÃO - MÉTODO 'criarReserva'
    // Este método foi reescrito para se adaptar ao novo modelo
    // onde as datas ficam em ItemReserva, e não em Reserva.
    //*******************************************
    private static void criarReserva() {
        System.out.println("\n--- CRIAR RESERVA ---");

        // a logica mudou, agr a gente pede o responsavel primeiro
        int idResponsavel =lerInteiro("Digite o SEU ID para a reserva (ou digite 0 para cancelar): ");
        if (idResponsavel == 0){
            System.out.println("Operação de reserva cancelada pelo usuário.");
            return;
        }
        Pessoa responsavel =banco.getPessoas().buscaId(idResponsavel);

        if (responsavel==null){
            System.out.println("Erro: Pessoa não encontrada!");
            return;
        }

        // criar reserva (agora só com o responsável)
        Reserva reserva = new Reserva(responsavel);

        // adicionar salas à reserva
        boolean adicionandoSalas = true;
        while (adicionandoSalas) {
            System.out.println("\n--- Adicionando Sala à Reserva (Responsável: " + responsavel.getNome() + ") ---");
            listarSalas(); // Mostra as salas cadastradas

            //se digitar 0 ele sai do metodo
            int idSala = lerInteiro("Digite o ID da sala para adicionar à reserva (0 para concluir): ");

            if (idSala == 0) {
                //checa se existe uma sala acessando um objeto dela que no caso é o objeto item
                if (reserva.getItensDaReserva().isEmpty()) {
                    System.out.println("Erro: A reserva deve ter pelo menos uma sala!");
                    continue; // Volta ao início do loop
                }
                //interrompe a variavel para parar de adicionar
                adicionandoSalas = false;
            } else {
                //vai adicionando salas e criando
                Sala salaEscolhida = banco.getSalas().buscaId(idSala);
                if (salaEscolhida == null) {
                    System.out.println("Erro: Sala com ID " + idSala + " não encontrada.");
                    continue; // Volta ao início do loop
                }

                // pede a data e hora pra esse item especifico
                System.out.println("Defina o período para a Sala ID " + salaEscolhida.getId() + ":");
                // ler data e hora com funcao nativa java
                LocalDateTime dataHoraInicio = lerDataHora("  Data e hora de início (dd/MM/yyyy HH:mm): ");
                LocalDateTime dataHoraFim = lerDataHora("  Data e hora de fim (dd/MM/yyyy HH:mm): ");

                //checagem anti batata do usuario
                if (dataHoraFim.isBefore(dataHoraInicio) || dataHoraFim.isEqual(dataHoraInicio)) {
                    System.out.println("Erro: Data/hora de fim deve ser posterior à de início!");
                    continue; // Volta ao início do loop
                }

                // verifica o conflito pra essa sala pra esse hoario
                if (salaEstaOcupada(salaEscolhida, dataHoraInicio, dataHoraFim)) {
                    System.out.println("ERRO: A sala " + salaEscolhida.getId() + " já está ocupada nesse período!");
                    System.out.println("Por favor, escolha outra sala ou outro horário.");
                } else {
                    //confere se a sala existe e reserva ela
                    ItemReserva item = new ItemReserva(salaEscolhida, dataHoraInicio, dataHoraFim);
                    reserva.adicionarItem(item);
                    System.out.println(">>> Sala '" + salaEscolhida.getId() + "' adicionada com sucesso! <<<");
                }
            }
        }

        // registra aa reserva no banco de dados
        banco.getReservas().inserir(reserva);
        // adiciona a reserva na lista da pessoa 
        responsavel.adicionarReserva(reserva);
        
        System.out.println("Reserva criada com sucesso! ID: " + reserva.getId());
        //mostra o resumo da reserva usando o novo toString() de Reserva
        System.out.println(reserva.toString());
    }

     //alexandre aqui vou ter que mudar toda a logica de criar reserva, por causa da mudança que o matheus pediu

     

    //exibe as pessoas ja criadas
    private static void listarPessoas() {
        System.out.println("\n--- LISTA DE TODAS AS PESSOAS CADASTRADAS ---");
        //acessa o banco de dados e percorre a lista de pessoas criadas
        List<Pessoa> pessoas = banco.getPessoas().listarTodos();
        //isEmpty é outra forma (nativa do java) de saber se um array esta vazio
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
        } else {
            System.out.println("Total de pessoas cadastradas: " + pessoas.size());
            System.out.println("----------------------------------------------");
            for (Pessoa pessoa : pessoas) {
                System.out.println("ID: " + pessoa.getId() + " | Nome: " + pessoa.getNome());
            }
            System.out.println("----------------------------------------------");
        }
    }

    private static void listarSalas() {
        System.out.println("\n--- LISTA DE TODAS AS SALAS ---");
        List<Sala> salas = banco.getSalas().listarTodos();

        if (salas.isEmpty()) {
            System.out.println("Nenhuma sala cadastrada.");
        } else {
            for (Sala sala : salas) {
                System.out.println(sala);
            }
        }
    }

    //alexandre aqui to mudando a logica da funcao buscarreservas por pessoas tmb


    private static void buscarReservasPorPessoa() {
        System.out.println("\n--- BUSCAR MINHAS RESERVAS ---");
        //recebe o id do usuario pelo scanner
        int idResponsavel = lerInteiro("Para ver suas reservas, por favor, digite seu ID: ");

        //consulta a lista de id's existentes
        Pessoa pessoa = banco.getPessoas().buscaId(idResponsavel);
        if (pessoa == null) {
            System.out.println("Erro: Pessoa com ID " + idResponsavel + " não encontrada.");
            return;
        }

        List<Reserva> reservasDaPessoa = pessoa.getMinhasReservas();

        //caso do usuario nao ter reserva no seu id
        if (reservasDaPessoa.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada para " + pessoa.getNome() + ".");
        } else {
            // imprime todas reservas que estao no id do usuario
            System.out.println("\n--- Reservas de " + pessoa.getNome() + " ---");
            for (Reserva reserva : reservasDaPessoa) {
                System.out.println(reserva.toString());
                System.out.println("--------------------");
            }
        }
    }

    //alexandre aqui to mudando a logica da funcao buscarreservas por pessoas tmb

        //**************** 

    //alexandre aqui to mudando a logica da funcao cancelarreserva tmb

    //acessa o banco de dados e exclui umaa reserva feita pelo usuario
    private static void cancelarReserva() {
        System.out.println("\n--- CANCELAR RESERVA ---");
        //recebe qual sala vai ser cancelada
        int idResponsavel = lerInteiro("Para cancelar uma reserva, primeiro identifique-se. Qual o seu ID? ");

        //erro se id der errado
        Pessoa pessoa = banco.getPessoas().buscaId(idResponsavel);
        if (pessoa == null) {
            System.out.println("Erro: Pessoa com ID " + idResponsavel + " não encontrada.");
            return;
        }

        List<Reserva> reservasDaPessoa = pessoa.getMinhasReservas();

        //caso id nao tenha feito reserva
        if (reservasDaPessoa.isEmpty()) {
            System.out.println("Você não possui nenhuma reserva para cancelar.");
            return;
        }

        //puxa as reservas com bse no id
        System.out.println("\n--- Suas Reservas Atuais ---");
        for (Reserva r : reservasDaPessoa){
            System.out.println("Reserva ID: " + r.getId() + " | Itens na reserva: " + r.getItensDaReserva().size());
        }

        int idReserva = lerInteiro("Digite o ID da reserva que deseja cancelar: ");

        //confere se o id do usuario é compativel com quem fez a reserva e se ela existe
        Reserva reservaParaCancelar = banco.getReservas().buscaId(idReserva);
        if (reservaParaCancelar == null || reservaParaCancelar.getResponsavel().getId() != idResponsavel){
            System.out.println("Erro: Reserva não encontrada ou não pertence a você.");
            return;
        }


        if(banco.getReservas().excluir(idReserva)){
            // IMPORTANTE: Remove a reserva da lista da pessoa também
            // (Você precisa criar este método 'removerReserva' em Pessoa.java)
            pessoa.removerReserva(reservaParaCancelar);
            
            System.out.println("Reserva cancelada com sucesso!");
        }else{
            System.out.println("Erro: Não foi possível cancelar a reserva.");
        }
    }

    //alexandre aqui to mudando a logica da funcao cancelarreserva tmb

    // ... (buscarSalasDisponiveis e salaEstaOcupada continuam iguais, apenas usando buscaId implicitamente via outra lógica)
    //opcao8 menu
    private static void buscarSalasDisponiveis() {
        System.out.println("\n--- BUSCAR SALAS DISPONIVEIS POR PERIODO ---");
        //le a entrada de dados ja convertendo com a fucnao nativa para saber se no horario especificado vai ter salas
        LocalDateTime dataHoraInicio = lerDataHora("Data e hora de início (dd/MM/yyyy HH:mm): ");
        LocalDateTime dataHoraFim = lerDataHora("Data e hora de fim (dd/MM/yyyy HH:mm): ");
        //metodo anti batata do usuario
        if (dataHoraFim.isBefore(dataHoraInicio)) {
            System.out.println("Data/hora do fim deve ser posterior à data/hora de início!");
            return;
        }

        //se tudo der certo retorna dizendo quais salas estao disponiveis no periodo informado
        System.out.println("\nSalas disponíveis no período:");
        List<Sala> todasSalas = banco.getSalas().listarTodos();
        boolean encontrouDisponivel = false;

        for (Sala sala : todasSalas) {
            if (!salaEstaOcupada(sala, dataHoraInicio, dataHoraFim)) {
                //imprime qual a sala e as informacoes dela se ela for disponivel
                System.out.println(sala);
                //modifica o metodo para informar que ao menos uma sala foi encontrada
                encontrouDisponivel = true;
            }
        }

        //se o metodo encontroudisponivel() retornar falso informa que nao existem salas disponiveis
        if (!encontrouDisponivel) {
            System.out.println("Nenhuma sala disponível no período selecionado.");
        }
    }

    // Nova função para criar sala
    private static void criarSala() {
        System.out.println("\n--- CRIAR NOVA SALA ---");

        // Ler prédio
        System.out.print("Nome do prédio (ex: CCOMP, ZOOTEC, ARQUIT): ");
        String predio = scanner.nextLine().trim();
        if (predio.isEmpty()) {
            System.out.println("Erro: Nome do prédio não pode estar vazio!");
            return;
        }

        // Ler capacidade
        int capacidade = lerInteiro("Capacidade de pessoas: ");
        if (capacidade <= 0) {
            System.out.println("Erro: Capacidade deve ser maior que zero!");
            return;
        }

        // Ler recursos disponíveis
        System.out.println("\n--- RECURSOS DA SALA ---");
        boolean temQuadro = lerSimNao("A sala tem quadro? (s/n): ");
        boolean temProjetor = lerSimNao("A sala tem projetor? (s/n): ");
        boolean temComputador = lerSimNao("A sala tem computador? (s/n): ");
        boolean temArCondicionado = lerSimNao("A sala tem ar-condicionado? (s/n): ");

        // Confirmar criação
        System.out.println("\n--- RESUMO DA SALA ---");
        System.out.println("Prédio: " + predio);
        System.out.println("Capacidade: " + capacidade + " pessoas");
        System.out.println("Quadro: " + (temQuadro ? "Sim" : "Não"));
        System.out.println("Projetor: " + (temProjetor ? "Sim" : "Não"));
        System.out.println("Computador: " + (temComputador ? "Sim" : "Não"));
        System.out.println("Ar-Condicionado: " + (temArCondicionado ? "Sim" : "Não"));

        boolean confirmar = lerSimNao("\nConfirmar criação desta sala? (s/n): ");

        if (confirmar) {
            Sala novaSala = new Sala(predio, capacidade, temQuadro, temProjetor, temComputador, temArCondicionado);
            banco.getSalas().inserir(novaSala);
            System.out.println("Sala criada com sucesso! ID: " + novaSala.getId());
        } else {
            System.out.println("Criação da sala cancelada.");
        }
    }


    //alexandre aqui to mudando a logica da funcao salaestaocupada tmb

    //verifica se existe conflito de horarios entre uma nova reserva e outra ja existente
    private static boolean salaEstaOcupada(Sala sala, LocalDateTime inicioDesejado, LocalDateTime fimDesejado) {
        // le toda a lista de reservas do banco de dados e verifica se alguma vai dar conflito
        for (Reserva reservaFeita : banco.getReservas().listarTodos()){
            // le o atributo individualmente (de cada ItemReserva)
            for (ItemReserva itemAgendado : reservaFeita.getItensDaReserva()){
                if (itemAgendado.getSala().getId() == sala.getId()) {
                    LocalDateTime inicioAgendado = itemAgendado.getDataHoraInicio();
                    LocalDateTime fimAgendado = itemAgendado.getDataHoraFim();

                    // verifica se há sobreposição de horários
                    if (inicioDesejado.isBefore(fimAgendado) && fimDesejado.isAfter(inicioAgendado)) {
                        return true; //conflito encontrado
                    }
                }
            }
        }
        return false; // nenhum conflito encontrado para esta sala.
    }


    //alexandre aqui to mudando a logica da funcao salaestaocupada tmb

    private static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            } else if (resposta.equals("n") || resposta.equals("não") || resposta.equals("nao")) {
                return false;
            } else {
                System.out.println("Erro: Digite 's' para sim ou 'n' para não!");
            }
        }
    }
    //*******************************************

    // (lerInteiro e lerDataHora continuam iguais)
    // constantemente fica recebendo que opcao o usuario quer acessar
    private static int lerInteiro(String mensagem) {
        while (true) {
            //tenta ler a entrada e se der erro ele puxa o catch dizendo pra tentar novamente
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um número válido!");
            }
        }
    }

    //funcao nativa java que lê entrada do usuario (string) e converte em horário
    private static LocalDateTime lerDataHora(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim();
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Erro: Formato inválido! Use dd/MM/yyyy HH:mm (ex: 01/01/2025 12:30)");
            }
        }
    }
}
//package sistemareserva.visao;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.stream.Collectors; // precisa desse import pro novo filtro de atributos
//
////fvcbdsbcv djxhb
//
//import sistemareserva.modelo.*;
//import sistemareserva.persistencia.*;
//
//public class Programa {
//    private static BancoDeDados banco = new BancoDeDados();
//    private static Scanner scanner = new Scanner(System.in);
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//
//    public static void main(String[] args) {
//        carregarDadosIniciais();
//        System.out.println("\n=== SISTEMA DE RESERVA DE SALAS ===");
//        System.out.println("Bem-vindo ao sistema de gerenciamento de reservas!\n");
//        // 1
//        int opcao;
//        do {
//            exibirMenuPrincipal();
//            opcao = lerInteiro("Escolha uma opção: ");
//
//            switch (opcao) {
//                case 1:
//                    menuGerenciarPessoas();
//                    break;
//                case 2:
//                    menuGerenciarSalas();
//                    break;
//                case 3:
//                    menuGerenciarReservas();
//                    break;
//                case 0:
//                    System.out.println("Saindo do sistema...");
//                    break;
//                default:
//                    System.out.println("Opção inválida!");
//            }
//            System.out.println();
//        } while (opcao != 0);
//
//        //fecha a leitura de dados
//        scanner.close();
//    }
//
//    private static void carregarDadosIniciais() {
//        System.out.println("Inicializando sistema e carregando dados...");
//        banco.getPessoas().inserir(new Pessoa("Professor Girafales"));
//        banco.getPessoas().inserir(new Pessoa("Dona Florinda"));
//        banco.getPessoas().inserir(new Pessoa("Seu Madruga"));
//        banco.getPessoas().inserir(new Pessoa("Professor Matheus"));
//
//        banco.getSalas().inserir(new Sala("CCOMP", 50, true, true, false, true));
//        banco.getSalas().inserir(new Sala("CCOMP", 30, true, false, false, true));
//        banco.getSalas().inserir(new Sala("CCOMP", 100, true, true, true, true));
//        banco.getSalas().inserir(new Sala("CCOMP", 10, true, false, false, false));
//        banco.getSalas().inserir(new Sala("ZOOTEC", 25, false, false, false, false));
//        banco.getSalas().inserir(new Sala("ARQUIT", 40, true, true, false, true));
//
//
//    }
//
//    // imprime o menu principal
//
//    //alexandre aqui atualizei o menu principal, a partir daqui vai pro sub menu específico
//    private static void exibirMenuPrincipal() {
//        System.out.println("===== MENU PRINCIPAL =====");
//        System.out.println("1 - Gerenciar Pessoas");
//        System.out.println("2 - Gerenciar Salas");
//        System.out.println("3 - Gerenciar Reservas");
//        System.out.println("0 - Sair");
//        System.out.println("===========================");
//    }
//
//    //alexandre aqui atualizei o menu principal, a partir daqui vai pro sub menu específico
//
//
//
//
//    //alexandre sub menus especificos
//
//
//    private static void menuGerenciarPessoas(){
//        int opcao;
//        do{
//            System.out.println("\n--- GERENCIAR PESSOAS ---");
//            System.out.println("1 - Inserir Pessoa (Cadastrar)");
//            System.out.println("2 - Alterar Pessoa");
//            System.out.println("3 - Excluir Pessoa");
//            System.out.println("4 - Buscar Pessoa por ID");
//            System.out.println("5 - Listar Todas as Pessoas");
//            System.out.println("0 - Voltar ao Menu Principal");
//
//            opcao = lerInteiro("Escolha uma opção: ");
//            switch(opcao) {
//                case 1: cadastrarPessoa();
//                    break;
//                case 2: alterarPessoa();
//                    break;
//                case 3: excluirPessoa();
//                    break;
//                case 4: buscarPessoaPorId();
//                    break;
//                case 5: listarPessoas();
//                    break;
//                case 0: System.out.println("Voltando...");
//                    break;
//                default: System.out.println("Opção inválida!");
//            }
//        }while (opcao != 0);
//    }
//
//
//    private static void menuGerenciarSalas(){
//        int opcao;
//        do {
//            System.out.println("\n--- GERENCIAR SALAS ---");
//            System.out.println("1 - Inserir Sala (Cadastrar)");
//            System.out.println("2 - Alterar Sala");
//            System.out.println("3 - Excluir Sala");
//            System.out.println("4 - Buscar Sala por ID");
//            System.out.println("5 - Listar Todas as Salas");
//            System.out.println("0 - Voltar ao Menu Principal");
//
//            opcao = lerInteiro("Escolha uma opção: ");
//            switch(opcao) {
//                case 1: criarSala();
//                    break;
//                case 2: alterarSala();
//                    break;
//                case 3: excluirSala();
//                    break;
//                case 4: buscarSalaPorId();
//                    break;
//                case 5: listarSalas();
//                    break;
//                case 0: System.out.println("Voltando...");
//                    break;
//                default: System.out.println("Opção inválida!");
//            }
//        } while (opcao != 0);
//    }
//
//
//    private static void menuGerenciarReservas(){
//        int opcao;
//        do {
//            System.out.println("\n--- GERENCIAR RESERVAS ---");
//            System.out.println("1 - Inserir Reserva (Criar)");
//            System.out.println("2 - Alterar Reserva (Adicionar/Remover Itens)");
//            System.out.println("3 - Excluir Reserva ");
//            System.out.println("4 - Buscar Reserva por ID");
//            System.out.println("5 - Listar Todas as Reservas ");
//            System.out.println("6 - Buscar Minhas Reservas");
//            System.out.println("0 - Voltar ao Menu Principal");
//
//            opcao = lerInteiro("Escolha uma opção: ");
//            switch(opcao) {
//                case 1: criarReserva();
//                    break;
//                case 2: alterarReserva();
//                    break;
//                case 3: cancelarReserva();
//                    break;
//                case 4: buscarReservaPorId();
//                    break;
//                case 5: listarTodasReservas();
//                    break;
//                case 6: buscarReservasPorPessoa();
//                    break;
//                case 0: System.out.println("Voltando...");
//                    break;
//                default: System.out.println("Opção inválida!");
//            }
//        } while (opcao != 0);
//    }
//
//    //alexandre sub menus especificos
//
//
//    //cadastra uma pessoa nova com base no persistencia.bancodedados
//    private static void cadastrarPessoa() {
//        System.out.println("\n--- CADASTRAR PESSOA ---");
//        System.out.print("Nome da pessoa: ");
//        String nome = scanner.nextLine().trim();
//        //checagem anti batata
//        if (nome.isEmpty()) {
//            System.out.println("Erro: Nome não pode estar vazio!");
//            return;
//        }
//        Pessoa pessoa = new Pessoa(nome);
//        //puxa o banco de dados pra iniciar uma pessoa
//        banco.getPessoas().inserir(pessoa);
//        //diz que cadastrou com sucesso e puxa o id da pessoa do modelo.entidade
//        System.out.println("Pessoa cadastrada com sucesso! ID: " + pessoa.getId());
//    }
//
//    //alexandre aqui vou ter que mudar toda a logica de criar reserva, por causa da mudança que o matheus pediu
//
//    //*************************************************************
//
//    //alexandre metodos novos que nao tinham antes, necessarios para novas funcoes e menus
//
//    private static void excluirPessoa(){
//        System.out.println("\n--- EXCLUIR PESSOA ---");
//        listarPessoas(); // Mostra as pessoas para o usuário saber o ID
//        int id = lerInteiro("Digite o ID da pessoa para excluir: ");
//
//        //alexandre observação: talvez seja interessante a gente verificar se a pessoa tem reservas antes de cancelar
//        if (banco.getPessoas().excluir(id)){
//            System.out.println("Pessoa excluída com sucesso!");
//        }else{
//            System.out.println("Erro: Pessoa não encontrada.");
//        }
//    }
//
//    private static void alterarPessoa() {
//        System.out.println("\n--- ALTERAR PESSOA ---");
//        listarPessoas();
//        int id = lerInteiro("Digite o ID da pessoa para alterar: ");
//        Pessoa pessoa = banco.getPessoas().buscaId(id);
//        if (pessoa == null) {
//            System.out.println("Erro: Pessoa não encontrada.");
//            return;
//        }
//        System.out.print("Digite o novo nome para '" + pessoa.getNome() + "': ");
//        String novoNome = scanner.nextLine().trim();
//        if (novoNome.isEmpty()) {
//            System.out.println("Nome não pode ser vazio. Alteração cancelada.");
//            return;
//        }
//        pessoa.setNome(novoNome);
//        if (banco.getPessoas().alterar(pessoa)) {
//            System.out.println("Pessoa alterada com sucesso!");
//        } else {
//            System.out.println("Erro ao alterar pessoa.");
//        }
//    }
//
//    private static void buscarPessoaPorId(){
//        System.out.println("\n--- BUSCAR PESSOA POR ID ---");
//        int id = lerInteiro("Digite o ID da pessoa: ");
//        Pessoa pessoa = banco.getPessoas().buscaId(id);
//        if(pessoa == null){
//            System.out.println("Erro: Pessoa não encontrada.");
//        }else{
//            System.out.println("Encontrado: " + pessoa.toString());
//        }
//    }
//
//    //*************************************************************
//
//    private static void alterarSala(){
//        System.out.println("\n--- ALTERAR SALA ---");
//        listarSalas();
//        int id = lerInteiro("Digite o ID da sala para alterar: ");
//        Sala sala = banco.getSalas().buscaId(id);
//        if (sala == null) {
//            System.out.println("Erro: Sala não encontrada.");
//            return;
//        }
//        System.out.println("Alterando Sala: " + sala.toString());
//        System.out.println("Deixe em branco ou digite 0 para manter a informação atual.");
//
//        System.out.print("Novo prédio (" + sala.getPredio() + "): ");
//        String predio = scanner.nextLine().trim();
//        if (!predio.isEmpty()) {
//            sala.setPredio(predio);
//        }
//
//        int capacidade = lerInteiro("Nova capacidade (" + sala.getCapacidade() + "): ");
//        if (capacidade > 0) {
//            sala.setCapacidade(capacidade);
//        }
//
//        //talvez seja interessante colocar mais uma logica pra alterar os valores booleanos(dos itens nas salas) depois, é algo que o matheus pode encher o saco
//
//        if (banco.getSalas().alterar(sala)) {
//            System.out.println("Sala alterada com sucesso!");
//        } else {
//            System.out.println("Erro ao alterar sala.");
//        }
//    }
//
//
//    private static void excluirSala(){
//        System.out.println("\n--- EXCLUIR SALA ---");
//        listarSalas();
//        int id = lerInteiro("Digite o ID da sala para excluir: ");
//        // alexandre talvez a gente devsse checar se a sala tem reservas antes de excluir, mesmo caso da pessoa
//        if(banco.getSalas().excluir(id)){
//            System.out.println("Sala excluída com sucesso!");
//        }else{
//            System.out.println("Erro: Sala não encontrada.");
//        }
//    }
//
//
//    private static void buscarSalaPorId(){
//        System.out.println("\n--- BUSCAR SALA POR ID ---");
//        int id = lerInteiro("Digite o ID da sala: ");
//        Sala sala = banco.getSalas().buscaId(id);
//        if (sala == null) {
//            System.out.println("Erro: Sala não encontrada.");
//        } else {
//            System.out.println("Encontrado: " + sala.toString());
//        }
//    }
//
//
//    private static void alterarReserva(){
//        System.out.println("\n--- ALTERAR RESERVA ---");
//        int idResponsavel = lerInteiro("Primeiro, identifique-se. Qual o seu ID? ");
//        Pessoa pessoa = banco.getPessoas().buscaId(idResponsavel);
//        if (pessoa == null) {
//            System.out.println("Erro: Pessoa não encontrada.");
//            return;
//        }
//
//        List<Reserva> reservasDaPessoa = pessoa.getMinhasReservas();
//        if (reservasDaPessoa.isEmpty()) {
//            System.out.println("Você não possui nenhuma reserva para alterar.");
//            return;
//        }
//
//        System.out.println("\n--- Suas Reservas Atuais ---");
//        for (Reserva r : reservasDaPessoa) {
//            System.out.println("Reserva ID: " + r.getId() + " | Itens: " + r.getItensDaReserva().size());
//        }
//
//        int idReserva = lerInteiro("Digite o ID da reserva que deseja alterar: ");
//        Reserva reservaParaAlterar = banco.getReservas().buscaId(idReserva);
//
//        if (reservaParaAlterar == null || reservaParaAlterar.getResponsavel().getId() != idResponsavel) {
//            System.out.println("Erro: Reserva não encontrada ou não pertence a você.");
//            return;
//        }
//
//        System.out.println("Alterando Reserva ID: " + reservaParaAlterar.getId());
//        System.out.println(reservaParaAlterar.toString());
//
//        int opcao;
//        do {
//            System.out.println("\n1 - Adicionar Sala");
//            System.out.println("2 - Remover Sala");
//            System.out.println("0 - Concluir Alteração");
//            opcao = lerInteiro("Escolha uma opção: ");
//
//            if (opcao == 1) {
//                System.out.println("ADICIONANDO NOVA SALA...");
//                listarSalas();
//                int idSala = lerInteiro("Digite o ID da sala para adicionar: ");
//                Sala salaEscolhida = banco.getSalas().buscaId(idSala);
//                if (salaEscolhida == null) {
//                    System.out.println("Erro: Sala não encontrada.");
//                    continue;
//                }
//                LocalDateTime inicio = lerDataHora("  Data e hora de início: ");
//                LocalDateTime fim = lerDataHora("  Data e hora de fim: ");
//                if (!salaEstaOcupada(salaEscolhida, inicio, fim)) {
//                    ItemReserva item = new ItemReserva(salaEscolhida, inicio, fim);
//                    reservaParaAlterar.adicionarItem(item);
//                    System.out.println("Sala adicionada!");
//                } else {
//                    System.out.println("Erro: Sala ocupada nesse período.");
//                }
//            }else if(opcao == 2){
//                System.out.println("REMOVENDO SALA...");
//                List<ItemReserva> itens = reservaParaAlterar.getItensDaReserva();
//                if (itens.isEmpty()) {
//                    System.out.println("Reserva não possui itens para remover.");
//                    continue;
//                }
//
//                for (int i = 0; i < itens.size(); i++) {
//                    System.out.println((i+1) + " - " + itens.get(i).toString());
//                }
//                int indiceRemover = lerInteiro("Digite o número do item a remover (ex: 1): ");
//                if (indiceRemover > 0 && indiceRemover <= itens.size()) {
//                    ItemReserva itemRemovido = itens.remove(indiceRemover - 1);
//                    System.out.println("Item removido: Sala ID " + itemRemovido.getSala().getId());
//                } else {
//                    System.out.println("Índice inválido.");
//                }
//            }
//        } while (opcao != 0);
//
//        banco.getReservas().alterar(reservaParaAlterar);
//        System.out.println("Reserva alterada com sucesso!");
//        System.out.println(reservaParaAlterar.toString());
//    }
//
//    private static void buscarReservaPorId(){
//        System.out.println("\n--- BUSCAR RESERVA POR ID ---");
//        int id = lerInteiro("Digite o ID da reserva: ");
//        Reserva reserva = banco.getReservas().buscaId(id);
//        if (reserva == null) {
//            System.out.println("Erro: Reserva não encontrada.");
//        } else {
//            System.out.println("Encontrado: \n" + reserva.toString());
//        }
//    }
//
//
//    private static void listarTodasReservas(){
//        System.out.println("\n--- LISTAR TODAS AS RESERVAS (ADMIN) ---");
//        // Usamos o toString() do Persistente, como o professor pediu.
//        System.out.println(banco.getReservas().toString());
//    }
//
//
//
//    //*****************************************************************
//
//
//
//
//    //alexandre metodos novos que nao tinham antes, necessarios para novas funcoes e menus
//
//
//
//    private static void criarReserva() {
//        System.out.println("\n--- CRIAR RESERVA ---");
//
//        // a logica mudou, agr a gente pede o responsavel primeiro
//        int idResponsavel =lerInteiro("Digite o SEU ID para a reserva (ou digite 0 para cancelar): ");
//        if (idResponsavel == 0){
//            System.out.println("Operação de reserva cancelada pelo usuário.");
//            return;
//        }
//        Pessoa responsavel =banco.getPessoas().buscaId(idResponsavel);
//
//        if (responsavel==null){
//            System.out.println("Erro: Pessoa não encontrada!");
//            return;
//        }
//
//        // criar reserva (agora só com o responsável)
//        Reserva reserva = new Reserva(responsavel);
//
//        // adicionar salas à reserva
//        boolean adicionandoSalas = true;
//        while (adicionandoSalas) {
//            System.out.println("\n--- Adicionando Sala à Reserva (Responsável: " + responsavel.getNome() + ") ---");
//            listarSalas(); // Mostra as salas cadastradas
//
//            //se digitar 0 ele sai do metodo
//            int idSala = lerInteiro("Digite o ID da sala para adicionar à reserva (0 para concluir): ");
//
//            if (idSala == 0) {
//                //checa se existe uma sala acessando um objeto dela que no caso é o objeto item
//                if (reserva.getItensDaReserva().isEmpty()) {
//                    System.out.println("Erro: A reserva deve ter pelo menos uma sala!");
//                    continue; // Volta ao início do loop
//                }
//                //interrompe a variavel para parar de adicionar
//                adicionandoSalas = false;
//            } else {
//                //vai adicionando salas e criando
//                Sala salaEscolhida = banco.getSalas().buscaId(idSala);
//                if (salaEscolhida == null) {
//                    System.out.println("Erro: Sala com ID " + idSala + " não encontrada.");
//                    continue; // Volta ao início do loop
//                }
//
//                // pede a data e hora pra esse item especifico
//                System.out.println("Defina o período para a Sala ID " + salaEscolhida.getId() + ":");
//                // ler data e hora com funcao nativa java
//                LocalDateTime dataHoraInicio = lerDataHora("  Data e hora de início (dd/MM/yyyy HH:mm): ");
//                LocalDateTime dataHoraFim = lerDataHora("  Data e hora de fim (dd/MM/yyyy HH:mm): ");
//
//                //checagem anti batata do usuario
//                if (dataHoraFim.isBefore(dataHoraInicio) || dataHoraFim.isEqual(dataHoraInicio)) {
//                    System.out.println("Erro: Data/hora de fim deve ser posterior à de início!");
//                    continue; // Volta ao início do loop
//                }
//
//                // verifica o conflito pra essa sala pra esse hoario
//                if (salaEstaOcupada(salaEscolhida, dataHoraInicio, dataHoraFim)) {
//                    System.out.println("ERRO: A sala " + salaEscolhida.getId() + " já está ocupada nesse período!");
//                    System.out.println("Por favor, escolha outra sala ou outro horário.");
//                } else {
//                    //confere se a sala existe e reserva ela
//                    ItemReserva item = new ItemReserva(salaEscolhida, dataHoraInicio, dataHoraFim);
//                    reserva.adicionarItem(item);
//                    System.out.println(">>> Sala '" + salaEscolhida.getId() + "' adicionada com sucesso! <<<");
//                }
//            }
//        }
//
//        // registra aa reserva no banco de dados
//        banco.getReservas().inserir(reserva);
//        // adiciona a reserva na lista da pessoa
//        responsavel.adicionarReserva(reserva);
//
//        System.out.println("Reserva criada com sucesso! ID: " + reserva.getId());
//        //mostra o resumo da reserva usando o novo toString() de Reserva
//        System.out.println(reserva.toString());
//    }
//
//     //alexandre aqui vou ter que mudar toda a logica de criar reserva, por causa da mudança que o matheus pediu
//
//
//
//    //exibe as pessoas ja criadas
//    private static void listarPessoas() {
//        System.out.println("\n--- LISTA DE TODAS AS PESSOAS CADASTRADAS ---");
//        //acessa o banco de dados e percorre a lista de pessoas criadas
//        List<Pessoa> pessoas = banco.getPessoas().listarTodos();
//        //isEmpty é outra forma (nativa do java) de saber se um array esta vazio
//        if (pessoas.isEmpty()) {
//            System.out.println("Nenhuma pessoa cadastrada.");
//        } else {
//            System.out.println("Total de pessoas cadastradas: " + pessoas.size());
//            System.out.println("----------------------------------------------");
//            for (Pessoa pessoa : pessoas) {
//                System.out.println("ID: " + pessoa.getId() + " | Nome: " + pessoa.getNome());
//            }
//            System.out.println("----------------------------------------------");
//        }
//    }
//
//    private static void listarSalas() {
//        System.out.println("\n--- LISTA DE TODAS AS SALAS ---");
//        List<Sala> salas = banco.getSalas().listarTodos();
//
//        if (salas.isEmpty()) {
//            System.out.println("Nenhuma sala cadastrada.");
//        } else {
//            for (Sala sala : salas) {
//                System.out.println(sala);
//            }
//        }
//    }
//
//    //alexandre aqui to mudando a logica da funcao buscarreservas por pessoas tmb
//
//
//    private static void buscarReservasPorPessoa() {
//        System.out.println("\n--- BUSCAR MINHAS RESERVAS ---");
//        //recebe o id do usuario pelo scanner
//        int idResponsavel = lerInteiro("Para ver suas reservas, por favor, digite seu ID: ");
//
//        //consulta a lista de id's existentes
//        Pessoa pessoa = banco.getPessoas().buscaId(idResponsavel);
//        if (pessoa == null) {
//            System.out.println("Erro: Pessoa com ID " + idResponsavel + " não encontrada.");
//            return;
//        }
//
//        List<Reserva> reservasDaPessoa = pessoa.getMinhasReservas();
//
//        //caso do usuario nao ter reserva no seu id
//        if (reservasDaPessoa.isEmpty()) {
//            System.out.println("Nenhuma reserva encontrada para " + pessoa.getNome() + ".");
//        } else {
//            // imprime todas reservas que estao no id do usuario
//            System.out.println("\n--- Reservas de " + pessoa.getNome() + " ---");
//            for (Reserva reserva : reservasDaPessoa) {
//                System.out.println(reserva.toString());
//                System.out.println("--------------------");
//            }
//        }
//    }
//
//    //alexandre aqui to mudando a logica da funcao buscarreservas por pessoas tmb
//
//        //****************
//
//    //alexandre aqui to mudando a logica da funcao cancelarreserva tmb
//
//    //acessa o banco de dados e exclui umaa reserva feita pelo usuario
//    private static void cancelarReserva() {
//        System.out.println("\n--- CANCELAR RESERVA ---");
//        //recebe qual sala vai ser cancelada
//        int idResponsavel = lerInteiro("Para cancelar uma reserva, primeiro identifique-se. Qual o seu ID? ");
//
//        //erro se id der errado
//        Pessoa pessoa = banco.getPessoas().buscaId(idResponsavel);
//        if (pessoa == null) {
//            System.out.println("Erro: Pessoa com ID " + idResponsavel + " não encontrada.");
//            return;
//        }
//
//        List<Reserva> reservasDaPessoa = pessoa.getMinhasReservas();
//
//        //caso id nao tenha feito reserva
//        if (reservasDaPessoa.isEmpty()) {
//            System.out.println("Você não possui nenhuma reserva para cancelar.");
//            return;
//        }
//
//        //puxa as reservas com bse no id
//        System.out.println("\n--- Suas Reservas Atuais ---");
//        for (Reserva r : reservasDaPessoa){
//            System.out.println("Reserva ID: " + r.getId() + " | Itens na reserva: " + r.getItensDaReserva().size());
//        }
//
//        int idReserva = lerInteiro("Digite o ID da reserva que deseja cancelar: ");
//
//        //confere se o id do usuario é compativel com quem fez a reserva e se ela existe
//        Reserva reservaParaCancelar = banco.getReservas().buscaId(idReserva);
//        if (reservaParaCancelar == null || reservaParaCancelar.getResponsavel().getId() != idResponsavel){
//            System.out.println("Erro: Reserva não encontrada ou não pertence a você.");
//            return;
//        }
//
//
//        if(banco.getReservas().excluir(idReserva)){
//            // aqui ela remove a reserva da lista da pessoa também
//            pessoa.removerReserva(reservaParaCancelar);
//
//            System.out.println("Reserva cancelada com sucesso!");
//        }else{
//            System.out.println("Erro: Não foi possível cancelar a reserva.");
//        }
//    }
//
//    //alexandre aqui to mudando a logica da funcao cancelarreserva tmb
//
//    //opcao8 menu
//    private static void buscarSalasDisponiveis() {
//        System.out.println("\n--- BUSCAR SALAS DISPONIVEIS POR PERIODO ---");
//        //le a entrada de dados ja convertendo com a fucnao nativa para saber se no horario especificado vai ter salas
//        LocalDateTime dataHoraInicio = lerDataHora("Data e hora de início (dd/MM/yyyy HH:mm): ");
//        LocalDateTime dataHoraFim = lerDataHora("Data e hora de fim (dd/MM/yyyy HH:mm): ");
//        //metodo anti batata do usuario
//        if (dataHoraFim.isBefore(dataHoraInicio)) {
//            System.out.println("Data/hora do fim deve ser posterior à data/hora de início!");
//            return;
//        }
//
//        //se tudo der certo retorna dizendo quais salas estao disponiveis no periodo informado
//        System.out.println("\nSalas disponíveis no período:");
//        List<Sala> todasSalas = banco.getSalas().listarTodos();
//        boolean encontrouDisponivel = false;
//
//        for (Sala sala : todasSalas) {
//            if (!salaEstaOcupada(sala, dataHoraInicio, dataHoraFim)) {
//                //imprime qual a sala e as informacoes dela se ela for disponivel
//                System.out.println(sala);
//                //modifica o metodo para informar que ao menos uma sala foi encontrada
//                encontrouDisponivel = true;
//            }
//        }
//
//        //se o metodo encontroudisponivel() retornar falso informa que nao existem salas disponiveis
//        if (!encontrouDisponivel) {
//            System.out.println("Nenhuma sala disponível no período selecionado.");
//        }
//    }
//
//    private static void criarSala() {
//        System.out.println("\n--- CRIAR NOVA SALA ---");
//
//        System.out.print("Nome do prédio (ex: CCOMP, ZOOTEC, ARQUIT): ");
//        String predio = scanner.nextLine().trim();
//        if (predio.isEmpty()) {
//            System.out.println("Erro: Nome do prédio não pode estar vazio!");
//            return;
//        }
//
//        int capacidade = lerInteiro("Capacidade de pessoas: ");
//        if (capacidade <= 0) {
//            System.out.println("Erro: Capacidade deve ser maior que zero!");
//            return;
//        }
//
//        System.out.println("\n--- RECURSOS DA SALA ---");
//        boolean temQuadro = lerSimNao("A sala tem quadro? (s/n): ");
//        boolean temProjetor = lerSimNao("A sala tem projetor? (s/n): ");
//        boolean temComputador = lerSimNao("A sala tem computador? (s/n): ");
//        boolean temArCondicionado = lerSimNao("A sala tem ar-condicionado? (s/n): ");
//
//        System.out.println("\n--- RESUMO DA SALA ---");
//        System.out.println("Prédio: " + predio);
//        System.out.println("Capacidade: " + capacidade + " pessoas");
//        System.out.println("Quadro: " + (temQuadro ? "Sim" : "Não"));
//        System.out.println("Projetor: " + (temProjetor ? "Sim" : "Não"));
//        System.out.println("Computador: " + (temComputador ? "Sim" : "Não"));
//        System.out.println("Ar-Condicionado: " + (temArCondicionado ? "Sim" : "Não"));
//
//        boolean confirmar = lerSimNao("\nConfirmar criação desta sala? (s/n): ");
//
//        if (confirmar) {
//            Sala novaSala = new Sala(predio, capacidade, temQuadro, temProjetor, temComputador, temArCondicionado);
//            banco.getSalas().inserir(novaSala);
//            System.out.println("Sala criada com sucesso! ID: " + novaSala.getId());
//        } else {
//            System.out.println("Criação da sala cancelada.");
//        }
//    }
//
//
//    //alexandre aqui to mudando a logica da funcao salaestaocupada tmb
//
//    //verifica se existe conflito de horarios entre uma nova reserva e outra ja existente
//    private static boolean salaEstaOcupada(Sala sala, LocalDateTime inicioDesejado, LocalDateTime fimDesejado) {
//        // le toda a lista de reservas do banco de dados e verifica se alguma vai dar conflito
//        for (Reserva reservaFeita : banco.getReservas().listarTodos()){
//            // le o atributo individualmente (de cada ItemReserva)
//            for (ItemReserva itemAgendado : reservaFeita.getItensDaReserva()){
//                if (itemAgendado.getSala().getId() == sala.getId()) {
//                    LocalDateTime inicioAgendado = itemAgendado.getDataHoraInicio();
//                    LocalDateTime fimAgendado = itemAgendado.getDataHoraFim();
//
//                    // verifica se há sobreposição de horários
//                    if (inicioDesejado.isBefore(fimAgendado) && fimDesejado.isAfter(inicioAgendado)) {
//                        return true; //conflito encontrado
//                    }
//                }
//            }
//        }
//        return false; // nenhum conflito encontrado para esta sala.
//    }
//
//
//    //alexandre aqui to mudando a logica da funcao salaestaocupada tmb
//
//    private static boolean lerSimNao(String mensagem) {
//        while (true) {
//            System.out.print(mensagem);
//            String resposta = scanner.nextLine().trim().toLowerCase();
//            if (resposta.equals("s") || resposta.equals("sim")) {
//                return true;
//            } else if (resposta.equals("n") || resposta.equals("não") || resposta.equals("nao")) {
//                return false;
//            } else {
//                System.out.println("Erro: Digite 's' para sim ou 'n' para não!");
//            }
//        }
//    }
//    //*******************************************
//
//    // constantemente fica recebendo que opcao o usuario quer acessar
//    private static int lerInteiro(String mensagem) {
//        while (true) {
//            //tenta ler a entrada e se der erro ele puxa o catch dizendo pra tentar novamente
//            try {
//                System.out.print(mensagem);
//                return Integer.parseInt(scanner.nextLine());
//            } catch (NumberFormatException e) {
//                System.out.println("Erro: Digite um número válido!");
//            }
//        }
//    }
//
//    //funcao nativa java que lê entrada do usuario (string) e converte em horário
//    private static LocalDateTime lerDataHora(String mensagem) {
//        while (true) {
//            try {
//                System.out.print(mensagem);
//                String input = scanner.nextLine().trim();
//                return LocalDateTime.parse(input, formatter);
//            } catch (DateTimeParseException e) {
//                System.out.println("Erro: Formato inválido! Use dd/MM/yyyy HH:mm (ex: 01/01/2025 12:30)");
//            }
//        }
//    }
//}
package sistemareserva.visao;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; //manipula dados da tabela
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.text.ParseException;
import java.util.List;
//imports necessarios para datas e horas:
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import sistemareserva.modelo.Pessoa;
import sistemareserva.modelo.Sala;
import sistemareserva.modelo.Reserva;
import sistemareserva.modelo.ItemReserva;

public class ReservaGUI extends JFrame {

    private SistemaReservaService service;
    
    private JTable tabelaPessoas; 
    private DefaultTableModel tableModel; //alexandre mudei o jtextarea

    //novas variaveis 
    private JTable tabelaSalas;
    private DefaultTableModel tableModelSalas;

    private JTable tabelaReservas;
    private DefaultTableModel tableModelReservas;
    
    // alexandre formatarr para datas (ta meio errado acho que tem q ajustar isso dps)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private CardLayout cardLayout; 
    private JPanel mainPanel; 

    public ReservaGUI() {
        service = new SistemaReservaService(); 

        setTitle("Sistema de Reserva de Salas - SWING");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        configurarLayoutPrincipal();
        setVisible(true);
    }

    private void configurarLayoutPrincipal() {
        //alexandre inicia cardlayoout, consegue mostrar um painel por vez
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        //alexandre adicionei os paineis iniciais na tela, separados
        JPanel painelMenu = criarPainelMenuPrincipal();
        JPanel painelPessoas = criarPainelGerenciarPessoas();
        
        // alexandre criando e adicionando os painéis novos salas e reevervas
        JPanel painelSalas = criarPainelGerenciarSalas();
        JPanel painelReservas = criarPainelGerenciarReservas();
        
        mainPanel.add(painelMenu, "MENU");
        mainPanel.add(painelPessoas, "PESSOAS");
        mainPanel.add(painelSalas, "SALAS");
        mainPanel.add(painelReservas, "RESERVAS");

        add(mainPanel);
        
        cardLayout.show(mainPanel, "MENU"); //qual tela começa aparecendo
    }

    private JPanel criarPainelMenuPrincipal() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JLabel titulo = new JLabel("=== SISTEMA DE RESERVA DE SALAS ===", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painel.add(titulo, BorderLayout.NORTH);

        JPanel menuBotoes = new JPanel(new GridLayout(1, 3, 10, 10));
        menuBotoes.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Margem para ficar bonito

        JButton btnPessoas = new JButton("1 - Gerenciar Pessoas");
        JButton btnSalas = new JButton("2 - Gerenciar Salas");
        JButton btnReservas = new JButton("3 - Gerenciar Reservas");

        btnPessoas.addActionListener(e -> {
            cardLayout.show(mainPanel, "PESSOAS");
            listarTodasPessoas(); // atualiza tabela
        });
        
        // agr os botoes de reerva e salas funciomam msm
        btnSalas.addActionListener(e -> {
            cardLayout.show(mainPanel, "SALAS");
            listarTodasSalas();
        });
        
        btnReservas.addActionListener(e -> {
            cardLayout.show(mainPanel, "RESERVAS");
            listarTodasReservas();
        });

        menuBotoes.add(btnPessoas);
        menuBotoes.add(btnSalas);
        menuBotoes.add(btnReservas);

        painel.add(menuBotoes, BorderLayout.CENTER);
        return painel;
    }

    // alexandre metodo novo para gerenciar as pessoas
    private JPanel criarPainelGerenciarPessoas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //bootoes do topo
        JPanel botoesPanel = new JPanel(new GridLayout(1, 6, 5, 0));
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnBuscar = new JButton("Buscar ID");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnInfos = new JButton("Infos reserva");    //(Julia) consultar as reservas
        JButton btnVoltar = new JButton("Voltar");

        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAtualizar);
        botoesPanel.add(btnVoltar);

        btnCadastrar.addActionListener(e -> executarAcaoCadastrarPessoa());
        btnAlterar.addActionListener(e -> executarAcaoAlterarPessoa());
        btnExcluir.addActionListener(e -> executarAcaoExcluirPessoa());
        btnBuscar.addActionListener(e -> executarAcaoBuscarPessoa());
        btnAtualizar.addActionListener(e -> listarTodasPessoas());
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));

        String[] colunas = {"ID", "Nome"}; // cabeçalho da tabela
        // o 0 indica que começa sem linhas
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //dica de boa pratica do chat pra evitar bananagem
            }
        }; 
        tabelaPessoas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaPessoas);

        painel.add(botoesPanel, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }
    
    // alexandre painel de gerenciar salas, copiei do de pessoas e alterei
    private JPanel criarPainelGerenciarSalas(){
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //alexandre botoes
        JPanel botoesPanel = new JPanel(new GridLayout(1, 5, 5, 0));
        JButton btnCadastrar = new JButton("Nova Sala");
        JButton btnAlterar = new JButton("Alterar Sala"); // botao adicionado de alyera sala
        JButton btnExcluir = new JButton("Excluir Sala");
        JButton btnListar = new JButton("Atualizar Lista");
        JButton btnVoltar = new JButton("Voltar");
        //alexandre novo botao de buscar por id: 
        JButton btnBuscar = new JButton("Buscar ID");
        btnBuscar.addActionListener(e -> executarAcaoBuscarSala());

        botoesPanel.add(btnBuscar);

        btnCadastrar.addActionListener(e -> executarAcaoCadastrarSala());
        btnAlterar.addActionListener(e -> executarAcaoAlterarSala()); 
        btnExcluir.addActionListener(e -> executarAcaoExcluirSala());
        btnListar.addActionListener(e -> listarTodasSalas());
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU")); 
       

        //adicionando os botoes no painel
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnAlterar); 
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnListar);
        botoesPanel.add(btnVoltar);
        String[] colunas = {"ID", "Prédio", "Capacidade"};
        tableModelSalas =new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaSalas =new JTable(tableModelSalas);
        painel.add(botoesPanel, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaSalas), BorderLayout.CENTER);
        return painel;
    }

    // alexandre painel de gerenciar as reservas, copiei do de pessoas e alterei

    //FALTA ALGUNS METODOS AINDA, O DE ALTERAR, FALTA IMPLEMENTAR O DE CANCELAR, 
    //E TEM QUE AJUSTAR TAMBEM A PARA DE DATA E HORARIOS PRA FICAR MAIS FACIL DE DIGITAR, AZ VEZES O CHAT 
    //DA UMA IDEIA BOA DE FICAR MAIS FACIL PRO USUARIO, TIPO UM CAPO PRO DIA/MES/ANO E OUTRO PRA HORARIO, MAS ISSO É DETALHE TMB FDS
    //E FALTA TMB COLOCAR OS RECURSOS NA RESERVA, DE QUANTIDADE DE PESSOAS, E NECCESECIDADE DE RECURCSOS
    private JPanel criarPainelGerenciarReservas(){
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Aumentei o grid para 5 colunas
        JPanel botoesPanel = new JPanel(new GridLayout(1, 5, 5, 0));
        JButton btnNovaReserva = new JButton("Nova Reserva");
        JButton btnAlterar = new JButton("Alterar Reserva"); // Botao adicionado
        JButton btnListar = new JButton("Atualizar Lista");
        JButton btnExcluir = new JButton("Cancelar Reserva"); 
        JButton btnInfos = new JButton("Infos Reserva");    //botao extra
        JButton btnVoltar = new JButton("Voltar");
        //alexandre criando o botao de buscar por id:
        JButton btnBuscar = new JButton("Buscar ID");
        btnBuscar.addActionListener(e -> executarAcaoBuscarReserva());
        botoesPanel.add(btnBuscar);

        btnNovaReserva.addActionListener(e -> executarAcaoNovaReserva());
        btnAlterar.addActionListener(e -> executarAcaoAlterarReserva()); 
        btnListar.addActionListener(e -> listarTodasReservas());
        btnExcluir.addActionListener(e -> executarAcaoCancelarReserva()); 
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));

        //config botão de infos da reserva:
        btnInfos.addActionListener(e -> {
            int linha = tabelaReservas.getSelectedRow();
            if(linha == -1){
                JOptionPane.showMessageDialog(this, "Selecione uma reserva na tabela.");
                return;
            }

            int idReserva = Integer.parseInt(
                    tabelaReservas.getValueAt(linha, 0).toString()
            );
            Reserva r = service.buscaReservaPorId(idReserva);
            if(r == null){
                JOptionPane.showMessageDialog(this, "Reserva não encontrada.");
                return;
            }

            StringBuilder detalhes = new StringBuilder();
            detalhes.append("Reserva: ").append(r.getId()).append("\n");
            detalhes.append("Responsável: ").append(r.getResponsavel().getNome()).append("\n\n");
            detalhes.append("Salas reservadas:\n");

            for(ItemReserva item : r.getItensDaReserva()){
                detalhes.append("Sala ")
                        .append(item.getSala().getId())
                        .append(" — ")
                        .append(item.getDataHoraInicio().format(formatter))
                        .append(" até ")
                        .append(item.getDataHoraFim().format(formatter))
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, detalhes.toString(), 
                    "Detalhes da Reserva", JOptionPane.INFORMATION_MESSAGE);
        });

        
        botoesPanel.add(btnNovaReserva);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnListar);
        botoesPanel.add(btnInfos);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnVoltar);


        String[] colunas = {"ID Reserva", "Responsável"};//, "Sala", "Horário"};

        tableModelReservas = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaReservas = new JTable(tableModelReservas);
        painel.add(botoesPanel, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaReservas), BorderLayout.CENTER);
        return painel;
    }
    


    private void listarTodasPessoas() {

        tableModel.setRowCount(0); 
        List<Pessoa> pessoas = service.listarPessoas();

        if (pessoas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma pessoa cadastrada.");
        } else {
            for (Pessoa p : pessoas) {
                
                Object[] linha = {p.getId(), p.getNome()};
                tableModel.addRow(linha);
            }
        }
    }

    private void executarAcaoCadastrarPessoa() {
        String nome = JOptionPane.showInputDialog(this, "Digite o NOME da pessoa:", "Cadastrar", JOptionPane.QUESTION_MESSAGE);

        if (nome != null && !nome.trim().isEmpty()) {
            if (service.cadastrarPessoa(nome)) {
                JOptionPane.showMessageDialog(this, "Sucesso!");
                listarTodasPessoas(); 
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarAcaoExcluirPessoa() {
        //alexandre aqui vc pode agr clicar em cima do nome da pessoa e excluir, sem precisar digitar o id
        String idString = null;
        int linhaSelecionada = tabelaPessoas.getSelectedRow();
        
        if (linhaSelecionada != -1) {
            int idSelecionado = (int) tabelaPessoas.getValueAt(linhaSelecionada, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o ID " + idSelecionado + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                service.excluirPessoa(idSelecionado);
                listarTodasPessoas();
                return; 
            }
        }

        // se nao tiver selecionado ngm, ai deixa a caixinha que o marcus colocou mesmo, de colocar o id manualmente
        idString = JOptionPane.showInputDialog(this, "Digite o ID para EXCLUIR:");
        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                if (service.excluirPessoa(id)) {
                    JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: ID não encontrado ou com pendências.");
                }
                listarTodasPessoas();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    private void executarAcaoBuscarPessoa() {
        String idString = JOptionPane.showInputDialog(this, "ID para BUSCAR:");
        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Pessoa pessoa = service.buscarPessoaPorId(id);
                if (pessoa != null) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(
                        new Object[]{
                            pessoa.getId(), pessoa.getNome()
                        });
                } else {
                    JOptionPane.showMessageDialog(this, "Pessoa não encontrada.");
                    listarTodasPessoas();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    private void executarAcaoAlterarPessoa() {
        //alexandre: protecao pra caso a pessoa selecione mais de um item pra alterar
        int[] linhas = tabelaPessoas.getSelectedRows();

        if (linhas.length > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione apenas UMA linha para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        String idString = null;
        int linhaSelecionada = tabelaPessoas.getSelectedRow();
        
        if (linhaSelecionada != -1) {
            idString = String.valueOf(tabelaPessoas.getValueAt(linhaSelecionada, 0));
        } else {
            idString = JOptionPane.showInputDialog(this, "ID para ALTERAR:");
        }

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Pessoa pessoa = service.buscarPessoaPorId(id);

                if (pessoa != null) {
                    String novoNome = JOptionPane.showInputDialog(this, "Novo nome para '" + pessoa.getNome() + "':", pessoa.getNome());
                    if (novoNome != null && service.alterarPessoa(id, novoNome)) {
                        JOptionPane.showMessageDialog(this, "Alterado com sucesso!");
                        listarTodasPessoas();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "ID não encontrado.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    // metodos de logicas paras a ssalas:

    private void listarTodasSalas(){
        tableModelSalas.setRowCount(0);
        List<Sala> salas = service.listarSalas(); 
        if(salas != null && !salas.isEmpty()){
            for(Sala s : salas){
                tableModelSalas.addRow(new Object[]{s.getId(), s.getPredio(), s.getCapacidade()});
            }
        }else{
            //alexandre aqui pode colocar um aviso se a lista estiver vazia dps
        }
    }

    private void executarAcaoCadastrarSala() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); 
        JTextField txtPredio = new JTextField();
        JTextField txtCapacidade = new JTextField();
        
        
        panel.add(new JLabel("Prédio/Bloco:"));
        panel.add(txtPredio);
        panel.add(new JLabel("Capacidade:"));
        panel.add(txtCapacidade);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nova Sala", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String predio = txtPredio.getText();
                int capacidade = Integer.parseInt(txtCapacidade.getText());
                
                if (service.cadastrarSala(predio, capacidade)) {
                    JOptionPane.showMessageDialog(this, "Sala cadastrada com sucesso!");
                    listarTodasSalas();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar Sala.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: Capacidade deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // novo metodo adiocnado para alterar sala, antes nao tinha
    private void executarAcaoAlterarSala() {
        //alexandre: protecao pra caso a pessoa selecione mais de um item pra alterar
        int[] linhas = tabelaPessoas.getSelectedRows();

        if (linhas.length > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione apenas UMA linha para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        String idString = null;
        int linhaSelecionada = tabelaSalas.getSelectedRow();
        
        if (linhaSelecionada != -1) {
            idString = String.valueOf(tabelaSalas.getValueAt(linhaSelecionada, 0));
        } else {
            idString = JOptionPane.showInputDialog(this, "Digite o ID da Sala para ALTERAR:");
        }

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Sala salaAtual = service.buscaSalaPorId(id);

                if (salaAtual != null) {
                    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
                    JTextField txtPredio = new JTextField(salaAtual.getPredio());
                    JTextField txtCapacidade = new JTextField(String.valueOf(salaAtual.getCapacidade()));
                    
                    panel.add(new JLabel("Prédio/Bloco:"));
                    panel.add(txtPredio);
                    panel.add(new JLabel("Capacidade:"));
                    panel.add(txtCapacidade);

                    int result = JOptionPane.showConfirmDialog(this, panel, "Editar Sala ID " + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if(result == JOptionPane.OK_OPTION){
                        String predio = txtPredio.getText();
                        int capacidade = Integer.parseInt(txtCapacidade.getText());
                        
                        boolean sucesso = service.alterarSala(id, predio, capacidade);
                        if(sucesso){
                            JOptionPane.showMessageDialog(this, "Sala alterada com sucesso!");
                            listarTodasSalas();
                        } else {
                            JOptionPane.showMessageDialog(this, "Erro ao alterar sala.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Sala não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID ou Capacidade inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarAcaoExcluirSala(){
        int row = tabelaSalas.getSelectedRow();
        if(row != -1){
            int id =(int)tabelaSalas.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Excluir Sala ID " + id + "?") == JOptionPane.YES_OPTION) {
                service.excluirSala(id);
                listarTodasSalas();
            }
        }else{
             String idStr = JOptionPane.showInputDialog(this, "ID da Sala para excluir:");
             if(idStr != null){
                 try{
                     service.excluirSala(Integer.parseInt(idStr));
                     listarTodasSalas();
                 }catch(Exception e){
                     JOptionPane.showMessageDialog(this, "ID inválido.");
                 }
             }
        }
    }

    //alexandre, novo metodo de executar a acao de buscar a sala por id:
        private void executarAcaoBuscarSala(){
        String idString = JOptionPane.showInputDialog(this, "ID da Sala para BUSCAR:");
        if (idString != null){
            try{
                int id = Integer.parseInt(idString);
                Sala sala = service.buscaSalaPorId(id);
                if (sala != null){
                    tableModelSalas.setRowCount(0);
                    tableModelSalas.addRow(new Object[]{
                        sala.getId(), 
                        sala.getPredio(), 
                        sala.getCapacidade()
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Sala não encontrada.");
                    listarTodasSalas();
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    //alexandre metodos da logica para as funcoes da reserva
    private void listarTodasReservas(){
        tableModelReservas.setRowCount(0);
        List<Reserva> reservas = service.listarReservas();
        if(reservas != null){
            for(Reserva r : reservas){
                String infoSala = "Sem Sala";
                String infoData = "-";
                if(!r.getItensDaReserva().isEmpty()){
                    ItemReserva item = r.getItensDaReserva().get(0);
                    infoSala = "Sala " + item.getSala().getId();
                    infoData = item.getDataHoraInicio().format(formatter);
                }
                tableModelReservas.addRow(new Object[]{
                    r.getId(),
                    (r.getResponsavel() != null ? r.getResponsavel().getNome() : "?"),
                    infoSala,
                    infoData
                });
            }
        }
    }

    private void executarAcaoNovaReserva(){
        List<ItemReserva> reservasTemp = new ArrayList<>(); //(Julia) múltiplas salas em uma reserva

        JPanel panel = new JPanel(new BorderLayout(10, 10));  //painel principal com dois subpaineis: Form e Tabela 
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 5, 5)); //divide a janela em duas colunas, cada linha com a mesma altura
        JPanel panelTabela = new JPanel(new BorderLayout());   //tem um sub painel

        JTextField txtIdPessoa = new JTextField();

        //(Julia) combo box de salas
        java.util.List<Sala> salas = service.listarSalas();
        JComboBox<Sala> comboSala = new JComboBox<>();

        for(Sala s : salas){
            comboSala.addItem(s);   //preenche a lista 
        }

        //(Julia) pra não ficar apagando os parâmetros de data e hora
        MaskFormatter formatoDataHora = null;
        JFormattedTextField txtDataInicio;
        JFormattedTextField txtDataFim;

        try{
            formatoDataHora = new MaskFormatter("##/##/#### ##:##");
            formatoDataHora.setPlaceholderCharacter('_');
        }catch(ParseException e){
            e.printStackTrace();
        }

        txtDataInicio = new JFormattedTextField(formatoDataHora);
        txtDataFim = new JFormattedTextField(formatoDataHora);

        //--------------------------------------------------------
        JButton btnAdicionar = new JButton("Adicionar sala");   

        String[] colunas = {"Sala", "Início", "Fim"};   //define o nome das colunas da tabela
        DefaultTableModel modeloTemp = new DefaultTableModel(colunas, 0);   //inicialmente nenhuma linha
        JTable tabelaTemp = new JTable(modeloTemp); //tabela "dinamica"
        tabelaTemp.setRowHeight(22);

        //add barra de rolagem caso a tabela fique muito grande
        JScrollPane scrollTemp = new JScrollPane(tabelaTemp);
        scrollTemp.setPreferredSize(new Dimension(280, 120));   //ajusta o scroll
        scrollTemp.setMaximumSize(new Dimension(280, 200));
        panelTabela.add(scrollTemp, BorderLayout.CENTER);
            //(Julia) add mais de uma sala em uma única reserva
                btnAdicionar.addActionListener(e->{

                    Sala salaSelecionada = (Sala) comboSala.getSelectedItem();
                    if(salaSelecionada == null){
                        JOptionPane.showMessageDialog(this, "Selecione uma sala");
                        return;
                    }
                    //int idSala = salaSelecionada.getId();
                    try{
                        LocalDateTime inicio = LocalDateTime.parse(txtDataInicio.getText(), formatter);
                        LocalDateTime fim = LocalDateTime.parse(txtDataFim.getText(), formatter); 

                        if(!inicio.isBefore(fim)){
                            JOptionPane.showConfirmDialog(panel, "Horário inválido!");
                        }

                        ItemReserva item = new ItemReserva(salaSelecionada, inicio, fim);
                        reservasTemp.add(item);

                        modeloTemp.addRow(new Object[]{ //add a nova sala à tabela com todas as infos
                            salaSelecionada.toString(),
                            inicio.format(formatter),
                            fim.format(formatter)
                        });
                        int linhas = tabelaTemp.getRowCount();
                        int altura = Math.min(120 + (linhas * 22), 220);

                        scrollTemp.setPreferredSize(new Dimension(280, altura));
                        scrollTemp.revalidate();


                        //reseta os valores pras próximas reservas
                        txtDataInicio.setValue(null);
                        txtDataFim.setValue(null);
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(panel, "Formato inválido de data/hora");
                    }
                });
        //ajustei aqui pra ficar no lugar certo da tabela
        panelForm.add(new JLabel("ID Responsável:"));
        panelForm.add(txtIdPessoa);
        panelForm.add(new JLabel("Sala:"));
        panelForm.add(comboSala);   //mudança pro combo box

        panelForm.add(new JLabel("Início:"));
        panelForm.add(txtDataInicio);
        panelForm.add(new JLabel("Fim:"));
        panelForm.add(txtDataFim);

        panelForm.add(btnAdicionar);
        panelForm.add(scrollTemp);

        panel.add(panelForm, BorderLayout.WEST);
        panel.add(panelTabela, BorderLayout.CENTER);


        int result= JOptionPane.showConfirmDialog(this, panel, "Nova Reserva", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            try{
                int idPessoa = Integer.parseInt(txtIdPessoa.getText());

            Reserva novaReserva = service.criarReserva(idPessoa);//cria reeserva principal

            boolean sucessoFinal = true;
            for(ItemReserva item : reservasTemp){   //(Julia) testando aqui
            boolean ok = service.adicionarItemNaReserva(
                novaReserva.getId(),
                item.getSala().getId(),
                item.getDataHoraInicio(),
                item.getDataHoraFim()
            );
            if(!ok) sucessoFinal = false;
        }
            
                    if (sucessoFinal){
                        JOptionPane.showMessageDialog(this, "Reserva realizada!");
                        listarTodasReservas();
                    }else{
                        JOptionPane.showMessageDialog(this, "Erro: confira os IDs e disponibilidade.");
                    }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Erro de formato. Use dd/MM/yyyy HH:mm");
            }
        }
    }

    private void executarAcaoAlterarReserva() {
        //alexandre: protecao pra caso a pessoa selecione mais de um item pra alterar
        int[] linhas = tabelaPessoas.getSelectedRows();

        if (linhas.length > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione apenas UMA linha para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        String idString = JOptionPane.showInputDialog(this, "Digite o ID da Reserva para ALTERAR:");
        
        if (idString != null) {
            try {
                int idReserva = Integer.parseInt(idString);
                Reserva reservaAtual = service.buscaReservaPorId(idReserva); 

                if (reservaAtual != null) {
                    // preenche o painel 
                    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
                    
                    // pega dados do primeiro item da reserva 
                    ItemReserva itemAtual = reservaAtual.getItensDaReserva().isEmpty() ? null : reservaAtual.getItensDaReserva().get(0);
                    
                    JTextField txtIdPessoa = new JTextField(String.valueOf(reservaAtual.getResponsavel().getId()));
                    JTextField txtIdSala = new JTextField(itemAtual != null ? String.valueOf(itemAtual.getSala().getId()) : "");
                    
                    String dataIniStr = itemAtual != null ? itemAtual.getDataHoraInicio().format(formatter) : "dd/MM/yyyy HH:mm";
                    String dataFimStr = itemAtual != null ? itemAtual.getDataHoraFim().format(formatter) : "dd/MM/yyyy HH:mm";

                    //(Julia) pra não ficar apagando os parâmetros de data e hora
                    MaskFormatter formatoDataHora = null;
                    JFormattedTextField txtDataInicio;
                    JFormattedTextField txtDataFim;

                    try{
                        formatoDataHora = new MaskFormatter("##/##/#### ##:##");
                        formatoDataHora.setPlaceholderCharacter('_');
                    }catch(ParseException e){
                        e.printStackTrace();
                    }

                    txtDataInicio = new JFormattedTextField(formatoDataHora);
                    txtDataFim = new JFormattedTextField(formatoDataHora);
                    //--------------------------------------------------------

                    panel.add(new JLabel("Novo ID Responsável:"));
                    panel.add(txtIdPessoa);
                    panel.add(new JLabel("Novo ID Sala:"));
                    panel.add(txtIdSala);

                    panel.add(new JLabel("Nova Data Início:"));
                    panel.add(txtDataInicio);
                    panel.add(new JLabel("Nova Data Fim:"));
                    panel.add(txtDataFim);

                    int result = JOptionPane.showConfirmDialog(this, panel, "Alterar Reserva ID " + idReserva, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if(result == JOptionPane.OK_OPTION){
                        int idPessoa = Integer.parseInt(txtIdPessoa.getText());
                        int idSala = Integer.parseInt(txtIdSala.getText());
                        LocalDateTime inicio = LocalDateTime.parse(txtDataInicio.getText(), formatter);
                        LocalDateTime fim = LocalDateTime.parse(txtDataFim.getText(), formatter);
                        
                        // Chama o metodo de alterar do service
                        boolean sucesso = service.alterarReserva(idReserva, idPessoa, idSala, inicio, fim);
                        if (sucesso){
                            JOptionPane.showMessageDialog(this, "Reserva alterada!");
                            listarTodasReservas();
                        }else{
                            JOptionPane.showMessageDialog(this, "Erro na alteração.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Reserva não encontrada.");
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Dados inválidos ou erro de formato.");
            }
        }
    }

    // implementanod de fsto agora o metodo de cancelar reserva, antes tava so de enfeite agr ta funcionando
    private void executarAcaoCancelarReserva() {
        String idString = null;
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        
        // tenta pegar o ID da linha selecionada
        if (linhaSelecionada != -1) {
            idString = String.valueOf(tabelaReservas.getValueAt(linhaSelecionada, 0));
        }else{
            idString = JOptionPane.showInputDialog(this, "Digite o ID da Reserva para CANCELAR:");
        }

        if (idString != null) {
            try {
                int idReserva = Integer.parseInt(idString);
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar a reserva " + idReserva + "?");
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucesso = service.cancelarReserva(idReserva);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso!");
                        listarTodasReservas();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao cancelar (ID não encontrado?).");
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    //alexandre adicionando o metodo de executar a cao de buscar por id as reservas:

    private void executarAcaoBuscarReserva(){
        String idString = JOptionPane.showInputDialog(this, "ID da Reserva para BUSCAR:");
        if(idString != null){
            try{
                int id = Integer.parseInt(idString);
                Reserva r = service.buscaReservaPorId(id);
                if(r != null){
                    tableModelReservas.setRowCount(0);
                    String infoSala = "Sem Sala";
                    String infoData = "-";
                    if (!r.getItensDaReserva().isEmpty()) {
                        ItemReserva item = r.getItensDaReserva().get(0);
                        infoSala = "Sala " + item.getSala().getId();
                        infoData = item.getDataHoraInicio().format(formatter); 
                    }
                    tableModelReservas.addRow(new Object[]{
                        r.getId(),
                        (r.getResponsavel() != null ? r.getResponsavel().getNome() : "?"),
                        infoSala,
                        infoData
                    });
                }else{
                    JOptionPane.showMessageDialog(this, "Reserva não encontrada.");
                    listarTodasReservas();
                }
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    public static void main(String[] args) {
        // dica do chat, adicionar o LookAndFeel pra deixar a janela mais bonita 
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            new ReservaGUI();
        });
    }
}
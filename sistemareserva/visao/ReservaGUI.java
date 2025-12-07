package sistemareserva.visao;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; //manipula dados da tabela
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;
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
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //alexandre : janela que apareceao fechar o sistema:
        addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e){
                int confirm =JOptionPane.showConfirmDialog(null, 
                    "Tem certeza que deseja sair do sistema?\nOs dados não salvos serão perdidos", 
                    "Sair", 
                    JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
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
        // Painel principal com fundo suave
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 250)); 

        //ti tulo - topo
        JPanel painelTitulo = new JPanel();
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setBackground(new Color(60, 90, 150));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JLabel titulo = new JLabel("SISTEMA DE RESERVA DE SALAS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitulo = new JLabel("Trabalho - Programação Modular - Professor Matheus");
        subTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitulo.setForeground(new Color(200, 220, 255));
        subTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelTitulo.add(titulo);
        painelTitulo.add(Box.createRigidArea(new Dimension(0, 5)));//espaço entre os textos
        painelTitulo.add(subTitulo);
        
        painel.add(painelTitulo, BorderLayout.NORTH);

        // centro - botoes
        JPanel menuBotoes = new JPanel(new GridLayout(3, 1, 15, 15));
        menuBotoes.setBackground(new Color(245, 245, 250)); 
        // margem pra separar os botoes
        menuBotoes.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200)); 

        JButton btnPessoas = criarBotaoBonito("Gerenciar Pessoas");
        JButton btnSalas = criarBotaoBonito("Gerenciar Salas");
        JButton btnReservas = criarBotaoBonito("Gerenciar Reservas");

        btnPessoas.addActionListener(e -> {
            cardLayout.show(mainPanel, "PESSOAS");
            listarTodasPessoas();
        });
        
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
        // rodape
        JPanel rodape = new JPanel();
        rodape.setBackground(new Color(230, 230, 230));
        rodape.add(new JLabel("Desenvolvido por Alexandre, Julia, João Vitor e Marcus Vinicius"));
        painel.add(rodape, BorderLayout.SOUTH);

        return painel;
    }

    private JButton criarBotaoBonito(String texto){
        JButton btn= new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false); 
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),//borda cinza
            BorderFactory.createEmptyBorder(10, 20, 10, 20) //espaçamento interno
        ));
        // efeito de passar mouse por cima
        btn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                btn.setBackground(new Color(220, 230, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
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
        tabelaPessoas.setAutoCreateRowSorter(true);//ordena
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
        tabelaSalas.setAutoCreateRowSorter(true);//ordena 
        painel.add(botoesPanel, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaSalas), BorderLayout.CENTER);
        return painel;
    }

    // alexandre painel de gerenciar as reservas, copiei do de pessoas e alterei


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
        tabelaReservas.setAutoCreateRowSorter(true);//ordena
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
        // Pega todos os índices selecionados na tabela
        int[] linhasSelecionadas = tabelaPessoas.getSelectedRows();
        
        if (linhasSelecionadas.length == 0) {
            // Se nada foi selecionado, mantém o comportamento antigo (pedir ID)
            String idString = JOptionPane.showInputDialog(this, "Nenhuma linha selecionada.\nDigite o ID para EXCLUIR:");
            if (idString != null) {
                try {
                    int id = Integer.parseInt(idString);
                    if (service.excluirPessoa(id)) {
                        JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro: ID não encontrado.");
                    }
                    listarTodasPessoas();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "ID inválido.");
                }
            }
            return;
        }

        // Constrói uma mensagem listando os IDs que serão excluídos
        StringBuilder idsParaExcluir = new StringBuilder();
        for (int i = 0; i < linhasSelecionadas.length; i++) {
            int modelRow = tabelaPessoas.convertRowIndexToModel(linhasSelecionadas[i]);
            // AQUI ESTAVA O ERRO: Mudamos de 'tableModelPessoas' para 'tableModel'
            idsParaExcluir.append(tableModel.getValueAt(modelRow, 0)).append(" ");
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this, 
            "Tem certeza que deseja excluir os IDs de Pessoa: " + idsParaExcluir.toString().trim() + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int excluidos = 0;
            // Loop reverso para evitar problemas de índice ao remover
            for (int i = linhasSelecionadas.length - 1; i >= 0; i--) {
                int modelRow = tabelaPessoas.convertRowIndexToModel(linhasSelecionadas[i]);
                try {
                    // AQUI TAMBÉM: Mudamos de 'tableModelPessoas' para 'tableModel'
                    int id = (int) tableModel.getValueAt(modelRow, 0);
                    if (service.excluirPessoa(id)) {
                        excluidos++;
                    }
                } catch (Exception e) {
                    // Ignora erros individuais e continua
                }
            }
            
            if (excluidos > 0) {
                JOptionPane.showMessageDialog(this, excluidos + " pessoa(s) excluída(s) com sucesso!");
                listarTodasPessoas(); // Atualiza a tabela
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma pessoa foi excluída (talvez tenham reservas ativas).");
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
        int[] linhasSelecionadas = tabelaSalas.getSelectedRows();
        if (linhasSelecionadas.length > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione apenas UMA sala para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
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


    private void executarAcaoExcluirSala() {
        int[] linhasSelecionadas = tabelaSalas.getSelectedRows();
        
        if (linhasSelecionadas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma ou mais salas para excluir.");
            return;
        }

        StringBuilder idsParaExcluir = new StringBuilder();
        for (int i = 0; i < linhasSelecionadas.length; i++) {
            int modelRow = tabelaSalas.convertRowIndexToModel(linhasSelecionadas[i]);
            idsParaExcluir.append(tableModelSalas.getValueAt(modelRow, 0)).append(" ");
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this, 
            "Tem certeza que deseja excluir os IDs de Sala: " + idsParaExcluir.toString().trim() + "?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int excluidos = 0;
            for (int i = linhasSelecionadas.length - 1; i >= 0; i--) {
                int modelRow = tabelaSalas.convertRowIndexToModel(linhasSelecionadas[i]);
                try {
                    int id = (int) tableModelSalas.getValueAt(modelRow, 0);
                    if (service.excluirSala(id)) {
                        excluidos++;
                    }
                } catch (Exception e) {
                    // Ignora erros individuais e continua
                }
            }
            
            if (excluidos > 0) {
                JOptionPane.showMessageDialog(this, excluidos + " sala(s) excluída(s) com sucesso!");
                listarTodasSalas(); // Atualiza a tabela
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma sala foi excluída.");
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

        Function<Component, JPanel> wrap = comp -> {    //função dentro do método pra facilitar --> formatação
            JPanel p = new JPanel(new BorderLayout());
            p.add(comp, BorderLayout.CENTER);
            return p;
        };

        List<ItemReserva> reservasTemp = new ArrayList<>(); //(Julia) múltiplas salas em uma reserva

        JPanel panel = new JPanel(new BorderLayout(10, 10));  //painel principal com dois subpaineis: Form e Tabela 
        //JPanel panelForm = new JPanel(new GridLayout(0, 2, 5, 5)); 
        //ajuste formatação início
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        //ajuste formatação fim

        JPanel panelTabela = new JPanel(new BorderLayout());   //tem um sub painel

        //JTextField txtIdPessoa = new JTextField();
        java.util.List<Pessoa> pessoa = service.listarPessoas();
        JComboBox<Pessoa> comboPessoa = new JComboBox<>();
        //comboPessoa.setPreferredSize(new Dimension(10, 28));    //formatação

        for(Pessoa p : pessoa){
            comboPessoa.addItem(p);
        }

        //(Julia) combo box de salas
        java.util.List<Sala> salas = service.listarSalas();
        JComboBox<Sala> comboSala = new JComboBox<>();
        //comboSala.setPreferredSize(new Dimension(10, 28));


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
        txtDataInicio.setPreferredSize(new Dimension(10, 28));  //formatação
        txtDataFim = new JFormattedTextField(formatoDataHora);
        txtDataFim.setPreferredSize(new Dimension(10, 28)); //formatação

        //--------------------------------------------------------
        JButton btnAdicionar = new JButton("Adicionar sala");   
        JButton btnExcluir = new JButton("Excluir sala");   
        JPanel panelBotoes = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnExcluir);


        String[] colunas = {"Sala", "Início", "Fim"};   //define o nome das colunas da tabela
        DefaultTableModel modeloTemp = new DefaultTableModel(colunas, 0);   //inicialmente nenhuma linha
        JTable tabelaTemp = new JTable(modeloTemp); //tabela "dinamica"
        tabelaTemp.setRowHeight(22);

        //add barra de rolagem caso a tabela fique muito grande
        JScrollPane scrollTemp = new JScrollPane(tabelaTemp);
        scrollTemp.setPreferredSize(new Dimension(400, 120));   //ajusta o scroll
        scrollTemp.setMaximumSize(new Dimension(500, 200)); //acompanha a largura da janela

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
                        //verificando conflito ao add salas:
                        //horário inválido
                        if (!inicio.isBefore(fim)) {
                            JOptionPane.showMessageDialog(this,
                                "A data de término deve ser posterior à data de início.", "Erro de Data",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        //conflito com reservas já feitas
                        if (service.temConflito(salaSelecionada.getId(), inicio, fim)) {
                            JOptionPane.showMessageDialog(this,
                                    "Esta sala já possui uma reserva cadastrada neste horário.",
                                    "Conflito com banco",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        //conflito com reservas da tabela
                        for (ItemReserva itemExistente : reservasTemp) {
                            if (itemExistente.getSala().getId() == salaSelecionada.getId()) {

                                boolean naoConflita =
                                        fim.isBefore(itemExistente.getDataHoraInicio()) ||
                                        inicio.isAfter(itemExistente.getDataHoraFim());

                                if (!naoConflita) {
                                    JOptionPane.showMessageDialog(this,
                                            "Conflito com outra reserva adicionada nesta mesma operação.",
                                            "Conflito interno",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
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
                    }catch(Exception ex){   //essa exceção já é tratada pelos if else acima, então tem que ver se vai tirar esse try catch ou lançar mais duas exceções (2 tipos de conflito)
                        JOptionPane.showMessageDialog(panel, "Formato inválido de data/hora");
                    }
                });

                //listener do botão excluir:
                btnExcluir.addActionListener(e -> {
                    int linha = tabelaTemp.getSelectedRow();

                    if (linha == -1) {
                        JOptionPane.showMessageDialog(this, "Selecione uma sala para excluir.");
                        return;
                    }

                    reservasTemp.remove(linha);
                    modeloTemp.removeRow(linha);

                    int linhas = tabelaTemp.getRowCount();
                    int novaAltura = Math.min(120 + (linhas * 22), 220);
                    scrollTemp.setPreferredSize(new Dimension(280, novaAltura));
                    scrollTemp.revalidate();
                });

        //formatação (ref: código produtoForm.java disponibilizado no github):
        //linha 1 -> responsavel pela reserva
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;  // label não cresce
        panelForm.add(new JLabel("Responsável:"), gbc);
        gbc.gridx = 1; 
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // combo cresce
        panelForm.add(comboPessoa, gbc);

        //linha 2 -> sala
        gbc.gridx = 0; 
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Sala:"), gbc);
        gbc.gridx = 1; 
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(comboSala, gbc);

        //linha 3 -> datahora início
        gbc.gridx = 0; 
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Início:"), gbc);
        gbc.gridx = 1; 
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtDataInicio, gbc);

        //linha 4 -> datahora fim
        gbc.gridx = 0; 
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Fim:"), gbc);
        gbc.gridx = 1; 
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(txtDataFim, gbc);

        //linha 5 -> botões
        gbc.gridx = 0; 
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panelForm.add(panelBotoes, gbc);

        //linha 6 -> tabela
        gbc.gridx = 0; 
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelForm.add(scrollTemp, gbc);


        panel.add(panelForm, BorderLayout.WEST);
        panel.add(panelTabela, BorderLayout.CENTER);

        panel.setPreferredSize(new Dimension(425, 450));
        int result= JOptionPane.showConfirmDialog(this, panel, "Nova Reserva", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            try{
                Pessoa pessoaSelecionada = (Pessoa) comboPessoa.getSelectedItem();
                if (pessoaSelecionada == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um responsável.");
                    return;
                }
                int idPessoa = pessoaSelecionada.getId();
                
                if (reservasTemp.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Adicione pelo menos uma sala à lista!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean sucesso = service.realizarReservaLista(idPessoa, reservasTemp);

                if (sucesso){
                    JOptionPane.showMessageDialog(this, "Reserva realizada com sucesso!");
                    listarTodasReservas();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Conflito de horário ou ID de pessoa inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "ID da Pessoa inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //alexandre: refiz toda a executaracaoalterar reserva, agora ta funcioando com selecao dos itens
    private void executarAcaoAlterarReserva() {
        // verifica se o usuário selecionou mais de uma linha
        int[] linhasSelecionadas = tabelaReservas.getSelectedRows();
        if (linhasSelecionadas.length > 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione apenas UMA reserva para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idString = null;
        int linhaSelecionada = tabelaReservas.getSelectedRow();
        if (linhaSelecionada != -1) {
            int modelRow = tabelaReservas.convertRowIndexToModel(linhaSelecionada);
            idString = String.valueOf(tableModelReservas.getValueAt(modelRow, 0));
        } else {
            idString = JOptionPane.showInputDialog(this, "Nenhuma linha selecionada.\nDigite o ID da Reserva para ALTERAR:");
        }

        if (idString != null) {
            try {
                int idReserva = Integer.parseInt(idString);
                Reserva reservaAtual = service.buscaReservaPorId(idReserva);

                if (reservaAtual != null) {
                    List<ItemReserva> itensTemp = new ArrayList<>(reservaAtual.getItensDaReserva());
                    
                    JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
                    
                    JPanel panelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JTextField txtIdPessoa = new JTextField(String.valueOf(reservaAtual.getResponsavel().getId()), 10);
                    panelTopo.add(new JLabel("ID Responsável:"));
                    panelTopo.add(txtIdPessoa);
                    
                    String[] colunasItens = {"Sala", "Início (dd/MM/yyyy HH:mm)", "Fim (dd/MM/yyyy HH:mm)"};
                    
                    //bloquear a edicao do nome das salas
                    DefaultTableModel modeloItens = new DefaultTableModel(colunasItens, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            // coluna 0 (sala) retorna false (nao editavel).
                            // colunas 1 e 2 (datas) retorna true (editavel).
                            return column != 0; 
                        }
                    };

                    JTable tabelaItens = new JTable(modeloItens);
                    JScrollPane scrollItens = new JScrollPane(tabelaItens);
                    scrollItens.setPreferredSize(new Dimension(550, 150)); 

                    try {
                        javax.swing.text.MaskFormatter mascaraData = new javax.swing.text.MaskFormatter("##/##/#### ##:##");
                        mascaraData.setPlaceholderCharacter('_');
                        
                        JFormattedTextField campoData = new JFormattedTextField(mascaraData);
                        
                        DefaultCellEditor editorData = new DefaultCellEditor(campoData);
                        
                        tabelaItens.getColumnModel().getColumn(1).setCellEditor(editorData);
                        tabelaItens.getColumnModel().getColumn(2).setCellEditor(editorData);
                    } catch (Exception ex) {
                        ex.printStackTrace(); 
                    }
                    
                    for (ItemReserva item : itensTemp) {
                        modeloItens.addRow(new Object[]{
                            item.getSala().getPredio() + " (" + item.getSala().getId() + ")", 
                            item.getDataHoraInicio().format(formatter),
                            item.getDataHoraFim().format(formatter)
                        });
                    }

                    JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    JButton btnAddSala = new JButton("Adicionar Sala");
                    JButton btnRemSala = new JButton("Remover Sala Selecionada");
                    
                    btnAddSala.addActionListener(e -> {
                        JPanel panelAdd = new JPanel(new GridLayout(0, 2));
                        JComboBox<Sala> comboSala = new JComboBox<>();
                        for(Sala s : service.listarSalas()) comboSala.addItem(s);
                        
                        javax.swing.text.MaskFormatter formatoDataHora = null;
                        try { formatoDataHora = new javax.swing.text.MaskFormatter("##/##/#### ##:##"); formatoDataHora.setPlaceholderCharacter('_'); } catch (Exception ex) {}
                        JFormattedTextField txtIni = new JFormattedTextField(formatoDataHora);
                        JFormattedTextField txtFim = new JFormattedTextField(formatoDataHora);
                        
                        panelAdd.add(new JLabel("Sala:")); panelAdd.add(comboSala);
                        panelAdd.add(new JLabel("Início:")); panelAdd.add(txtIni);
                        panelAdd.add(new JLabel("Fim:")); panelAdd.add(txtFim);
                        
                        int res = JOptionPane.showConfirmDialog(panelPrincipal, panelAdd, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);
                        if (res == JOptionPane.OK_OPTION) {
                            try {
                                Sala s = (Sala) comboSala.getSelectedItem();
                                LocalDateTime ini = LocalDateTime.parse(txtIni.getText(), formatter);
                                LocalDateTime fim = LocalDateTime.parse(txtFim.getText(), formatter);
                                
                                if (fim.isBefore(ini) || fim.isEqual(ini)) throw new Exception("Data final inválida");
                                
                                ItemReserva novoItem = new ItemReserva(s, ini, fim);
                                itensTemp.add(novoItem);
                                modeloItens.addRow(new Object[]{ s.getPredio() + " (" + s.getId() + ")", ini.format(formatter), fim.format(formatter) });
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panelPrincipal, "Erro: " + ex.getMessage());
                            }
                        }
                    });

                    btnRemSala.addActionListener(e -> {
                        int row = tabelaItens.getSelectedRow();
                        if (row != -1) {
                            if (tabelaItens.isEditing()) {
                                tabelaItens.getCellEditor().stopCellEditing();
                            }
                            itensTemp.remove(row);
                            modeloItens.removeRow(row);
                        } else {
                            JOptionPane.showMessageDialog(panelPrincipal, "Selecione um item na tabela para remover.");
                        }
                    });

                    panelBotoes.add(btnAddSala);
                    panelBotoes.add(btnRemSala);

                    panelPrincipal.add(panelTopo, BorderLayout.NORTH);
                    panelPrincipal.add(scrollItens, BorderLayout.CENTER);
                    panelPrincipal.add(panelBotoes, BorderLayout.SOUTH);

                    int result = JOptionPane.showConfirmDialog(this, panelPrincipal, "Editando Reserva " + idReserva, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            if (tabelaItens.isEditing()) {
                                tabelaItens.getCellEditor().stopCellEditing();
                            }

                            int novoIdPessoa = Integer.parseInt(txtIdPessoa.getText());
                            
                            if (itensTemp.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "A reserva precisa ter pelo menos uma sala.", "Aviso", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            List<ItemReserva> itensAtualizadosParaSalvar = new ArrayList<>();
                            
                            for (int i = 0; i < modeloItens.getRowCount(); i++) {
                                String dataIniStr = (String) modeloItens.getValueAt(i, 1);
                                String dataFimStr = (String) modeloItens.getValueAt(i, 2);
                                
                                Sala salaOriginal = itensTemp.get(i).getSala();
                                
                                LocalDateTime novaIni = LocalDateTime.parse(dataIniStr, formatter);
                                LocalDateTime novaFim = LocalDateTime.parse(dataFimStr, formatter);
                                
                                if (novaFim.isBefore(novaIni) || novaFim.isEqual(novaIni)) {
                                    throw new Exception("Data final inválida na linha " + (i+1));
                                }

                                itensAtualizadosParaSalvar.add(new ItemReserva(salaOriginal, novaIni, novaFim));
                            }

                            boolean sucesso = service.atualizarReservaCompleta(idReserva, novoIdPessoa, itensAtualizadosParaSalvar);
                            
                            if (sucesso) {
                                JOptionPane.showMessageDialog(this, "Reserva atualizada com sucesso!");
                                listarTodasReservas();
                            } else {
                                JOptionPane.showMessageDialog(this, "Erro ao atualizar. Verifique conflitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "ID de Pessoa inválido.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Reserva não encontrada.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    // implementanod de fsto agora o metodo de cancelar reserva, antes tava so de enfeite agr ta funcionando

    private void executarAcaoCancelarReserva() {
        int[] linhasSelecionadas = tabelaReservas.getSelectedRows();
        
        if (linhasSelecionadas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma ou mais reservas para cancelar.");
            return;
        }

        StringBuilder idsParaCancelar = new StringBuilder();
        for (int i = 0; i < linhasSelecionadas.length; i++) {
            int modelRow = tabelaReservas.convertRowIndexToModel(linhasSelecionadas[i]);
            idsParaCancelar.append(tableModelReservas.getValueAt(modelRow, 0)).append(" ");
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this, 
            "Tem certeza que deseja CANCELAR os IDs de Reserva: " + idsParaCancelar.toString().trim() + "?", 
            "Confirmar Cancelamento", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            int cancelados = 0;
            for (int i = linhasSelecionadas.length - 1; i >= 0; i--) {
                int modelRow = tabelaReservas.convertRowIndexToModel(linhasSelecionadas[i]);
                try {
                    int id = (int) tableModelReservas.getValueAt(modelRow, 0);
                    if (service.cancelarReserva(id)) {
                        cancelados++;
                    }
                } catch (Exception e) {
                }
            }
            
            if (cancelados > 0) {
                JOptionPane.showMessageDialog(this, cancelados + " reserva(s) cancelada(s) com sucesso!");
                listarTodasReservas(); // Atualiza a tabela
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma reserva foi cancelada.");
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

}
package sistemareserva.visao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; //manipula dados da tabela
import java.awt.*;
import java.util.List;
import sistemareserva.modelo.Pessoa;

public class ReservaGUI extends JFrame {

    private SistemaReservaService service;
    
    private JTable tabelaPessoas; 
    private DefaultTableModel tableModel; //alexandre mudei o jtextarea

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
        
        mainPanel.add(painelMenu, "MENU");
        mainPanel.add(painelPessoas, "PESSOAS");

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
        
        btnSalas.addActionListener(e -> exibirMenuSalas());
        btnReservas.addActionListener(e -> exibirMenuReservas());

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
        JButton btnVoltar = new JButton("Voltar");

        btnCadastrar.addActionListener(e -> executarAcaoCadastrarPessoa());
        btnAlterar.addActionListener(e -> executarAcaoAlterarPessoa());
        btnExcluir.addActionListener(e -> executarAcaoExcluirPessoa());
        btnBuscar.addActionListener(e -> executarAcaoBuscarPessoa());
        btnAtualizar.addActionListener(e -> listarTodasPessoas());
        
        btnVoltar.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));

        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAtualizar);
        botoesPanel.add(btnVoltar);

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
                    tableModel.addRow(new Object[]{pessoa.getId(), pessoa.getNome()});
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

    private void exibirMenuSalas() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de SALAS em construção.");
    }

    private void exibirMenuReservas() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de RESERVAS em construção.");
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
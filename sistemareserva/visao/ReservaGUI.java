package sistemareserva.visao;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import sistemareserva.modelo.Pessoa;

public class ReservaGUI extends JFrame {

    private SistemaReservaService service;
    private JTextArea displayArea;
    private JPanel mainPanel;

    // Elementos do Menu Principal
    private JButton btnGerenciarPessoas;
    private JButton btnGerenciarSalas;
    private JButton btnGerenciarReservas;

    public ReservaGUI() {
        service = new SistemaReservaService(); // Inicializa o serviço e carrega os dados

        setTitle("Sistema de Reserva de Salas - SWING");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        configurarLayoutPrincipal();
        exibirMensagemInicial();

        setVisible(true);
    }

    /** Configura o layout principal (Menu no Topo e Conteúdo no Centro). */
    private void configurarLayoutPrincipal() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Área de Exibição
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // 2. Painel de Botões (Menu Principal)
        JPanel menuPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        btnGerenciarPessoas = new JButton("1 - Gerenciar Pessoas");
        btnGerenciarSalas = new JButton("2 - Gerenciar Salas");
        btnGerenciarReservas = new JButton("3 - Gerenciar Reservas");

        // Adiciona as ações
        btnGerenciarPessoas.addActionListener(e -> exibirMenuPessoas());
        btnGerenciarSalas.addActionListener(e -> exibirMenuSalas());
        btnGerenciarReservas.addActionListener(e -> exibirMenuReservas());

        menuPanel.add(btnGerenciarPessoas);
        menuPanel.add(btnGerenciarSalas);
        menuPanel.add(btnGerenciarReservas);

        // Adiciona os painéis à janela
        mainPanel.add(menuPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void exibirMensagemInicial() {
        displayArea.setText("=== SISTEMA DE RESERVA DE SALAS ===\n");
        displayArea.append("Bem-vindo ao sistema de gerenciamento de reservas!\n");
        displayArea.append("Use os botões acima para navegar nos menus.\n\n");
    }

    // Metodos para gerenciar as pessoas

    private void exibirMenuPessoas() {
        //Remove os componentes antigos e configura o novo layout
        mainPanel.removeAll();

        JPanel panelPessoas = new JPanel(new GridLayout(2, 3, 10, 10));

        //Criação dos botões para as opções do sub-menu
        JButton btnCadastrar = new JButton("1 - Cadastrar Pessoa");
        JButton btnAlterar = new JButton("2 - Alterar Pessoa");
        JButton btnExcluir = new JButton("3 - Excluir Pessoa");
        JButton btnBuscar = new JButton("4 - Buscar por ID");
        JButton btnListar = new JButton("5 - Listar Todas");
        JButton btnVoltar = new JButton("0 - Voltar");

        //Adiciona as ações funcionais
        btnCadastrar.addActionListener(e -> executarAcaoCadastrarPessoa());
        btnAlterar.addActionListener(e -> executarAcaoAlterarPessoa());
        btnExcluir.addActionListener(e -> executarAcaoExcluirPessoa());
        btnBuscar.addActionListener(e -> executarAcaoBuscarPessoa());
        btnListar.addActionListener(e -> listarTodasPessoas());

        btnVoltar.addActionListener(e -> {
            mainPanel.removeAll();
            configurarLayoutPrincipal(); // Volta para o layout inicial
            exibirMensagemInicial();
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        panelPessoas.add(btnCadastrar);
        panelPessoas.add(btnAlterar);
        panelPessoas.add(btnExcluir);
        panelPessoas.add(btnBuscar);
        panelPessoas.add(btnListar);
        panelPessoas.add(btnVoltar);

        // Adiciona os painéis e atualiza a exibição
        mainPanel.add(panelPessoas, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        // Exibe a lista ao entrar no menu
        listarTodasPessoas();
    }

    private void listarTodasPessoas() {
        displayArea.setText("--- LISTA DE TODAS AS PESSOAS CADASTRADAS ---\n");
        List<Pessoa> pessoas = service.listarPessoas();

        if (pessoas.isEmpty()) {
            displayArea.append("Nenhuma pessoa cadastrada.\n");
        } else {
            displayArea.append("Total de pessoas cadastradas: " + pessoas.size() + "\n");
            displayArea.append("----------------------------------------------\n");
            for (Pessoa pessoa : pessoas) {
                displayArea.append("ID: " + pessoa.getId() + " | Nome: " + pessoa.getNome() + "\n");
            }
            displayArea.append("----------------------------------------------\n");
        }
    }

    private void executarAcaoCadastrarPessoa() {
        String nome = JOptionPane.showInputDialog(this, "Digite o NOME da pessoa para cadastrar:", "Cadastrar Pessoa", JOptionPane.QUESTION_MESSAGE);

        if (nome != null) {
            if (service.cadastrarPessoa(nome)) {
                JOptionPane.showMessageDialog(this, "Pessoa '" + nome + "' cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Nome não pode estar vazio.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            }
            listarTodasPessoas(); // Atualiza a lista
        }
    }

    private void executarAcaoExcluirPessoa() {
        String idString = JOptionPane.showInputDialog(this, "Digite o ID da pessoa para EXCLUIR:");

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                if (service.excluirPessoa(id)) {
                    JOptionPane.showMessageDialog(this, "Pessoa ID " + id + " excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Pessoa ID " + id + " não encontrada (ou possui reservas pendentes).", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                listarTodasPessoas();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarAcaoBuscarPessoa() {
        String idString = JOptionPane.showInputDialog(this, "Digite o ID da pessoa para BUSCAR:");

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Pessoa pessoa = service.buscarPessoaPorId(id);

                if (pessoa != null) {
                    JOptionPane.showMessageDialog(this, "Pessoa Encontrada:\nID: " + pessoa.getId() + "\nNome: " + pessoa.getNome(), "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Pessoa ID " + id + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarAcaoAlterarPessoa() {
        String idString = JOptionPane.showInputDialog(this, "Digite o ID da pessoa para ALTERAR:");

        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Pessoa pessoa = service.buscarPessoaPorId(id);

                if (pessoa != null) {
                    String novoNome = JOptionPane.showInputDialog(this, "Digite o NOVO nome para '" + pessoa.getNome() + "':", "Alterar Pessoa", JOptionPane.QUESTION_MESSAGE);

                    if (novoNome != null && service.alterarPessoa(id, novoNome)) {
                        JOptionPane.showMessageDialog(this, "Pessoa alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        listarTodasPessoas();
                    } else if (novoNome != null) {
                        JOptionPane.showMessageDialog(this, "Erro: Nome não pode estar vazio.", "Erro de Alteração", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Pessoa ID " + id + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Fazer os metodos da sala e reservas

    private void exibirMenuSalas() {
        displayArea.setText("--- GERENCIAR SALAS ---\n\n");
        displayArea.append("Ações de Sala em implementação. Use o menu Pessoas para ver as funcionalidades.");

    }

    private void exibirMenuReservas() {
        displayArea.setText("--- GERENCIAR RESERVAS ---\n\n");
        displayArea.append("Ações de Reserva em implementação. Use o menu Pessoas para ver as funcionalidades.");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReservaGUI();
        });
    }
}
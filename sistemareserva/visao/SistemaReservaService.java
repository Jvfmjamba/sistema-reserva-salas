package sistemareserva.visao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import sistemareserva.modelo.*;
import sistemareserva.persistencia.*;

public class SistemaReservaService {
    private BancoDeDados banco = new BancoDeDados();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SistemaReservaService() {
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        // System.out.println("Inicializando sistema e carregando dados..."); // Removido o print
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

    public boolean cadastrarPessoa(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        Pessoa pessoa = new Pessoa(nome.trim());
        banco.getPessoas().inserir(pessoa);
        return true; // Sucesso
    }

    public List<Pessoa> listarPessoas() {
        return banco.getPessoas().listarTodos();
    }

    public boolean excluirPessoa(int id) {
        // Verificação adicional: o ideal é checar se a pessoa tem reservas
        // Mas por simplicidade, vamos usar a função de exclusão do banco
        return banco.getPessoas().excluir(id);
    }
    public Pessoa buscarPessoaPorId(int id) {
        return banco.getPessoas().buscaId(id);
    }

    public boolean alterarPessoa(int id, String novoNome) {
        Pessoa pessoa = banco.getPessoas().buscaId(id);
        if (pessoa == null || novoNome == null || novoNome.trim().isEmpty()) {
            return false;
        }
        pessoa.setNome(novoNome.trim());
        return banco.getPessoas().alterar(pessoa);
    }

//adicionar aqui os metods de reserva e sala
}
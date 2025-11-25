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

    //alexandre aqui adiocionei os metos que tavam precisando pra gui funcionar
    
    //metodos novos para funcionar a nova GUI, agr com reserva e salas funcionando
     public List<Sala> listarSalas(){
        // alexandre metodo necessario para a tabela de salas da GUI
        return banco.getSalas().listarTodos();
    }

    public boolean cadastrarSala(String predio, int capacidade, boolean quadro, boolean projetor, boolean pc, boolean ar) {
        // alexandre metodo para receber os dados do formulario da GUI e criar a Sala
        if (predio == null || predio.trim().isEmpty() || capacidade <= 0) {
            return false;
        }
        Sala novaSala = new Sala(predio, capacidade, quadro, projetor, pc, ar);
        banco.getSalas().inserir(novaSala);
        return true;
    }

    public boolean excluirSala(int id) {
        // alexandre metodo de excluir sala pelo id vindo do botao da GUI
        return banco.getSalas().excluir(id);
    }

    // metodos novos adiocnados para funcionar a busca de sala, alterar e cancelar sala, alterar e cancelar reserva

    public Sala buscaSalaPorId(int id) {
        return banco.getSalas().buscaId(id);
    }

    public boolean alterarSala(int id, String predio, int capacidade, boolean quadro, boolean projetor, boolean pc, boolean ar) {
        Sala sala = banco.getSalas().buscaId(id);
        if (sala == null) return false;
        
        sala.setPredio(predio);
        sala.setCapacidade(capacidade);
        // atualiza os recursos
        sala.quadro = quadro;
        sala.projetor = projetor;
        sala.computador = pc;
        sala.arCondicionado = ar;
        
        return banco.getSalas().alterar(sala);
    }

    public List<Reserva> listarReservas() {
        // alexandre metodo  para a tabela de reservas
        return banco.getReservas().listarTodos();
    }

    public Reserva buscaReservaPorId(int id) {
        return banco.getReservas().buscaId(id);
    }

    public boolean realizarReserva(int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        // alexansre busca a pessoa
        Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
        if(responsavel == null) return false;
        // busca a sala
        Sala sala = banco.getSalas().buscaId(idSala);
        if (sala == null) return false;
        // verifica se o iniciio ta antes o fim, se nao crasha
        if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;
        Reserva novaReserva = new Reserva(responsavel);
        ItemReserva item = new ItemReserva(sala, inicio, fim);
        novaReserva.adicionarItem(item);
        // salvando no banco de dados
        banco.getReservas().inserir(novaReserva);
        responsavel.adicionarReserva(novaReserva);

        return true;
    }

    public boolean cancelarReserva(int id) {
        Reserva reserva = banco.getReservas().buscaId(id);
        if (reserva == null) return false;
        
        if (reserva.getResponsavel() != null) {
            reserva.getResponsavel().removerReserva(reserva);
        }
        
        return banco.getReservas().excluir(id);
    }

    public boolean alterarReserva(int idReserva, int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        Reserva reserva = banco.getReservas().buscaId(idReserva);
        if (reserva == null) return false;

        Pessoa novaPessoa = banco.getPessoas().buscaId(idPessoa);
        Sala novaSala = banco.getSalas().buscaId(idSala);

        if (novaPessoa == null || novaSala == null) return false;
        if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;

        if (reserva.getResponsavel().getId() != idPessoa) {
            reserva.getResponsavel().removerReserva(reserva); // remove do antigo
            reserva.setResponsavel(novaPessoa);
            novaPessoa.adicionarReserva(reserva); // adiciona no novo
        }

        if (!reserva.getItensDaReserva().isEmpty()) {
            ItemReserva itemAntigo = reserva.getItensDaReserva().get(0);
            reserva.removerItem(itemAntigo);
        }
        
        ItemReserva novoItem = new ItemReserva(novaSala, inicio, fim);
        reserva.adicionarItem(novoItem);

        return banco.getReservas().alterar(reserva);
    }
}
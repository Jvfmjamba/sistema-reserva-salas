package sistemareserva.visao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import sistemareserva.modelo.*;
import sistemareserva.persistencia.*;

public class SistemaReservaService {
    private BancoDeDados banco;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SistemaReservaService() {
        this.banco = new BancoDeDados();
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        banco.getPessoas().inserir(new Pessoa("Alexandre"));
        banco.getPessoas().inserir(new Pessoa("Julia"));
        banco.getPessoas().inserir(new Pessoa("Joao Vitor"));
        banco.getPessoas().inserir(new Pessoa("Marcus Vinicius"));
        banco.getPessoas().inserir(new Pessoa("Professor Matheus"));
        banco.getPessoas().inserir(new Pessoa("Professor Elverton"));
        banco.getPessoas().inserir(new Pessoa("Professor Vinicius"));
        banco.getPessoas().inserir(new Pessoa("Professora Milenne"));

        banco.getSalas().inserir(new Sala("CCOMP", 50));
        banco.getSalas().inserir(new Sala("CCOMP", 30));
        banco.getSalas().inserir(new Sala("CCOMP", 45));
        banco.getSalas().inserir(new Sala("CCOMP", 100));
        banco.getSalas().inserir(new Sala("CCOMP-lab", 30));
        banco.getSalas().inserir(new Sala("CCOMP-lab", 20));
        banco.getSalas().inserir(new Sala("ECONOMIA", 35));
        banco.getSalas().inserir(new Sala("ECONOMIA", 25));
        banco.getSalas().inserir(new Sala("ECONOMIA", 55));
        banco.getSalas().inserir(new Sala("ZOOTEC", 50));
        banco.getSalas().inserir(new Sala("ZOOTEC", 10));
        banco.getSalas().inserir(new Sala("ARQUIT", 25));
        banco.getSalas().inserir(new Sala("ARQUIT", 30));
        banco.getSalas().inserir(new Sala("MUSICA", 55));
        banco.getSalas().inserir(new Sala("MUSICA", 15));
    }

    // PESSOAS PESSOAS PESSOAS
    public boolean cadastrarPessoa(String nome) {
        if (nome == null || nome.trim().isEmpty()) return false;
        return banco.getPessoas().inserir(new Pessoa(nome.trim()));
    }
    public List<Pessoa> listarPessoas() { return banco.getPessoas().listarTodos(); }
    public boolean excluirPessoa(int id) { return banco.getPessoas().excluir(id); }
    public Pessoa buscarPessoaPorId(int id) {
        try { return banco.getPessoas().buscaId(id); } catch (IdInexistenteException e) { return null; }
    }
    public boolean alterarPessoa(int id, String novoNome) {
        try {
            Pessoa p = banco.getPessoas().buscaId(id);
            if (p == null || novoNome == null || novoNome.trim().isEmpty()) return false;
            p.setNome(novoNome.trim());
            return banco.getPessoas().alterar(p);
        } catch (IdInexistenteException e) { return false; }
    }
    // PESSOAS PESSOAS PESSOAS

    // SALAS SALAS SALAS
    public List<Sala> listarSalas(){ return banco.getSalas().listarTodos(); }
    public boolean cadastrarSala(String predio, int capacidade) {
        if (predio == null || predio.trim().isEmpty() || capacidade <= 0) return false;
        Sala novaSala = new Sala(predio, capacidade);
        return banco.getSalas().inserir(novaSala);
    }
    public boolean excluirSala(int id) { return banco.getSalas().excluir(id); }
    public Sala buscaSalaPorId(int id) {
        try { return banco.getSalas().buscaId(id); } catch (IdInexistenteException e) { return null; }
    }
    public boolean alterarSala(int id, String predio, int capacidade) {
        try {
            Sala sala = banco.getSalas().buscaId(id);
            sala.setPredio(predio);
            sala.setCapacidade(capacidade);
            return banco.getSalas().alterar(sala);
        } catch (IdInexistenteException e) { return false; }
    }
    // SALAS SALAS SALAS

    // RESERVAS RESERVAS RESERVAS
    public List<Reserva> listarReservas() { return banco.getReservas().listarTodos(); }
    public Reserva buscaReservaPorId(int id) {
        try { return banco.getReservas().buscaId(id); } catch (IdInexistenteException e) { return null; }
    }

    // METODO PARA UM ITEM NA RESERVA
    public boolean realizarReserva(int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        try {
            if (temConflito(idSala, inicio, fim)) return false; // Verifica conflito antes

            Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
            Sala sala = banco.getSalas().buscaId(idSala);
            if (responsavel == null || sala == null) return false;
            if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;

            Reserva novaReserva = new Reserva(responsavel);
            ItemReserva item = new ItemReserva(sala, inicio, fim);
            novaReserva.adicionarItem(item);
            
            boolean inseriu = banco.getReservas().inserir(novaReserva);
            if (inseriu) responsavel.adicionarReserva(novaReserva);
            return inseriu;
        } catch (IdInexistenteException e) { return false; }
    }

    //NOVO METODO QUE SUBSTITUI OS ANTIGOS ADICIONAR ITEM E CRIAR RESERVA, AGORA NUM LUGAR SO
    public boolean realizarReservaLista(int idPessoa, List<ItemReserva> itens) {
        try {
            Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
            if (responsavel == null || itens.isEmpty()) return false;

            //VALIDA OS CONFLITOS ANTES DE SALVAR
            for (ItemReserva item : itens) {
                if (temConflito(item.getSala().getId(), item.getDataHoraInicio(), item.getDataHoraFim())) {
                    return false; //SE DER ALGUM ERRO CANCELA TUDO
                }
            }

            //CRIA A RESERVA E ADICOINA TODS OS ITENS
            Reserva novaReserva = new Reserva(responsavel);
            for (ItemReserva item : itens) {
                novaReserva.adicionarItem(item);
            }

            //SLVA NO BANCO DE DADOS
            boolean inseriu = banco.getReservas().inserir(novaReserva);
            if (inseriu) {
                responsavel.adicionarReserva(novaReserva);
                return true;
            }
            return false;

        } catch (IdInexistenteException e) {
            return false;
        }
    }

    public boolean cancelarReserva(int id) {
        try {
            Reserva reserva = banco.getReservas().buscaId(id);
            if (reserva.getResponsavel() != null) {
                reserva.getResponsavel().removerReserva(reserva);
            }
            return banco.getReservas().excluir(id);
        } catch (IdInexistenteException e) { return false; }
    }

    public boolean alterarReserva(int idReserva, int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        try {
            Reserva reserva = banco.getReservas().buscaId(idReserva);
            Pessoa novaPessoa = banco.getPessoas().buscaId(idPessoa);
            Sala novaSala = banco.getSalas().buscaId(idSala);

            if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;

            if (reserva.getResponsavel().getId() != idPessoa) {
                reserva.getResponsavel().removerReserva(reserva);
                reserva.setResponsavel(novaPessoa);
                novaPessoa.adicionarReserva(reserva);
            }

            if (!reserva.getItensDaReserva().isEmpty()) {
                reserva.getItensDaReserva().clear(); //LIMPA OS ITENS ANTIGOS
            }
            
            ItemReserva novoItem = new ItemReserva(novaSala, inicio, fim);
            reserva.adicionarItem(novoItem);

            return banco.getReservas().alterar(reserva);
        } catch (IdInexistenteException e) { return false; }
    }

    public boolean atualizarReservaCompleta(int idReserva, int idPessoa, List<ItemReserva> novosItens){
        try {
            Reserva reserva = banco.getReservas().buscaId(idReserva);
            Pessoa novaPessoa = banco.getPessoas().buscaId(idPessoa);
            if (reserva == null || novaPessoa == null) return false;

            for (ItemReserva item : novosItens) {
                if (temConflitoIgnorandoReserva(idReserva, item.getSala().getId(), item.getDataHoraInicio(), item.getDataHoraFim())) {
                    return false;
                }
            }

            if (reserva.getResponsavel().getId() != idPessoa) {
                reserva.getResponsavel().removerReserva(reserva);
                reserva.setResponsavel(novaPessoa);
                novaPessoa.adicionarReserva(reserva);
            }

            reserva.getItensDaReserva().clear();
            for (ItemReserva item : novosItens){
                reserva.adicionarItem(item);
            }
            return banco.getReservas().alterar(reserva);
        } catch(IdInexistenteException e) { return false; }
    }
    // RESERVAS RESERVAS RESERVAS

    public boolean temConflito(int idSala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva r : banco.getReservas().listarTodos()) {
            for (ItemReserva item : r.getItensDaReserva()) {
                if (item.getSala().getId() == idSala) {
                    if (inicio.isBefore(item.getDataHoraFim()) && fim.isAfter(item.getDataHoraInicio())) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }

    private boolean temConflitoIgnorandoReserva(int idReservaIgnorar, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva r : banco.getReservas().listarTodos()) {
            if (r.getId() == idReservaIgnorar) continue;
            for (ItemReserva item : r.getItensDaReserva()) {
                if (item.getSala().getId() == idSala) {
                    if (inicio.isBefore(item.getDataHoraFim()) && fim.isAfter(item.getDataHoraInicio())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
package sistemareserva.visao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import sistemareserva.modelo.*;
import sistemareserva.persistencia.*;

public class SistemaReservaService {
    private BancoDeDados banco;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    //cria o banco de dados e chama o metodo que insere pessoas e salas iniciais.
    public SistemaReservaService() {
        this.banco = new BancoDeDados();
        carregarDadosIniciais();
    }
    //insere no banco uma lista fixa de pessoas e salas ja pré-cadastradas para iniciar o sistema com dados iniciais.
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

    public boolean cadastrarPessoa(String nome) {
        if (nome == null || nome.trim().isEmpty()) 
            return false;
        return banco.getPessoas().inserir(new Pessoa(nome.trim()));
    }

    public List<Pessoa> listarPessoas() { 
        return banco.getPessoas().listarTodos(); 
    }

    public boolean excluirPessoa(int id) { 
        return banco.getPessoas().excluir(id); 
    }

    public Pessoa buscarPessoaPorId(int id) {
        try { 
            return banco.getPessoas().buscaId(id); 
        } catch (IdInexistenteException e) {
            return null; 
        }
    }

    //altera o nome de uma pessoa: busca a pessoa –> valida o novo nome -> salva de volta no banco
    public boolean alterarPessoa(int id, String novoNome) {
        try {
            Pessoa p = banco.getPessoas().buscaId(id);
            if (p == null || novoNome == null || novoNome.trim().isEmpty()) return false;
            p.setNome(novoNome.trim());
            return banco.getPessoas().alterar(p);
        } catch (IdInexistenteException e) { return false; }
    }

    //retorna todas as salas cadastradas.
    public List<Sala> listarSalas(){ 
        return banco.getSalas().listarTodos(); 
    }

    //cadastra uma nova sala com predio e capacidade, valida os dados antes de salvar
    public boolean cadastrarSala(String predio, int capacidade) {
        if (predio == null || predio.trim().isEmpty() || capacidade <= 0) return false;
        Sala novaSala = new Sala(predio, capacidade);
        return banco.getSalas().inserir(novaSala);
    }

    //remove uma sala pelo ID
    public boolean excluirSala(int id) { 
        return banco.getSalas().excluir(id); 
    }

    //busca e retorna a sala pelo ID, se não existir, retorna null
    public Sala buscaSalaPorId(int id) {
        try{
            return banco.getSalas().buscaId(id); 
        }catch (IdInexistenteException e){ 
            return null; 
        }
    }

    //atualiza os dados de uma sala existente: busca a sala –> altera prédio e capacidade –> salva no banco
    public boolean alterarSala(int id, String predio, int capacidade) {
        try {
            Sala sala = banco.getSalas().buscaId(id);
            sala.setPredio(predio);
            sala.setCapacidade(capacidade);
            return banco.getSalas().alterar(sala);
        } catch (IdInexistenteException e) { return false; }
    }

    //retorna todas as reservas cadastradas no banco
    public List<Reserva> listarReservas() {
        return banco.getReservas().listarTodos(); 
    }

    public Reserva buscaReservaPorId(int id) {
        try { 
            return banco.getReservas().buscaId(id); 
        }catch(IdInexistenteException e) { 
            return null; 
        }
    }

    // cria uma reserva simples : valida dados, verifica conflitos e salva
    public boolean realizarReserva(int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        try {
            if (temConflito(idSala, inicio, fim)) return false; // verifica conflito antes

            Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
            Sala sala = banco.getSalas().buscaId(idSala);
            if (responsavel == null || sala == null) 
                return false;
            if (inicio.isAfter(fim) || inicio.isEqual(fim)) 
                return false;

            Reserva novaReserva = new Reserva(responsavel);
            ItemReserva item = new ItemReserva(sala, inicio, fim);
            novaReserva.adicionarItem(item);
            
            boolean inseriu = banco.getReservas().inserir(novaReserva);
            if (inseriu) responsavel.adicionarReserva(novaReserva);
            return inseriu;
        } catch (IdInexistenteException e) { 
            return false; 
        }
    }

    // cria uma reserva com vários itens (várias salas e horários) apos validar tudo e verificar conflitos
    public boolean realizarReservaLista(int idPessoa, List<ItemReserva> itens) {
        try {
            Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
            if (responsavel == null || itens.isEmpty()) return false;

            for (ItemReserva item : itens) {
                if (temConflito(item.getSala().getId(), item.getDataHoraInicio(), item.getDataHoraFim())) {
                    return false; 
                }
            }

            Reserva novaReserva = new Reserva(responsavel);
            for (ItemReserva item : itens) {
                novaReserva.adicionarItem(item);
            }

            //salva no banco de dados
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

    //verifica se não foram digitados caracteres inválidos (ex: números) no campoo do nome
    public void somenteLetras(String texto) throws EntradaInvalidaException {
        if (texto == null || texto.trim().isEmpty())
            throw new EntradaInvalidaException("O campo não pode estar vazio!");

        for (char c : texto.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                throw new EntradaInvalidaException("Nomes devem conter apenas letras!");
            }
        }
    }

    //  cancela uma reserva: remove do responsável e apaga do banco
    public boolean cancelarReserva(int id) {
        try {
            Reserva reserva = banco.getReservas().buscaId(id);
            if (reserva.getResponsavel() != null) {
                reserva.getResponsavel().removerReserva(reserva);
            }
            return banco.getReservas().excluir(id);
        } catch (IdInexistenteException e) { return false; }
    }

    //  altera uma reserva simples: troca responsavel, substitui item e salva a atualização.
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
                reserva.getItensDaReserva().clear(); //limpa os intens antigos
            }
            
            ItemReserva novoItem = new ItemReserva(novaSala, inicio, fim);
            reserva.adicionarItem(novoItem);

            return banco.getReservas().alterar(reserva);
        } catch (IdInexistenteException e) { return false; }
    }

    // altera uma reserva completa (múltiplas salas): verifica conflitos, troca responsável e substitui todos os itens.
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

    // verifica se a sala está ocupada no horário informado, comparando com todas as reservas cadastradas
    public boolean temConflito(int idSala, LocalDateTime inicio, LocalDateTime fim){
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

    // verifica conflito de horário ignorando uma reserva específica (usado ao editar reservas).
    private boolean temConflitoIgnorandoReserva(int idReservaIgnorar, int idSala, LocalDateTime inicio, LocalDateTime fim){
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
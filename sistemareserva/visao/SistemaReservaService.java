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

        banco.getSalas().inserir(new Sala("CCOMP", 50));
        banco.getSalas().inserir(new Sala("CCOMP", 30));
        banco.getSalas().inserir(new Sala("CCOMP", 100));
        banco.getSalas().inserir(new Sala("CCOMP", 10));
        banco.getSalas().inserir(new Sala("ZOOTEC", 25));
        banco.getSalas().inserir(new Sala("ARQUIT", 40));
    }

    public boolean cadastrarPessoa(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        Pessoa pessoa = new Pessoa(nome.trim());
        //alexandre: agr retorna o resultado do inserir, com boolena true ou false
        return banco.getPessoas().inserir(pessoa);
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
        try{
            // agr trata a excecao
        return banco.getPessoas().buscaId(id);
        }catch(IdInexistenteException e){
            return null;
        }
    }

    //aletrando de acordo com o novo persisnte 
    public boolean alterarPessoa(int id, String novoNome){
        try{
            Pessoa pessoa = banco.getPessoas().buscaId(id);
            if(pessoa == null || novoNome == null || novoNome.trim().isEmpty()){
                return false;
            }
            pessoa.setNome(novoNome.trim());
            return banco.getPessoas().alterar(pessoa);
        }catch(IdInexistenteException e){
            return false;
        }
    }

    //adicionar aqui os metods de reserva e sala

    //alexandre aqui adiocionei os metos que tavam precisando pra gui funcionar
    
    //metodos novos para funcionar a nova GUI, agr com reserva e salas funcionando
     public List<Sala> listarSalas(){
        // alexandre metodo necessario para a tabela de salas da GUI
        return banco.getSalas().listarTodos();
    }

    public boolean cadastrarSala(String predio, int capacidade) {
        // alexandre metodo para receber os dados do formulario da GUI e criar a Sala
        if (predio == null || predio.trim().isEmpty() || capacidade <= 0) {
            return false;
        }
        Sala novaSala = new Sala(predio, capacidade);
        // alexandre mudança do return
        return banco.getSalas().inserir(novaSala);
    }

    public boolean excluirSala(int id) {
        // alexandre metodo de excluir sala pelo id vindo do botao da GUI
        return banco.getSalas().excluir(id);
    }

    // metodos novos adiocnados para funcionar a busca de sala, alterar e cancelar sala, alterar e cancelar reserva

    //alexandre mudanaça agr trata excecao
    public Sala buscaSalaPorId(int id) {
        try{
            return banco.getSalas().buscaId(id);
        }catch (IdInexistenteException e){
            return null;
        }
    }

    public boolean alterarSala(int id, String predio, int capacidade) {
        try {
            Sala sala = banco.getSalas().buscaId(id);
            // atualiza os recrusos
            sala.setPredio(predio);
            sala.setCapacidade(capacidade);
            /*sala.quadro = quadro;
            sala.projetor = projetor;
            sala.computador = pc;
            sala.arCondicionado = ar;*/
            
            return banco.getSalas().alterar(sala);
        }catch(IdInexistenteException e){
            return false;
        }
    }

    //reservaaaaaaasssssssssss

    public List<Reserva> listarReservas() {
        // alexandre metodo  para a tabela de reservas
        return banco.getReservas().listarTodos();
    }

    public Reserva buscaReservaPorId(int id) {
        try{
            return banco.getReservas().buscaId(id);
        }catch (IdInexistenteException e) {
            return null;
        }
    }


    //nova funcao de realizar reserva agr tratando excecao
    public boolean realizarReserva(int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        try{
            Pessoa responsavel = banco.getPessoas().buscaId(idPessoa);
            Sala sala = banco.getSalas().buscaId(idSala);
            if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;
            Reserva novaReserva = new Reserva(responsavel);
            ItemReserva item = new ItemReserva(sala, inicio, fim);
            novaReserva.adicionarItem(item);
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

    //método exclusivo para verificar conflito:
    public boolean temConflito(int idSala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva r : banco.getReservas2()) {
            for (ItemReserva item : r.getItensDaReserva()) {
                if (item.getSala().getId() == idSala) {
                    boolean naoConflita = fim.isBefore(item.getDataHoraInicio()) || inicio.isAfter(item.getDataHoraFim());
                    if (!naoConflita) {
                        return true;   //conflito
                    }
                }
            }
        }
        return false;  //sem conflito
    }

    public boolean temConflitoIgnorandoReserva(int idReservaIgnorar, int idSala, LocalDateTime inicio, LocalDateTime fim) {

        for (Reserva r : banco.getReservas2()) {

            // ignora a própria reserva que está sendo alterada
            if (r.getId() == idReservaIgnorar) {
                continue;
            }

            for (ItemReserva item : r.getItensDaReserva()) {

                // só importa conflito com a mesma sala
                if (item.getSala().getId() == idSala) {

                    // NÃO há conflito se:
                    // fim < início existente  OU  início > fim existente
                    boolean naoConflita =
                            fim.isBefore(item.getDataHoraInicio()) ||
                            inicio.isAfter(item.getDataHoraFim());

                    if (!naoConflita) {
                        return true; // conflito existe
                    }
                }
            }
        }

        return false; // nenhum conflito encontrado
    }



    public Reserva criarReserva(int idPessoa) {     //(Julia) pra conseguir colocar mais de uma sala na mesma reserva
        Pessoa p = banco.buscarPessoa(idPessoa);
        if (p == null) return null;

        Reserva reserva = new Reserva();
        reserva.setResponsavel(p);

        banco.salvarReserva(reserva);
        return reserva;
    }

    public boolean adicionarItemNaReserva(int idReserva, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        Reserva reserva = banco.buscarReserva(idReserva);
        Sala sala = banco.buscarSala(idSala);

        if (reserva == null || sala == null) return false;

        for (Reserva r : banco.getReservas2()) { //verifica conflito
            for (ItemReserva item : r.getItensDaReserva()) {
                if (item.getSala().getId() == idSala) {
                    if (!(fim.isBefore(item.getDataHoraInicio()) || inicio.isAfter(item.getDataHoraFim()))) {
                        return false; // conflito
                    }
                }
            }
        }

        ItemReserva novoItem = new ItemReserva(sala, inicio, fim);
        reserva.adicionarItem(novoItem);
        return true;
    }


    //nova funcao de cancelar reserva tratando excecao
    public boolean cancelarReserva(int id){
        try{
            Reserva reserva = banco.getReservas().buscaId(id);
            if (reserva.getResponsavel() != null) {
                reserva.getResponsavel().removerReserva(reserva);
            }
            return banco.getReservas().excluir(id);
        }catch (IdInexistenteException e){
            return false;
        }
    }

    //alexandre alterando a funcao de alterar reserva agr tratando excecao
    public boolean alterarReserva(int idReserva, int idPessoa, int idSala, LocalDateTime inicio, LocalDateTime fim) {
        try {
            Reserva reserva = banco.getReservas().buscaId(idReserva);
            Pessoa novaPessoa = banco.getPessoas().buscaId(idPessoa);
            Sala novaSala = banco.getSalas().buscaId(idSala);

            if (inicio.isAfter(fim) || inicio.isEqual(fim)) return false;

            // Se mudou o responsável
            if (reserva.getResponsavel().getId() != idPessoa) {
                reserva.getResponsavel().removerReserva(reserva);
                reserva.setResponsavel(novaPessoa);
                novaPessoa.adicionarReserva(reserva);
            }

            if (!reserva.getItensDaReserva().isEmpty()) {
                ItemReserva itemAntigo = reserva.getItensDaReserva().get(0);
                reserva.removerItem(itemAntigo);
            }
            
            ItemReserva novoItem = new ItemReserva(novaSala, inicio, fim);
            reserva.adicionarItem(novoItem);

            return banco.getReservas().alterar(reserva);

        } catch (IdInexistenteException e) {
            return false; // Algum ID não foi encontrado
        }
    }
}
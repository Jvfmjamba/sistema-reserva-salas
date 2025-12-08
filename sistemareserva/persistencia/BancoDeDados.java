package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;
import sistemareserva.visao.*;

public class BancoDeDados {
    Persistente<Pessoa> pessoas = new Persistente<>(); //alexandre: de acordo com as especificacoes os dados tem que ficar nos objetos, ent removi o "static"
    Persistente<Sala> salas = new Persistente<>(); //alexandre: de acordo com as especificacoes os dados tem que ficar nos objetos, ent removi o "static"
    Persistente<Reserva> reservas = new Persistente<>();

    //(Julia) mais de uma sala na mesma reserva

    // Salva uma reserva no banco usando o método inserir de Persistente.
    // Inserir é usado em vez de add porque Persistente controla IDs e regras internas.

    public void salvarReserva(Reserva reserva) {    
        reservas.inserir(reserva);  //talvez esse inserir de problema, mas n da pra usar add pq não é uma lista
    }
// Percorre todas as reservas e retorna a reserva cujo ID corresponde ao informado.
// Se não encontrar, retorna null.

    public Reserva buscarReserva(int id) {
        for (Reserva r : reservas.listarTodos()) {
            if (r.getId() == id) return r;
        }
        return null;
    }
// Retorna uma cópia da lista de reservas armazenadas no Persistente.
     public List<Reserva> getReservas2() {    //novo get reservas
        return reservas.listarTodos();
    } 

    //adicionando getters e setters para pessoa, sala e reserva
    public Persistente<Pessoa> getPessoas() { //retirei static
        return pessoas;
    }

    public void setPessoas(Persistente<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public Persistente<Reserva> getReservas() {return reservas;}

    public void setReservas(Persistente<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Persistente<Sala> getSalas() { // retirei static
        return salas;
    }

    public void setSalas(Persistente<Sala> salas) {
        this.salas = salas;
    }

// Percorre todas as pessoas cadastradas e retorna aquela com o ID informado.
// Se não encontrar, retorna null.
    public Pessoa buscarPessoa(int id){
        for (Pessoa p : pessoas.listarTodos()){
            if (p.getId() == id) return p;
        }
        return null;
    }

    // Percorre todas as salas cadastradas e retorna aquela com o ID informado.
// Se não encontrar, retorna null.
    public Sala buscarSala(int id){
        for(Sala s : salas.listarTodos()){
            if (s.getId() == id) return s;
        }
        return null;
    }

}

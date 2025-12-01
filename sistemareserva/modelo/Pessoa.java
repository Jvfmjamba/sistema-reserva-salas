package sistemareserva.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pessoa extends Entidade implements Serializable {
    //Entidade matricula = new Entidade();
    protected String nome;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pessoa(){
        nome = null;
    }

    public Pessoa(String nome){
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Pessoa - Matricula = " + id + ", Nome= " + nome;
    }

    // alexandre tentando consertar os erros que o matheus falou

    private List<Reserva> minhasReservas =new ArrayList<>();

    public void adicionarReserva(Reserva r){
        this.minhasReservas.add(r);
    }

    public List<Reserva> getMinhasReservas(){
        return minhasReservas;
    }

    //alexandre adicionei a funcao removerpessoa

    public void removerReserva(Reserva reserva){
    this.minhasReservas.remove(reserva);
}

    // alexandre tentando consertar os erros que o matheus falou
}



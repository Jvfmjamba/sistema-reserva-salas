package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class BancoDeDados {
    Persistente<Pessoa> pessoas = new Persistente<>();
    Persistente<Sala> salas = new Persistente<>();
    Persistente<Reserva> reservas = new Persistente<>();

    //adicionando getters e setters para pessoa, sala e reserva
    public Persistente<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(Persistente<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public Persistente<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(Persistente<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Persistente<Sala> getSalas() {
        return salas;
    }

    public void setSalas(Persistente<Sala> salas) {
        this.salas = salas;
    }
}

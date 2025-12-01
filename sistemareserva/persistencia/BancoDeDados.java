package sistemareserva.persistencia;

import sistemareserva.modelo.*;
import java.util.*;

public class BancoDeDados {

    static Persistente<Pessoa> pessoas = new Persistente<>("persistencia/pessoas.db");
    static Persistente<Sala> salas = new Persistente<>("persistencia/salas.db");
    Persistente<Reserva> reservas = new Persistente<>("persistencia/reservas.db");

    public BancoDeDados() {}

    public void salvarReserva(Reserva reserva) {
        reservas.inserir(reserva); // Persistente já salva automaticamente
    }

    public Reserva buscarReserva(int id) {
        for (Reserva r : reservas.listarTodos()) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public List<Reserva> getReservas2() {
        return reservas.listarTodos();
    }

    public static Persistente<Pessoa> getPessoas() {
        return pessoas;
    }

    public Persistente<Reserva> getReservas() {
        return reservas;
    }

    public static Persistente<Sala> getSalas() {
        return salas;
    }

    public static void carregarDadosIniciais() {

        if (getPessoas().listarTodos().isEmpty()) {
            getPessoas().inserir(new Pessoa("Professor Girafales"));
            getPessoas().inserir(new Pessoa("Dona Florinda"));
            getPessoas().inserir(new Pessoa("Seu Madruga"));
            getPessoas().inserir(new Pessoa("Professor Matheus"));
        }

        if (getSalas().listarTodos().isEmpty()) {
            getSalas().inserir(new Sala("CCOMP", 50, true, true, false, true));
            getSalas().inserir(new Sala("CCOMP", 30, true, false, false, true));
            getSalas().inserir(new Sala("CCOMP", 100, true, true, true, true));
            getSalas().inserir(new Sala("CCOMP", 10, true, false, false, false));
            getSalas().inserir(new Sala("ZOOTEC", 25, false, false, false, false));
            getSalas().inserir(new Sala("ARQUIT", 40, true, true, false, true));
        }
    }

    public Pessoa buscarPessoa(int id) {
        for (Pessoa p : pessoas.listarTodos()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public Sala buscarSala(int id) {
        for (Sala s : salas.listarTodos()) {
            if (s.getId() == id) return s;
        }
        return null;
    }
}

package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;
import sistemareserva.visao.*;

public class BancoDeDados {
    static Persistente<Pessoa> pessoas = new Persistente<>();
    static Persistente<Sala> salas = new Persistente<>();
    Persistente<Reserva> reservas = new Persistente<>();

    //(Julia) mais de uma sala na mesma reserva

    public void salvarReserva(Reserva reserva) {    
        reservas.inserir(reserva);  //talvez esse inserir de problema, mas n da pra usar add pq não é uma lista
    }

    public Reserva buscarReserva(int id) {
        for (Reserva r : reservas.listarTodos()) {
            if (r.getId() == id) return r;
        }
        return null;
    }

     public List<Reserva> getReservas2() {    //novo get reservas
        return reservas.listarTodos();
    } 
//-------------------------------------------------------------------------
    //adicionando getters e setters para pessoa, sala e reserva
    public static Persistente<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(Persistente<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public Persistente<Reserva> getReservas() {return reservas;}

    public void setReservas(Persistente<Reserva> reservas) {
        this.reservas = reservas;
    }

    public static Persistente<Sala> getSalas() {
        return salas;
    }

    public void setSalas(Persistente<Sala> salas) {
        this.salas = salas;
    }


    // ... (carregarDadosIniciais, exibirMenuPrincipal e cadastrarPessoa continuam iguais) ...
    public static void carregarDadosIniciais() {
        System.out.println("Inicializando sistema e carregando dados...");
        getPessoas().inserir(new Pessoa("Professor Girafales"));
        getPessoas().inserir(new Pessoa("Dona Florinda"));
        getPessoas().inserir(new Pessoa("Seu Madruga"));
        getPessoas().inserir(new Pessoa("Professor Matheus"));

        getSalas().inserir(new Sala("CCOMP", 50, true, true, false, true));
        getSalas().inserir(new Sala("CCOMP", 30, true, false, false, true));
        getSalas().inserir(new Sala("CCOMP", 100, true, true, true, true));
        getSalas().inserir(new Sala("CCOMP", 10, true, false, false, false));
        getSalas().inserir(new Sala("ZOOTEC", 25, false, false, false, false));
        getSalas().inserir(new Sala("ARQUIT", 40, true, true, false, true));

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

package sistemareserva.modelo;
import java.util.*;

public class Sala extends Entidade {
    protected String predio;
    protected int capacidade;
    //recursos:
    public boolean quadro;
    public boolean projetor;
    public boolean computador;
    public boolean arCondicionado;

    public String getPredio() {
        return predio;
    }
    public void setPredio(String predio) {
        this.predio = predio;
    }

    public int getCapacidade() {
        return capacidade;
    }
    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public Sala() {
        predio = null;
        capacidade = 0;
        quadro = projetor = computador = arCondicionado = false;
    }

    public Sala(String predio, int capacidade, boolean quadro, boolean projetor, boolean computador, boolean arCondicionado){
        this.predio = predio;
        this.capacidade = capacidade;
        this.quadro = quadro;
        this.projetor = projetor;
        this.computador = computador;
        this.arCondicionado = arCondicionado;
    }

    public boolean temQuadro() {
        return quadro;
    }

    public boolean temProjetor() {
        return projetor;
    }

    public boolean temComputador() {
        return computador;
    }

    public boolean temArCondicionado() {
        return arCondicionado;
    }


    @Override
    public String toString() {
        return "Sala Número=" + id + ", Prédio=" + predio + ", Capacidade=" + capacidade + " pessoas";
    }
}
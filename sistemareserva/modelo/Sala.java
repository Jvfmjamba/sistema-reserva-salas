package sistemareserva.modelo;
import java.util.*;

public class Sala extends Entidade {
    protected String predio;
    protected int capacidade;

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
    }

    public Sala(String predio, int capacidade){
        this.predio = predio;
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return "Id sala: " + id + ", Prédio:" + predio + ", Capacidade: " + capacidade + " pessoas";
    }
}
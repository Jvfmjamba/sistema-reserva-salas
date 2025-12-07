package sistemareserva.modelo;
import java.time.*;
import java.util.List;
import java.util.ArrayList;
//armazena as infos das reservas

public class Reserva extends Entidade{
    private Pessoa responsavel;
    //private LocalDateTime dataHoraInicio;
    //private LocalDateTime dataHoraFim;

    private List<ItemReserva> itensDaReserva;

    public Reserva(){
        this.responsavel =null;
        this.itensDaReserva =new ArrayList<>();
    }

    public Reserva(Pessoa responsavel){
        this.responsavel =responsavel;
        this.itensDaReserva =new ArrayList<>();
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public List<ItemReserva> getItensDaReserva() {
        return itensDaReserva;
    }


    public void adicionarItem(ItemReserva item) {
        this.itensDaReserva.add(item);
    }

    public void removerItem(ItemReserva item) {
        this.itensDaReserva.remove(item);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Reserva ID: ").append(getId());
        builder.append(" | Responsável: ").append(responsavel.getNome()).append("\n");
        builder.append("Itens da Reserva:\n");
        
        for (ItemReserva item : itensDaReserva) {
            builder.append("  - ").append(item.toString()).append("\n");
        }
        return builder.toString();
    }

}
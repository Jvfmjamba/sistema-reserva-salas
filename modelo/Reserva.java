package sistemareserva.modelo;
import java.time.*;
import java.util.List;
import java.util.ArrayList;
//armazena as infos das reservas

public class Reserva extends Entidade{
    private Pessoa responsavel;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    private List<ItemReserva> itensDaReserva;

    public Reserva(){
        dataHoraInicio = null;
        dataHoraFim = null;
        this.itensDaReserva = new ArrayList<>();
    }

    public Reserva(Pessoa responsavel, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){
        this.responsavel = responsavel;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        //array de lista de salas a serem reservadas
        this.itensDaReserva = new ArrayList<>();
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
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
}
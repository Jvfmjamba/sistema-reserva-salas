package sistemareserva.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ItemReserva{
    private Sala sala;

    //dataHoraInicio e dataHoraFim removido de de Reserva.java, agora esta aqui em ItemReserva.java

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    public ItemReserva(Sala sala,LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){
        this.sala=sala;
        this.dataHoraInicio=dataHoraInicio;
        this.dataHoraFim=dataHoraFim;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    //getter e setter pra DataHoraInicio e DataHoraFim

    public LocalDateTime getDataHoraInicio(){
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim(){
        return dataHoraFim;
    }
    
    @Override
    public String toString() {
        return "Item da Reserva Sala ID=" + sala.getId();
    }

}
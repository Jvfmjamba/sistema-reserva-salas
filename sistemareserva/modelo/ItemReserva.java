package sistemareserva.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ItemReserva{
    //private Sala sala = new Sala();
    private Sala sala;

    //alexandre tirou dataHoraInicio e dataHoraFim de Reserva.java e trouxe aqui pra ItemReserva.java

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    //alexandre tirou dataHoraInicio e dataHoraFim de Reserva.java e trouxe aqui pra ItemReserva.java

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

    //alexandre adicionei getter e setter pra DataHoraInicio e DataHoraFim

    public LocalDateTime getDataHoraInicio(){
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim(){
        return dataHoraFim;
    }

    //alexandre adicionei getter e setter pra DataHoraInicio e DataHoraFim

    
    @Override
    public String toString() {
        return "Item da Reserva Sala ID=" + sala.getId();
    }

}
package sistemareserva.modelo;

public class ItemReserva{
    private Sala sala = new Sala();

    public ItemReserva(Sala sala) {
        this.sala = sala;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    @Override
    public String toString() {
        return "Item da Reserva Sala ID=" + sala.getId();
    }

}
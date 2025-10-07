package sistemareserva.modelo;

public class Entidade {
    protected int id;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public Entidade(){
        id = 0;
    }

    public Entidade(int id){
        this.id = id;
    }
}

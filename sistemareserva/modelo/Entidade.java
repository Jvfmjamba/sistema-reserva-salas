package sistemareserva.modelo;

public abstract class Entidade {
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

    //tostring que tava faltando em entidade
    @Override
    public String toString(){
        return String.valueOf(id);
    }
}

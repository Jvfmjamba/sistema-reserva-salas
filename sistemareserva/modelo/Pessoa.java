package sistemareserva.modelo;

public class Pessoa extends Entidade {
    //Entidade matricula = new Entidade();
    protected String nome;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pessoa(){
        nome = null;
    }

    public Pessoa(String nome){
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Pessoa - Matricula = " + id + ", Nome= " + nome;
    }
}

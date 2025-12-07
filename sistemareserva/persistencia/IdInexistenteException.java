package sistemareserva.persistencia;

public class IdInexistenteException extends Exception {
    public IdInexistenteException(String mensagem) {
        super(mensagem);
    }
}

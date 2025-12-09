package sistemareserva.persistencia;

//exceção de digitar número nos nomes (de pessoa e sala)
public class EntradaInvalidaException extends Exception {
    public EntradaInvalidaException(String mensagem) {
        super(mensagem);
    }
}

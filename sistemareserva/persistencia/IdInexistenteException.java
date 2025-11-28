package sistemareserva.persistencia;

//alexandre criaçao da excecao de id inexistente

public class IdInexistenteException extends Exception {
    public IdInexistenteException(String mensagem) {
        super(mensagem);
    }
}

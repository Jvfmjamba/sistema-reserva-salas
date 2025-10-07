package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    public List<T> lista;
    //atributo estatico do proximo id:
    private static int proxId =10000;

    //construtor
    public Persistente(){      //construtor
        this.lista = new ArrayList<>();
    }

    public void inserir(T novoObjeto) {
        novoObjeto.setId(proxId++);
        this.lista.add(novoObjeto);
    }

    public boolean excluir(int id) {
        return lista.removeIf(objeto-> objeto.getId() ==id);
    }

    public T buscaId(int id) {
        for(T objeto : lista) {
            if(objeto.getId() == id) {
                return objeto;
            }
        }
        return null;
    }

    public List<T> listarTodos() {
        // Retorna uma CÓPIA da lista, para proteger a lista original de modificações externas.
        return new ArrayList<>(this.lista);
    }
}
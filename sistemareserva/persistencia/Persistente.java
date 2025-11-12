package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    //public List<T> lista; mudei "List" para private:
    private List<T> lista;
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

    // alexandre adicionando toString no Persistente.java
    @Override
    public String toString(){
        if(lista.isEmpty()){
            return "nenhum item cadastrado";
        }
        StringBuilder builder =new StringBuilder();
        for(T item : lista){
            builder.append(item.toString()).append("\n");
        }

        return builder.toString();
    }

    // alexandre adicionando toString no Persistente.java

    //*********************************************************************************************** 

    //alexandre adicionando o método Alterar, nas especificacoes ele fala que tem que ter métodos pra inserção, alteração e exclusão, a gente só tem os que insere e exclui

    public boolean alterar(T objetoModificado){
    for(int i=0; i<lista.size();i++){
        if(lista.get(i).getId() ==objetoModificado.getId()){
            lista.set(i, objetoModificado);
            return true;
        }
    }
    return false;
    }

    //alexandre adicionando o método Alterar, nas especificacoes ele fala que tem que ter métodos pra inserção, alte ração e exclusão, a gente só tem os que insere e exclui

}
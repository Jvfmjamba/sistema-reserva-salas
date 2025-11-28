package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    //public List<T> lista; mudei "List" para private:
    private List<T> lista;
    //atributo estatico do proximo id:
    private static int proxId =10;

    //construtor
    public Persistente(){      //construtor
        this.lista = new ArrayList<>();
    }

    //alexandre alterando funcoes existentes e criando novas para retornar boolena, e funcionar os testes de unidade

    /*
    public void inserir(T novoObjeto) {
        novoObjeto.setId(proxId++);
        this.lista.add(novoObjeto);
    }*/

    public boolean inserir(T novoObjeto) {
        // verifica se o objeto tem um id
        if (novoObjeto.getId() != 0) {
            // se sim, verifica se ele existe na lista
            for (T obj : lista) {
                if (obj.getId() == novoObjeto.getId()) {
                    return false; //se o id ja existir, nao insere, caso do id duplicado
                }
            }
            // se ele existe mas nao esta na lista, adicionamos o id dele
            this.lista.add(novoObjeto);
            return true;
        }
        // caso em que nao tem id, ai geramos um novo
        novoObjeto.setId(proxId++);
        this.lista.add(novoObjeto);
        return true;
    }

    public boolean excluir(int id) {
        return lista.removeIf(objeto-> objeto.getId() ==id);
    }

    public T buscaId(int id) throws IdInexistenteException{
        for(T objeto : lista) {
            if(objeto.getId() == id) {
                return objeto;
            }
        }
        //alexandre aqui lançando a excessao, caso ele percorra tudo e nao ache:
        throw new IdInexistenteException("Entidade com ID " + id + " não encontrada.");
    }

    public List<T> listarTodos() {
        // Retorna uma CÓPIA da lista, para proteger a lista original de modificações externas.
        return new ArrayList<>(this.lista);
    }

    // alexandre adicionando toString no Persistente.java
    @Override
    public String toString(){
        if(lista.isEmpty()){
            return "Nenhum item cadastrado";
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
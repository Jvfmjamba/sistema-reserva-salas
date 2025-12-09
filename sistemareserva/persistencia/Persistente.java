package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    private List<T> lista;
    private static int proxId =10;

    public Persistente(){
        this.lista = new ArrayList<>();
    }

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

    // Exclui o objeto cujo ID corresponde ao fornecido.
    // Retorna true se encontrou e removeu, false caso contrário.
    public boolean excluir(int id) {
        return lista.removeIf(objeto-> objeto.getId() ==id);
    }

    // Busca um objeto pelo ID percorrendo a lista.
    // Se encontrar, retorna o objeto.
    // Se percorrer tudo e não achar, lança a exceção IdInexistenteException.
    public T buscaId(int id) throws IdInexistenteException{
        for(T objeto : lista) {
            if(objeto.getId() == id) {
                return objeto;
            }
        }
        throw new IdInexistenteException("Entidade com ID " + id + " não encontrada.");
    }

    // Retorna uma cópia da lista interna para evitar que ela seja alterada externamente.
    public List<T> listarTodos() {
        return new ArrayList<>(this.lista);
    }

    // Retorna uma string com todos os itens da lista, um por linha.
    @Override
    public String toString(){
        if(lista.isEmpty()){
            return "Nenhum item cadastrado";    //lista vazia
        }
        StringBuilder builder =new StringBuilder();
        for(T item : lista){
            builder.append(item.toString()).append("\n");
        }

        return builder.toString();
    }

    // Procura na lista um objeto com o mesmo ID do objeto modificado.
    // Se encontrar, substitui pelo objeto novo e retorna true.
    // Se não encontrar, retorna false.
    public boolean alterar(T objetoModificado){
    for(int i=0; i<lista.size();i++){
        if(lista.get(i).getId() ==objetoModificado.getId()){
            lista.set(i, objetoModificado);
            return true;
        }
    }
    return false;
    }


}
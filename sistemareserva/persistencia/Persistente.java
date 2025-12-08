package sistemareserva.persistencia;
import java.util.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    //public List<T> lista; mudei "List" para private:
    private List<T> lista;
    //atributo estatico do proximo id:
    private static int proxId =10;

    //construtor
    public Persistente(){
        this.lista = new ArrayList<>();
    }

    //alexandre alterando funcoes existentes e criando novas para retornar boolena, e funcionar os testes de unidade


    // Insere um novo objeto.
    // Se o objeto já tem ID, verifica se é duplicado.
    // Se o ID já existir na lista, não insere e retorna false.
    // Se o ID não existir, adiciona normalmente.
    // Se o objeto não tem ID (id = 0), gera um novo ID automaticamente e adiciona.
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
        //alexandre aqui lançando a excessao, caso ele percorra tudo e nao ache:
        throw new IdInexistenteException("Entidade com ID " + id + " não encontrada.");
    }

    // Retorna uma cópia da lista interna para evitar que ela seja alterada externamente.
    public List<T> listarTodos() {
        // retorna uma copia da lista, para proteger a lista original de modificações externas
        return new ArrayList<>(this.lista);
    }

    //toString adicionado no Persistente.java
    // Retorna uma string com todos os itens da lista, um por linha.
    // Se a lista estiver vazia, retorna a mensagem "Nenhum item cadastrado".
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
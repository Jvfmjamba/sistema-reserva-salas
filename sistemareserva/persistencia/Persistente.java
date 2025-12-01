package sistemareserva.persistencia;
import java.util.*;
import java.io.*;
import sistemareserva.modelo.*;

public class Persistente<T extends Entidade> {
    private List<T> lista;
    private static int proxId = 10;

    private File arquivo;  // Marcus: parte de salvar no arquivo

    public Persistente(String nomeArquivo) {
        this.lista = new ArrayList<>();
        this.arquivo = new File(nomeArquivo);  // Marcus: parte de salvar no arquivo
        carregarArquivo();  // Marcus: parte de salvar no arquivo
    }

    public boolean inserir(T novoObjeto) {
        if (novoObjeto.getId() != 0) {
            for (T obj : lista) {
                if (obj.getId() == novoObjeto.getId()) {
                    return false;
                }
            }
            this.lista.add(novoObjeto);
            salvarArquivo(); // Marcus: parte de salvar no arquivo
            return true;
        }
        novoObjeto.setId(proxId++);
        this.lista.add(novoObjeto);
        salvarArquivo(); // Marcus: parte de salvar no arquivo
        return true;
    }

    public boolean excluir(int id) {
        boolean removido = lista.removeIf(objeto -> objeto.getId() == id);
        if (removido) salvarArquivo(); // Marcus: parte de salvar no arquivo
        return removido;
    }

    public boolean excluir(T objeto) {
        boolean removido = lista.remove(objeto);
        if (removido) salvarArquivo(); // Marcus: parte de salvar no arquivo
        return removido;
    }

    public T buscaId(int id) throws IdInexistenteException {
        for (T objeto : lista) {
            if (objeto.getId() == id) {
                return objeto;
            }
        }
        throw new IdInexistenteException("Entidade com ID " + id + " não encontrada.");
    }

    public List<T> listarTodos() {
        return new ArrayList<>(this.lista);
    }

    public void setLista(List<T> novaLista) {
        this.lista = novaLista;
        salvarArquivo(); // Marcus: parte de salvar no arquivo
    }

    @Override
    public String toString() {
        if (lista.isEmpty()) {
            return "Nenhum item cadastrado";
        }
        StringBuilder builder = new StringBuilder();
        for (T item : lista) {
            builder.append(item.toString()).append("\n");
        }
        return builder.toString();
    }

    public boolean alterar(T objetoModificado) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == objetoModificado.getId()) {
                lista.set(i, objetoModificado);
                salvarArquivo(); // Marcus: parte de salvar no arquivo
                return true;
            }
        }
        return false;
    }

    // MÉTODOS DE ARQUIVO
    @SuppressWarnings("unchecked")
    private void carregarArquivo() {
        try {
            // Marcus: parte de salvar no arquivo
            arquivo.getParentFile().mkdirs();  // garante a pasta

            if (!arquivo.exists()) return;

            FileInputStream fis = new FileInputStream(arquivo);
            ObjectInputStream ois = new ObjectInputStream(fis);

            this.lista = (List<T>) ois.readObject();
            ois.close();

            for (T obj : lista) {
                if (obj.getId() >= proxId) {
                    proxId = obj.getId() + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar arquivo: " + e.getMessage());
        }
    }


    private void salvarArquivo() {
        try {
            // Marcus: parte de salvar no arquivo
            arquivo.getParentFile().mkdirs();  // cria pasta persistencia se não existir

            FileOutputStream fos = new FileOutputStream(arquivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(lista);
            oos.close();

        } catch (Exception e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
    // Marcus: método que o BancoDeDados espera
    public List<T> listar() {
        return listarTodos();
    }

}

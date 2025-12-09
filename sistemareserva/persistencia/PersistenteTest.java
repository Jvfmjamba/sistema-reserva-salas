package sistemareserva.persistencia;

//alexandre criacao da classe de teste do jnuit
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import sistemareserva.modelo.Pessoa;

public class PersistenteTest {

    private Persistente<Pessoa> bancoPessoas;

    // metodo que limpa o banco antes de rodar cada teste
    //executa antes de cada teste, reinicializa o banco para garantir testes independentes

    @Before
    public void setup() {
        bancoPessoas = new Persistente<>();
    }

    // testa inserir uma pessoa nova que ainda não tem ID, deve retornar true
    @Test
    public void testInserirEntidadeIdNaoExistente() {
        Pessoa p = new Pessoa("Teste Inserir");
        boolean resultado = bancoPessoas.inserir(p);
        assertTrue("Deveria retornar TRUE ao inserir novo item", resultado);
        try {
            // VERIIFCA SE TA LA
            Pessoa buscada = bancoPessoas.buscaId(p.getId());
            assertEquals("O nome deve ser o mesmo", "Teste Inserir", buscada.getNome());
        } catch (IdInexistenteException e) {
            // se cair aqui é pq o etste falhou
            assertTrue("Não deveria lançar exceção aqui", false);
        }
    }

    //testa inserir uma Pessoa usando manualmente um ID que já existe, deve falhar e retornar false
    @Test
    public void testInserirEntidadeIdJaExistente() {
        Pessoa p1 = new Pessoa("Pessoa Original");
        bancoPessoas.inserir(p1);
        int idExistente = p1.getId();
        Pessoa p2 = new Pessoa("Pessoa Duplicada");
        p2.setId(idExistente);
        boolean resultado = bancoPessoas.inserir(p2);
        assertFalse("Deveria retornar FALSE ao tentar inserir ID duplicado", resultado);
    }

    // testa alterar uma pessoa que já está no banco
    @Test
    public void testAlterarIdExistente() {
        Pessoa p = new Pessoa("Nome Antigo");
        bancoPessoas.inserir(p);
        p.setNome("Nome Novo");
        boolean resultado = bancoPessoas.alterar(p);
        assertTrue("Deveria retornar TRUE ao alterar item existente", resultado);
        try {
            Pessoa buscada = bancoPessoas.buscaId(p.getId());
            assertEquals("Nome Novo", buscada.getNome());
        } catch (Exception e) {
            assertTrue("Erro ao buscar", false);
        }
    }

    // testa alterar uma pessoa que não existe no banco, retorna false
    @Test
    public void testAlterarIdNaoExistente() {
        Pessoa p = new Pessoa("Fantasma");
        p.setId(9999); 
        boolean resultado = bancoPessoas.alterar(p);
        assertFalse("Deveria retornar FALSE ao tentar alterar ID inexistente", resultado);
    }

    // testa excluir uma Pessoa já cadastrada, deve retornar true
    @Test
    public void testApagarIdExistente() {
        Pessoa p = new Pessoa("Para Apagar");
        bancoPessoas.inserir(p);
        int id = p.getId();
        boolean resultado = bancoPessoas.excluir(id);
        assertTrue("Deveria retornar TRUE ao apagar item existente", resultado);
        assertThrows(IdInexistenteException.class, () -> {
            bancoPessoas.buscaId(id);
        });
    }

    //testa excluir um ID que não existe no banco, deve retornar false
    @Test
    public void testApagarIdNaoExistente() {
        boolean resultado = bancoPessoas.excluir(8888);
        assertFalse("Deveria retornar FALSE ao tentar apagar ID inexistente", resultado);
    }

    // testa buscar uma pessoa ja cadastrada
    @Test
    public void testBuscarIdExistente() {
        Pessoa p = new Pessoa("Achado");
        bancoPessoas.inserir(p);
        try {
            Pessoa encontrada = bancoPessoas.buscaId(p.getId());
            assertEquals(p.getId(), encontrada.getId());
        } catch (IdInexistenteException e) {
            assertTrue("Não deveria dar erro", false);
        }
    }

    // testa buscar ID que não existe, lança IdInexistenteException
    @Test
    public void testBuscarIdNaoExistente() {
        assertThrows(IdInexistenteException.class, () -> {
            bancoPessoas.buscaId(7777);
        });
    }
}
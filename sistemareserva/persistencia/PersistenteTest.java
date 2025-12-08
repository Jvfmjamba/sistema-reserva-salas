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
    // Executa antes de cada teste. Reinicializa o banco para garantir testes independentes.

    @Before
    public void setup() {
        bancoPessoas = new Persistente<>();
    }

    // TESTES DE INSERIR, COM ID EXISTENTE E COM ID INXISTENTE
    // Testa inserir uma Pessoa nova que ainda não tem ID.
    // Deve retornar TRUE e permitir buscá-la pelo ID recém-gerado.

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

    // Testa inserir uma Pessoa usando manualmente um ID que já existe.
    // Inserção deve falhar e retornar FALSE.

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

    //  TESTES DE ALTERAR COM ID EXISTENTE E COM ID INEXISTENTE
    // Testa alterar uma Pessoa que já está no banco.
    // Deve retornar TRUE e o nome atualizado deve ser encontrado no banco.
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

    // Testa alterar uma Pessoa que não existe no banco.
    // Deve retornar FALSE porque o ID não foi encontrado.
    @Test
    public void testAlterarIdNaoExistente() {
        Pessoa p = new Pessoa("Fantasma");
        p.setId(9999); 
        boolean resultado = bancoPessoas.alterar(p);
        assertFalse("Deveria retornar FALSE ao tentar alterar ID inexistente", resultado);
    }

    // TESTES DE APAGAR COM ID EXISTENTE E OCM ID INEXISTENTE
    // Testa excluir uma Pessoa já cadastrada.
    // Deve retornar TRUE e a busca pelo ID deve lançar IdInexistenteException.
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

    // Testa excluir um ID que não existe no banco.
    // Deve retornar FALSE.
    @Test
    public void testApagarIdNaoExistente() {
        boolean resultado = bancoPessoas.excluir(8888);
        assertFalse("Deveria retornar FALSE ao tentar apagar ID inexistente", resultado);
    }

    //  TESTES DE BUSCAR (2 Casos) COM ID EXISTENTE E COM ID INEXISTENTE
// Testa buscar uma Pessoa já cadastrada.
// Deve encontrar a pessoa e retornar seu ID corretamente.

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

    // Testa buscar ID que não existe.
    // A função deve lançar IdInexistenteException.

    @Test
    public void testBuscarIdNaoExistente() {
        assertThrows(IdInexistenteException.class, () -> {
            bancoPessoas.buscaId(7777);
        });
    }
}
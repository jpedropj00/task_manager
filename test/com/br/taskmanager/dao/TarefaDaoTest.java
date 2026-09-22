package com.br.taskmanager.dao;

import com.br.taskmanager.database.ConnectionFactory;
import com.br.taskmanager.database.DatabaseInitializer;
import com.br.taskmanager.models.Tarefa;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testa o DAO contra um SQLite de verdade, num arquivo temporário. */
class TarefaDaoTest {
    @TempDir
    Path pastaTemporaria;

    private Path arquivoBanco;
    private TarefaDao dao;

    @BeforeEach
    void setUp() {
        arquivoBanco = pastaTemporaria.resolve("subpasta").resolve("teste.db");
        System.setProperty(ConnectionFactory.PROPRIEDADE_CAMINHO, arquivoBanco.toString());
        DatabaseInitializer.initDatabase();
        dao = new TarefaDao();
    }

    @AfterEach
    void tearDown() {
        System.clearProperty(ConnectionFactory.PROPRIEDADE_CAMINHO);
    }

    @Test
    void criaOArquivoDoBancoEAsPastasQueFaltam() {
        assertTrue(Files.exists(arquivoBanco));
    }

    @Test
    void inicializarDuasVezesNaoDaErro() {
        assertDoesNotThrow(DatabaseInitializer::initDatabase);
    }

    @Test
    void salvaEBuscaPorIdComTodosOsCampos() {
        Tarefa tarefa = new Tarefa(UUID.randomUUID(), "Estudar SQLite", "Criar o DAO", true);
        dao.salvar(tarefa);

        Tarefa lida = dao.buscarPorId(tarefa.getId()).orElseThrow();

        assertEquals(tarefa.getId(), lida.getId());
        assertEquals("Estudar SQLite", lida.getTitulo());
        assertEquals("Criar o DAO", lida.getDescricao());
        assertTrue(lida.getStatus());
    }

    @Test
    void preservaAcentosEDescricaoVazia() {
        Tarefa tarefa = new Tarefa("Revisão de ação", "");
        dao.salvar(tarefa);

        Tarefa lida = dao.buscarPorId(tarefa.getId()).orElseThrow();

        assertEquals("Revisão de ação", lida.getTitulo());
        assertEquals("", lida.getDescricao());
    }

    @Test
    void buscarPorIdInexistenteDevolveVazio() {
        assertEquals(Optional.empty(), dao.buscarPorId(UUID.randomUUID()));
    }

    @Test
    void buscarTodosDevolveTodasAsTarefas() {
        dao.salvar(new Tarefa("A", ""));
        dao.salvar(new Tarefa("B", ""));

        List<Tarefa> todas = dao.buscarTodos();

        assertEquals(2, todas.size());
    }

    @Test
    void atualizarAlteraOsCamposEDevolveTrue() {
        Tarefa tarefa = new Tarefa("Antes", "descrição");
        dao.salvar(tarefa);

        boolean atualizou = dao.atualizar(new Tarefa(tarefa.getId(), "Depois", "nova", true));

        Tarefa lida = dao.buscarPorId(tarefa.getId()).orElseThrow();
        assertTrue(atualizou);
        assertEquals("Depois", lida.getTitulo());
        assertEquals("nova", lida.getDescricao());
        assertTrue(lida.getStatus());
    }

    @Test
    void atualizarInexistenteDevolveFalse() {
        assertFalse(dao.atualizar(new Tarefa("Fantasma", "")));
    }

    @Test
    void excluirRemoveEDevolveTrueSoNaPrimeiraVez() {
        Tarefa tarefa = new Tarefa("Remover", "");
        dao.salvar(tarefa);

        assertTrue(dao.excluir(tarefa));
        assertFalse(dao.excluir(tarefa));
        assertTrue(dao.buscarTodos().isEmpty());
    }
}

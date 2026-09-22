package com.br.taskmanager.services;

import com.br.taskmanager.exceptions.TarefaNaoEncontradaException;
import com.br.taskmanager.models.FiltroTarefa;
import com.br.taskmanager.models.OrdenacaoTarefa;
import com.br.taskmanager.models.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarefaServiceTest {
    private TarefaDaoEmMemoria dao;
    private TarefaService service;

    @BeforeEach
    void setUp() {
        dao = new TarefaDaoEmMemoria();
        service = new TarefaService(dao);
    }

    private static Tarefa copiaCom(Tarefa tarefa, String titulo, boolean status) {
        return new Tarefa(tarefa.getId(), titulo, tarefa.getDescricao(), status);
    }

    @Nested
    class Salvar {

        @Test
        void salvaTarefaNova() {
            Tarefa tarefa = new Tarefa("Estudar", "");

            service.salvarTarefa(tarefa);

            assertEquals(List.of(tarefa), service.listarTarefas());
        }

        @Test
        void bloqueiaTituloDuplicadoIgnorandoMaiusculasEEspacos() {
            service.salvarTarefa(new Tarefa("Estudar Java", ""));

            IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                    () -> service.salvarTarefa(new Tarefa("  ESTUDAR java  ", "")));

            assertEquals("Já existe uma tarefa com o título \"ESTUDAR java\".", erro.getMessage());
            assertEquals(1, service.listarTarefas().size());
        }

        @Test
        void comparaAcentosSemDiferenciarMaiusculas() {
            service.salvarTarefa(new Tarefa("Ação", ""));

            assertThrows(IllegalArgumentException.class, () -> service.salvarTarefa(new Tarefa("AÇÃO", "")));
            assertDoesNotThrow(() -> service.salvarTarefa(new Tarefa("Acao", "")));
        }

        @Test
        void rejeitaTarefaNula() {
            assertThrows(IllegalArgumentException.class, () -> service.salvarTarefa(null));
        }
    }

    @Nested
    class Atualizar {
        private Tarefa estudar;
        private Tarefa ler;

        @BeforeEach
        void cadastrar() {
            estudar = new Tarefa("Estudar", "cap 1");
            ler = new Tarefa("Ler", "");
            service.salvarTarefa(estudar);
            service.salvarTarefa(ler);
        }

        @Test
        void concluiTarefa() throws Exception {
            service.atualizarTarefa(copiaCom(estudar, "Estudar", true));

            assertTrue(dao.buscarPorId(estudar.getId()).orElseThrow().getStatus());
        }

        @Test
        void bloqueiaRenomearParaTituloDeOutraTarefa() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.atualizarTarefa(copiaCom(ler, "estudar", false)));

            assertEquals("Ler", dao.buscarPorId(ler.getId()).orElseThrow().getTitulo());
        }

        @Test
        void permiteMudarSoMaiusculasDoProprioTitulo() throws Exception {
            service.atualizarTarefa(copiaCom(estudar, "ESTUDAR", false));

            assertEquals("ESTUDAR", dao.buscarPorId(estudar.getId()).orElseThrow().getTitulo());
        }

        @Test
        void naoBloqueiaTarefaQueJaTinhaDuplicataAntiga() throws Exception {
            // Duplicatas gravadas antes da regra existir não podem travar a tarefa.
            Tarefa antiga = new Tarefa("estudar", "duplicata antiga");
            dao.inserirSemValidar(antiga);

            service.atualizarTarefa(copiaCom(antiga, "estudar", true));

            assertTrue(dao.buscarPorId(antiga.getId()).orElseThrow().getStatus());
        }

        @Test
        void lancaNaoEncontradaParaTarefaInexistente() {
            TarefaNaoEncontradaException erro = assertThrows(TarefaNaoEncontradaException.class,
                    () -> service.atualizarTarefa(new Tarefa("Fantasma", "")));

            assertEquals("Tarefa não encontrada. Ela pode ter sido removida.", erro.getMessage());
        }
    }

    @Nested
    class Excluir {

        @Test
        void excluiTarefa() throws Exception {
            Tarefa tarefa = new Tarefa("Estudar", "");
            service.salvarTarefa(tarefa);

            service.excluirTarefa(tarefa);

            assertTrue(service.listarTarefas().isEmpty());
        }

        @Test
        void lancaNaoEncontradaAoExcluirDuasVezes() throws Exception {
            Tarefa tarefa = new Tarefa("Estudar", "");
            service.salvarTarefa(tarefa);
            service.excluirTarefa(tarefa);

            assertThrows(TarefaNaoEncontradaException.class, () -> service.excluirTarefa(tarefa));
        }
    }

    @Nested
    class Organizar {
        private List<Tarefa> tarefas;

        @BeforeEach
        void montar() {
            tarefas = List.of(
                    new Tarefa("Zebra", ""),
                    new Tarefa(UUID.randomUUID(), "Ávila", "", true),
                    new Tarefa("bola", ""));
        }

        @Test
        void filtraEOrdena() {
            List<Tarefa> pendentes = service.organizar(tarefas, FiltroTarefa.PENDENTES,
                    OrdenacaoTarefa.TITULO.getComparador());

            assertEquals(List.of("bola", "Zebra"), pendentes.stream().map(Tarefa::getTitulo).toList());
        }

        @Test
        void naoAlteraAListaRecebida() {
            List<Tarefa> resultado = service.organizar(tarefas, FiltroTarefa.TODAS,
                    OrdenacaoTarefa.TITULO.getComparador());

            assertEquals("Zebra", tarefas.get(0).getTitulo());
            assertEquals("Ávila", resultado.get(0).getTitulo());
        }

        @Test
        void aceitaFiltroEComparadorDeSupertipo() {
            // ? super Tarefa: critérios escritos para Object também servem.
            Predicate<Object> todos = obj -> true;
            Comparator<Object> porTexto = Comparator.comparing(Object::toString);

            List<Tarefa> resultado = service.organizar(tarefas, todos, porTexto);

            // Ordem natural de String: maiúscula sem acento ("Z") vem antes de "b" e "Á".
            assertEquals(List.of("Zebra", "bola", "Ávila"),
                    resultado.stream().map(Tarefa::getTitulo).toList());
        }
    }
}

package com.br.taskmanager.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiltroEOrdenacaoTest {
    private final Tarefa pendente = new Tarefa("Pendente", "");
    private final Tarefa concluida = new Tarefa(UUID.randomUUID(), "Concluída", "", true);

    @Test
    void filtroTodasAceitaQualquerTarefa() {
        assertTrue(FiltroTarefa.TODAS.test(pendente));
        assertTrue(FiltroTarefa.TODAS.test(concluida));
    }

    @Test
    void filtroPendentesEConcluidasSeparamPeloStatus() {
        assertTrue(FiltroTarefa.PENDENTES.test(pendente));
        assertFalse(FiltroTarefa.PENDENTES.test(concluida));
        assertTrue(FiltroTarefa.CONCLUIDAS.test(concluida));
        assertFalse(FiltroTarefa.CONCLUIDAS.test(pendente));
    }

    @Test
    void ordenacaoPorTituloRespeitaAcentosEIgnoraMaiusculas() {
        List<Tarefa> tarefas = tarefas("Zebra", "bola", "Ávila", "casa", "Água");

        tarefas.sort(OrdenacaoTarefa.TITULO.getComparador());

        assertEquals(List.of("Água", "Ávila", "bola", "casa", "Zebra"), titulos(tarefas));
    }

    @Test
    void pendentesPrimeiroDepoisPorTitulo() {
        List<Tarefa> tarefas = new ArrayList<>(List.of(
                new Tarefa(UUID.randomUUID(), "Alfa", "", true),
                new Tarefa("Charlie", ""),
                new Tarefa(UUID.randomUUID(), "Bravo", "", true),
                new Tarefa("Beta", "")));

        tarefas.sort(OrdenacaoTarefa.PENDENTES_PRIMEIRO.getComparador());

        assertEquals(List.of("Beta", "Charlie", "Alfa", "Bravo"), titulos(tarefas));
    }

    @Test
    void textosExibidosNosCombos() {
        assertEquals("Concluídas", FiltroTarefa.CONCLUIDAS.toString());
        assertEquals("Título (A-Z)", OrdenacaoTarefa.TITULO.toString());
    }

    private static List<Tarefa> tarefas(String... titulos) {
        List<Tarefa> lista = new ArrayList<>();
        for (String titulo : titulos) {
            lista.add(new Tarefa(titulo, ""));
        }
        return lista;
    }

    private static List<String> titulos(List<Tarefa> tarefas) {
        return tarefas.stream().map(Tarefa::getTitulo).toList();
    }
}

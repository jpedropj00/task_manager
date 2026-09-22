package com.br.taskmanager.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TarefaTest {

    @Test
    void novaTarefaNasceComIdEPendente() {
        Tarefa tarefa = new Tarefa("Estudar", "Capítulo 1");

        assertNotNull(tarefa.getId());
        assertFalse(tarefa.getStatus());
    }

    @Test
    void removeEspacosDoTituloEDaDescricao() {
        Tarefa tarefa = new Tarefa("  Estudar  ", "  Capítulo 1  ");

        assertEquals("Estudar", tarefa.getTitulo());
        assertEquals("Capítulo 1", tarefa.getDescricao());
    }

    @Test
    void descricaoNulaViraTextoVazio() {
        assertEquals("", new Tarefa("Estudar", null).getDescricao());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "\t\n"})
    void rejeitaTituloEmBranco(String titulo) {
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> new Tarefa(titulo, "descrição"));

        assertEquals("O título não pode ficar em branco.", erro.getMessage());
    }

    @Test
    void rejeitaIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Tarefa(null, "Estudar", "", false));
    }

    @Test
    void igualdadeEhPeloIdENaoPeloConteudo() {
        UUID id = UUID.randomUUID();
        Tarefa original = new Tarefa(id, "Estudar", "antes", false);
        Tarefa editada = new Tarefa(id, "Outro título", "depois", true);
        Tarefa homonima = new Tarefa("Estudar", "antes");

        assertEquals(original, editada);
        assertEquals(original.hashCode(), editada.hashCode());
        assertNotEquals(original, homonima);
        assertEquals(1, Set.of(original).size());
    }
}

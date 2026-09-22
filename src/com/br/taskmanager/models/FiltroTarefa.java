package com.br.taskmanager.models;

import java.util.function.Predicate;

public enum FiltroTarefa implements Predicate<Tarefa> {
    TODAS("Todas", tarefa -> true),
    PENDENTES("Pendentes", tarefa -> !tarefa.getStatus()),
    CONCLUIDAS("Concluídas", Tarefa::getStatus);

    private final String descricao;
    private final Predicate<Tarefa> criterio;

    FiltroTarefa(String descricao, Predicate<Tarefa> criterio) {
        this.descricao = descricao;
        this.criterio = criterio;
    }

    @Override
    public boolean test(Tarefa tarefa) {
        return criterio.test(tarefa);
    }

    @Override
    public String toString() {
        return descricao;
    }
}

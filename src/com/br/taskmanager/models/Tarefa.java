package com.br.taskmanager.models;

import java.util.Objects;
import java.util.UUID;

/**
 * Tarefa imutável: para alterar, cria-se uma nova instância com o mesmo id.
 * Assim uma edição só "acontece" depois que o service grava no banco.
 */
public final class Tarefa {
    private final UUID id;
    private final String titulo;
    private final String descricao;
    private final boolean status;

    public Tarefa(String titulo, String descricao) {
        this(UUID.randomUUID(), titulo, descricao, false);
    }

    public Tarefa(UUID id, String titulo, String descricao, boolean status) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título não pode ficar em branco.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.descricao = (descricao == null) ? "" : descricao.trim();
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    /** {@code true} quando a tarefa está concluída. */
    public boolean getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tarefa outra)) {
            return false;
        }
        return this.id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return titulo + " (" + (status ? "Concluída" : "Pendente") + ")";
    }
}

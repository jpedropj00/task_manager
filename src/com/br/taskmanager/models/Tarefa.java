package com.br.taskmanager.models;

import java.util.Objects;
import java.util.UUID;

public class Tarefa {
    private final UUID id;
    private String titulo;
    private String descricao;
    private boolean status;

    public Tarefa(String titulo, String descricao) {
        this(UUID.randomUUID(), titulo, descricao, false);
    }

    public Tarefa(UUID id, String titulo, String descricao, boolean status) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo.");
        }
        this.id = id;
        setTitulo(titulo);
        setDescricao(descricao);
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título não pode ficar em branco.");
        }
        this.titulo = titulo.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = (descricao == null) ? "" : descricao.trim();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        return "Título: " + this.titulo
                + "\nDescrição: " + (this.descricao.isEmpty() ? "(sem descrição)" : this.descricao)
                + "\nStatus: " + (this.status ? "Concluída" : "Pendente");
    }
}

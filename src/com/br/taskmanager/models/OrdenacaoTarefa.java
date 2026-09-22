package com.br.taskmanager.models;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public enum OrdenacaoTarefa {
    PENDENTES_PRIMEIRO("Pendentes primeiro",
            Comparator.comparing(Tarefa::getStatus).thenComparing(porTitulo())),
    TITULO("Título (A-Z)", porTitulo());

    private final String descricao;
    private final Comparator<Tarefa> comparador;

    OrdenacaoTarefa(String descricao, Comparator<Tarefa> comparador) {
        this.descricao = descricao;
        this.comparador = comparador;
    }

    public Comparator<Tarefa> getComparador() {
        return comparador;
    }

    @Override
    public String toString() {
        return descricao;
    }

    /**
     * Ordena pelo título respeitando acentos do português ("Ávila" antes de "Bola").
     *
     * O Collator é um {@code Comparator<Object>}, e mesmo assim serve para comparar
     * Strings: {@code Comparator.comparing} declara o comparador da chave como
     * {@code Comparator<? super U>}. É o {@code ? super} funcionando na prática.
     */
    private static Comparator<Tarefa> porTitulo() {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("pt-BR"));
        collator.setStrength(Collator.SECONDARY);
        return Comparator.comparing(Tarefa::getTitulo, collator);
    }
}

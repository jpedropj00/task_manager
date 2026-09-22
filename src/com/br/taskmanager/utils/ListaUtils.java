package com.br.taskmanager.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Métodos genéricos para manipular coleções de qualquer tipo.
 *
 * Regra usada nos wildcards (PECS - Producer Extends, Consumer Super):
 * - {@code ? extends T}: a coleção só PRODUZ elementos (o método apenas lê dela).
 * - {@code ? super T}: o parâmetro só CONSOME elementos (recebe um T para avaliar).
 * - {@code ?}: o método não depende do tipo dos elementos.
 */
public final class ListaUtils {

    private ListaUtils() {
    }

    /**
     * Wildcard sem limite: só consulta o tamanho, nunca o conteúdo,
     * então aceita uma coleção de qualquer tipo.
     */
    public static boolean estaVazia(Collection<?> colecao) {
        return colecao == null || colecao.isEmpty();
    }

    /**
     * Devolve uma nova lista com os elementos que atendem ao critério.
     *
     * {@code origem} usa {@code ? extends T} porque só lemos dela: aceita,
     * por exemplo, uma {@code List<SubtipoDeTarefa>} onde se espera Tarefa.
     * {@code criterio} usa {@code ? super T} porque recebe um T: aceita um
     * {@code Predicate<Object>} genérico tanto quanto um {@code Predicate<Tarefa>}.
     */
    public static <T> List<T> filtrar(Collection<? extends T> origem, Predicate<? super T> criterio) {
        List<T> resultado = new ArrayList<>();
        for (T item : origem) {
            if (criterio.test(item)) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    /** Conta os elementos que atendem ao critério, com os mesmos wildcards de {@link #filtrar}. */
    public static <T> long contar(Collection<? extends T> origem, Predicate<? super T> criterio) {
        long total = 0;
        for (T item : origem) {
            if (criterio.test(item)) {
                total++;
            }
        }
        return total;
    }
}

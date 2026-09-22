package com.br.taskmanager.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListaUtilsTest {

    @Test
    void estaVaziaAceitaColecaoDeQualquerTipo() {
        // Collection<?>: String, Integer, Map.Entry... o método não depende do tipo.
        assertTrue(ListaUtils.estaVazia(null));
        assertTrue(ListaUtils.estaVazia(List.of()));
        assertFalse(ListaUtils.estaVazia(List.of("a")));
        assertFalse(ListaUtils.estaVazia(Set.of(1, 2)));
        assertFalse(ListaUtils.estaVazia(Map.of("k", 1).entrySet()));
    }

    @Test
    void filtrarDevolveNovaListaSemAlterarAOrigem() {
        List<Integer> origem = List.of(1, 2, 3, 4, 5, 6);

        List<Integer> pares = ListaUtils.filtrar(origem, n -> n % 2 == 0);

        assertEquals(List.of(2, 4, 6), pares);
        assertEquals(6, origem.size());
    }

    @Test
    void listaDevolvidaPorFiltrarPodeSerAlterada() {
        List<Integer> resultado = ListaUtils.filtrar(List.of(3, 1, 2), n -> true);

        resultado.sort(null);

        assertEquals(List.of(1, 2, 3), resultado);
    }

    @Test
    void extendsPermitePassarListaDeSubtipo() {
        // Collection<? extends Number> aceita List<Integer>; Collection<Number> não aceitaria.
        List<Integer> inteiros = List.of(10, 20, 30);

        List<Number> maioresQue15 = ListaUtils.<Number>filtrar(inteiros, n -> n.intValue() > 15);

        assertEquals(List.of(20, 30), maioresQue15);
    }

    @Test
    void superPermitePassarCriterioDeSupertipo() {
        // Predicate<? super Integer> aceita um Predicate<Object> genérico.
        Predicate<Object> naoNulo = obj -> obj != null;
        List<Integer> comNulos = new ArrayList<>();
        comNulos.add(1);
        comNulos.add(null);
        comNulos.add(3);

        assertEquals(List.of(1, 3), ListaUtils.filtrar(comNulos, naoNulo));
        assertEquals(2, ListaUtils.contar(comNulos, naoNulo));
    }

    @Test
    void contarDevolveZeroQuandoNadaAtendeOuColecaoVazia() {
        assertEquals(0, ListaUtils.contar(List.of("a", "b"), String::isEmpty));
        assertEquals(0, ListaUtils.contar(List.<String>of(), s -> true));
    }
}

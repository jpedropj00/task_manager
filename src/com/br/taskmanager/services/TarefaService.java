package com.br.taskmanager.services;

import com.br.taskmanager.dao.GenericDao;
import com.br.taskmanager.dao.TarefaDao;
import com.br.taskmanager.exceptions.TarefaNaoEncontradaException;
import com.br.taskmanager.models.Tarefa;
import com.br.taskmanager.utils.ListaUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class TarefaService {
    private static final String MENSAGEM_NAO_ENCONTRADA = "Tarefa não encontrada. Ela pode ter sido removida.";

    private final GenericDao<Tarefa, UUID> tarefaDao;

    public TarefaService() {
        this(new TarefaDao());
    }

    public TarefaService(GenericDao<Tarefa, UUID> tarefaDao) {
        this.tarefaDao = tarefaDao;
    }

    public List<Tarefa> listarTarefas() {
        return tarefaDao.buscarTodos();
    }

    /**
     * Devolve uma nova lista com as tarefas que atendem ao filtro, na ordem pedida.
     *
     * {@code tarefas} usa {@code ? extends Tarefa} porque só lemos dela.
     * {@code filtro} e {@code ordem} usam {@code ? super Tarefa} porque apenas
     * consomem tarefas: um critério escrito para um supertipo também serve.
     */
    public List<Tarefa> organizar(Collection<? extends Tarefa> tarefas,
                                  Predicate<? super Tarefa> filtro,
                                  Comparator<? super Tarefa> ordem) {
        List<Tarefa> resultado = ListaUtils.filtrar(tarefas, filtro);
        resultado.sort(ordem);
        return resultado;
    }

    public void salvarTarefa(Tarefa tarefa) {
        validar(tarefa);
        verificarTituloDisponivel(tarefa, tarefaDao.buscarTodos());
        tarefaDao.salvar(tarefa);
    }

    public void atualizarTarefa(Tarefa tarefa) throws TarefaNaoEncontradaException {
        validar(tarefa);
        List<Tarefa> existentes = tarefaDao.buscarTodos();
        Tarefa atual = existentes.stream()
                .filter(tarefa::equals)
                .findFirst()
                .orElseThrow(() -> new TarefaNaoEncontradaException(MENSAGEM_NAO_ENCONTRADA));

        // Só checa duplicata quando o título muda: concluir ou editar a descrição
        // de uma tarefa nunca deve ser bloqueado pelo próprio título dela.
        if (!atual.getTitulo().equalsIgnoreCase(tarefa.getTitulo())) {
            verificarTituloDisponivel(tarefa, existentes);
        }

        if (!tarefaDao.atualizar(tarefa)) {
            throw new TarefaNaoEncontradaException(MENSAGEM_NAO_ENCONTRADA);
        }
    }

    public void excluirTarefa(Tarefa tarefa) throws TarefaNaoEncontradaException {
        validar(tarefa);
        if (!tarefaDao.excluir(tarefa)) {
            throw new TarefaNaoEncontradaException(MENSAGEM_NAO_ENCONTRADA);
        }
    }

    private void validar(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("Tarefa inválida.");
        }
    }

    private void verificarTituloDisponivel(Tarefa tarefa, Collection<? extends Tarefa> existentes) {
        boolean duplicado = existentes.stream()
                .anyMatch(outra -> !outra.equals(tarefa)
                        && outra.getTitulo().equalsIgnoreCase(tarefa.getTitulo()));
        if (duplicado) {
            throw new IllegalArgumentException(
                    "Já existe uma tarefa com o título \"" + tarefa.getTitulo() + "\".");
        }
    }
}

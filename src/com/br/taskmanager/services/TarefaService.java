package com.br.taskmanager.services;

import com.br.taskmanager.dao.GenericDao;
import com.br.taskmanager.dao.TarefaDao;
import com.br.taskmanager.exceptions.TarefaNaoEncontradaException;
import com.br.taskmanager.models.Tarefa;
import com.br.taskmanager.utils.ListaUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
     * Lista as tarefas que atendem ao filtro, na ordem pedida.
     *
     * Os dois parâmetros usam {@code ? super Tarefa} porque apenas consomem
     * tarefas: um critério ou comparador escrito para um supertipo de Tarefa
     * também serve aqui.
     */
    public List<Tarefa> listarTarefas(Predicate<? super Tarefa> filtro, Comparator<? super Tarefa> ordem) {
        List<Tarefa> tarefas = ListaUtils.filtrar(tarefaDao.buscarTodos(), filtro);
        tarefas.sort(ordem);
        return tarefas;
    }

    public Optional<Tarefa> buscarPorId(UUID id) {
        return tarefaDao.buscarPorId(id);
    }

    public void salvarTarefa(Tarefa tarefa) {
        validar(tarefa);
        verificarTituloDisponivel(tarefa);
        tarefaDao.salvar(tarefa);
    }

    public void atualizarTarefa(Tarefa tarefa) throws TarefaNaoEncontradaException {
        validar(tarefa);
        Tarefa atual = tarefaDao.buscarPorId(tarefa.getId())
                .orElseThrow(() -> new TarefaNaoEncontradaException(MENSAGEM_NAO_ENCONTRADA));

        // Só checa duplicata quando o título muda: concluir ou editar a descrição
        // de uma tarefa nunca deve ser bloqueado pelo próprio título dela.
        if (!atual.getTitulo().equalsIgnoreCase(tarefa.getTitulo())) {
            verificarTituloDisponivel(tarefa);
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

    private void verificarTituloDisponivel(Tarefa tarefa) {
        boolean duplicado = tarefaDao.buscarTodos().stream()
                .anyMatch(outra -> !outra.equals(tarefa)
                        && outra.getTitulo().equalsIgnoreCase(tarefa.getTitulo()));
        if (duplicado) {
            throw new IllegalArgumentException(
                    "Já existe uma tarefa com o título \"" + tarefa.getTitulo() + "\".");
        }
    }
}

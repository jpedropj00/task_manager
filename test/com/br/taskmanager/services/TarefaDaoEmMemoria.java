package com.br.taskmanager.services;

import com.br.taskmanager.dao.GenericDao;
import com.br.taskmanager.models.Tarefa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** DAO falso que guarda as tarefas num Map, para testar o service sem banco. */
class TarefaDaoEmMemoria implements GenericDao<Tarefa, UUID> {
    private final Map<UUID, Tarefa> tarefas = new LinkedHashMap<>();

    @Override
    public void salvar(Tarefa tarefa) {
        tarefas.put(tarefa.getId(), tarefa);
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        return tarefas.replace(tarefa.getId(), tarefa) != null;
    }

    @Override
    public boolean excluir(Tarefa tarefa) {
        return tarefas.remove(tarefa.getId()) != null;
    }

    @Override
    public Optional<Tarefa> buscarPorId(UUID id) {
        return Optional.ofNullable(tarefas.get(id));
    }

    @Override
    public List<Tarefa> buscarTodos() {
        return new ArrayList<>(tarefas.values());
    }

    /** Grava direto, sem passar pelas regras do service (simula dados antigos). */
    void inserirSemValidar(Tarefa tarefa) {
        tarefas.put(tarefa.getId(), tarefa);
    }
}

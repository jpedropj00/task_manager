package com.br.taskmanager.controllers;

import java.util.ArrayList;

import com.br.taskmanager.exceptions.ListaVaziaException;
import com.br.taskmanager.models.Tarefa;

public class TaskManager {
	private ArrayList<Tarefa> arrayTarefas;

	public TaskManager() {
		this.arrayTarefas = new ArrayList<>();
	}

	public void adicionarTarefa(Tarefa tarefa) {
		if (tarefa == null) {
			throw new IllegalArgumentException("Tarefa inválida.");
		}
		if (buscarTarefa(tarefa.getTitulo()) != null) {
			throw new IllegalArgumentException("Tarefa já presente na lista.");
		}
		arrayTarefas.add(tarefa);
	}

	public void listarTarefas() throws ListaVaziaException {
		if (arrayTarefas.isEmpty()) {
			throw new ListaVaziaException("Lista está vazia.");
		}
		for (Tarefa tarefa : arrayTarefas) {
			System.out.println(tarefa);
			System.out.println();
		}
	}

	public void concluirTarefa(String nome) {
		Tarefa tarefa = buscarTarefa(nome);
		if (tarefa == null) {
			throw new IllegalArgumentException("Tarefa não encontrada.");
		}
		tarefa.setStatus(true);
	}

	public void removerTarefa(String nome) {
		Tarefa tarefa = buscarTarefa(nome);
		if (tarefa == null) {
			throw new IllegalArgumentException("Tarefa não cadastrada.");
		}
		arrayTarefas.remove(tarefa);
	}

	private Tarefa buscarTarefa(String nome) {
		if (nome == null || nome.isBlank()) {
			return null;
		}
		for (Tarefa tarefa : arrayTarefas) {
			if (tarefa.getTitulo().equalsIgnoreCase(nome.trim())) {
				return tarefa;
			}
		}
		return null;
	}
}

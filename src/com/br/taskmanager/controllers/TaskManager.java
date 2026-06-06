package com.br.taskmanager.controllers;
import java.util.ArrayList;

import com.br.taskmanager.models.Tarefa;

public class TaskManager {
	private ArrayList<Tarefa> arrayTarefas ;

	public TaskManager() {
		this.arrayTarefas = new ArrayList<>();
	}
	public void adicionarTarefa(Tarefa tarefa) {
		for (Tarefa t: arrayTarefas) {
			if (t.getTitulo().equalsIgnoreCase(tarefa.getTitulo())) {
				throw new IllegalArgumentException("Tarefa já presente na lista.");
			}
		}
		arrayTarefas.add(tarefa);
	}
	public void listarTarefas() {
		if (arrayTarefas.isEmpty()) {
			throw new IllegalStateException("Lista está vazia.");
		}
		for (Tarefa tarefa: arrayTarefas) {
			System.out.println(tarefa);
			System.out.println();
		}
	}
	public void concluirTarefa(String nome) {
		for (Tarefa t : arrayTarefas) {
	        if (t.getTitulo().equals(nome)) {
	            t.setStatus(true);
	            return;
	        }
	    }

	    throw new IllegalArgumentException("Tarefa não encontrada.");
	}
	public void removerTarefa(String nome) {
		for (Tarefa t: arrayTarefas) {
			if (t.getTitulo().equalsIgnoreCase(nome)) {
				arrayTarefas.remove(t);
				return;
			}
		}
		throw new IllegalArgumentException("Tarefa não cadastrada");
	}
	
}

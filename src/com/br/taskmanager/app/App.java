package com.br.taskmanager.app;

import java.util.Scanner;

import com.br.taskmanager.controllers.TaskManager;
import com.br.taskmanager.exceptions.ListaVaziaException;
import com.br.taskmanager.models.Tarefa;

public class App {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TaskManager taskManager = new TaskManager();
		boolean controller = true;
		do {
			System.out.println("""
					===== Gerenciador de Tarefas =====
					1.Criar Nova Tarefa
					2.Listar Tarefas
					3.Marcar Tarefa Concluída
					4.Remover Tarefa
					5.Sair
					""");
			if (!sc.hasNextLine()) {
				System.out.println("Entrada encerrada. Saindo...");
				break;
			}
			String op = sc.nextLine().trim();
			switch (op) {
			case "1":
				try {
					System.out.print("Digite o título: ");
					String tituloCadastrar = lerLinha(sc);
					System.out.print("Digite a descrição: ");
					String descricao = lerLinha(sc);
					Tarefa t = new Tarefa(tituloCadastrar, descricao);
					taskManager.adicionarTarefa(t);
					System.out.println("Tarefa \"" + t.getTitulo() + "\" cadastrada com sucesso.");
				} catch (IllegalArgumentException e) {
					System.out.println("Erro: " + e.getMessage());
					System.out.println("Tente novamente...");
				}
				break;
			case "2":
				try {
					taskManager.listarTarefas();
				} catch (ListaVaziaException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;
			case "3":
				try {
					taskManager.listarTarefas();
					System.out.print("Digite a tarefa a ser concluída: ");
					String tituloVerificar = lerLinha(sc);
					taskManager.concluirTarefa(tituloVerificar);
					System.out.println("Tarefa concluída com sucesso.");
				} catch (ListaVaziaException | IllegalArgumentException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;
			case "4":
				try {
					taskManager.listarTarefas();
					System.out.print("Digite a tarefa a ser removida: ");
					String tituloRetirar = lerLinha(sc);
					taskManager.removerTarefa(tituloRetirar);
					System.out.println("Tarefa removida com sucesso.");
				} catch (ListaVaziaException | IllegalArgumentException e) {
					System.out.println("Erro: " + e.getMessage());
				}
				break;
			case "5":
				System.out.println("Saindo...");
				controller = false;
				break;
			default:
				System.out.println("Entrada inválida");
				break;
			}

		} while (controller);
		sc.close();
	}

	private static String lerLinha(Scanner sc) {
		return sc.hasNextLine() ? sc.nextLine() : "";
	}
}

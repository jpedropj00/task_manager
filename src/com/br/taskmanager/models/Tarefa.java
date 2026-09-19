package com.br.taskmanager.models;

public class Tarefa {
	private String titulo;
	private String descricao;
	private boolean status;

	public Tarefa(String titulo, String descricao) {
		setTitulo(titulo);
		setDescricao(descricao);
		this.status = false;
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

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Título: " + this.titulo
				+ "\nDescrição: " + (this.descricao.isEmpty() ? "(sem descrição)" : this.descricao)
				+ "\nStatus: " + (this.status ? "Concluída" : "Pendente");
	}
}

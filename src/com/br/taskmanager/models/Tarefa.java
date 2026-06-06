package com.br.taskmanager.models;

public class Tarefa {
	public Tarefa(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.status = false;
	}
	private String titulo;
	private String descricao;
	private Boolean status;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
				+ "\nDescrição: " + this.descricao 
				+ "\nStatus: " + (this.status ? "Concluída": "Pendente");
	}
}

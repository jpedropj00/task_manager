package com.br.taskmanager.controllers;

import com.br.taskmanager.exceptions.DatabaseConnException;
import com.br.taskmanager.exceptions.TarefaNaoEncontradaException;
import com.br.taskmanager.models.Tarefa;
import com.br.taskmanager.services.TarefaService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Controla a tela de cadastro e edição. Sem tarefa em {@link #configurar}, é cadastro. */
public class TarefaFormController {
    @FXML
    private Label labelTituloTela;
    @FXML
    private TextField campoTitulo;
    @FXML
    private TextArea campoDescricao;
    @FXML
    private CheckBox checkConcluida;
    @FXML
    private Label labelErro;

    private TarefaService tarefaService;
    private Tarefa tarefaEmEdicao;
    private Tarefa tarefaSalva;

    @FXML
    private void initialize() {
        campoTitulo.textProperty().addListener((obs, antigo, novo) -> esconderErro());
    }

    public void configurar(TarefaService tarefaService, Tarefa tarefa) {
        this.tarefaService = tarefaService;
        this.tarefaEmEdicao = tarefa;

        boolean edicao = tarefa != null;
        labelTituloTela.setText(edicao ? "Editar tarefa" : "Nova tarefa");
        checkConcluida.setVisible(edicao);
        checkConcluida.setManaged(edicao);

        if (edicao) {
            campoTitulo.setText(tarefa.getTitulo());
            campoDescricao.setText(tarefa.getDescricao());
            checkConcluida.setSelected(tarefa.getStatus());
        }
    }

    /** A tarefa gravada, ou {@code null} se o usuário cancelou. */
    public Tarefa getTarefaSalva() {
        return tarefaSalva;
    }

    @FXML
    private void salvar() {
        try {
            if (tarefaEmEdicao == null) {
                Tarefa nova = new Tarefa(campoTitulo.getText(), campoDescricao.getText());
                tarefaService.salvarTarefa(nova);
                tarefaSalva = nova;
            } else {
                Tarefa atualizada = new Tarefa(tarefaEmEdicao.getId(), campoTitulo.getText(),
                        campoDescricao.getText(), checkConcluida.isSelected());
                tarefaService.atualizarTarefa(atualizada);
                tarefaSalva = atualizada;
            }
            fechar();
        } catch (IllegalArgumentException | TarefaNaoEncontradaException | DatabaseConnException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void mostrarErro(String mensagem) {
        labelErro.setText(mensagem);
        labelErro.setVisible(true);
        labelErro.setManaged(true);
        labelErro.getScene().getWindow().sizeToScene();
    }

    private void esconderErro() {
        if (labelErro.isVisible()) {
            labelErro.setVisible(false);
            labelErro.setManaged(false);
            labelErro.getScene().getWindow().sizeToScene();
        }
    }

    private void fechar() {
        ((Stage) campoTitulo.getScene().getWindow()).close();
    }
}

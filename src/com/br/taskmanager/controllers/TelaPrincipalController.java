package com.br.taskmanager.controllers;

import com.br.taskmanager.exceptions.DatabaseException;
import com.br.taskmanager.exceptions.TarefaNaoEncontradaException;
import com.br.taskmanager.models.FiltroTarefa;
import com.br.taskmanager.models.OrdenacaoTarefa;
import com.br.taskmanager.models.Tarefa;
import com.br.taskmanager.services.TarefaService;
import com.br.taskmanager.utils.ListaUtils;
import com.br.taskmanager.views.TarefaListCell;
import com.br.taskmanager.views.Telas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

public class TelaPrincipalController {
    @FXML
    private ComboBox<FiltroTarefa> comboFiltro;
    @FXML
    private ComboBox<OrdenacaoTarefa> comboOrdenacao;
    @FXML
    private Label labelResumo;
    @FXML
    private ListView<Tarefa> listaTarefas;
    @FXML
    private Button botaoEditar;
    @FXML
    private Button botaoAlternarStatus;
    @FXML
    private Button botaoRemover;

    private final TarefaService tarefaService = new TarefaService();
    private final Label placeholder = new Label();

    @FXML
    private void initialize() {
        comboFiltro.getItems().setAll(FiltroTarefa.values());
        comboFiltro.setValue(FiltroTarefa.TODAS);
        comboFiltro.valueProperty().addListener((obs, antigo, novo) -> carregarTarefas());

        comboOrdenacao.getItems().setAll(OrdenacaoTarefa.values());
        comboOrdenacao.setValue(OrdenacaoTarefa.PENDENTES_PRIMEIRO);
        comboOrdenacao.valueProperty().addListener((obs, antigo, novo) -> carregarTarefas());

        listaTarefas.setCellFactory(lista -> new TarefaListCell());
        listaTarefas.setPlaceholder(placeholder);
        listaTarefas.getSelectionModel().selectedItemProperty()
                .addListener((obs, antiga, nova) -> atualizarBotoes());
        listaTarefas.setOnMouseClicked(evento -> {
            if (evento.getClickCount() == 2 && tarefaSelecionada() != null) {
                editarTarefa();
            }
        });
        listaTarefas.setOnKeyPressed(evento -> {
            if (evento.getCode() == KeyCode.DELETE && tarefaSelecionada() != null) {
                removerTarefa();
            }
        });

        carregarTarefas();
    }

    @FXML
    private void novaTarefa() {
        abrirFormulario(null);
    }

    @FXML
    private void editarTarefa() {
        Tarefa tarefa = tarefaSelecionada();
        if (tarefa != null) {
            abrirFormulario(tarefa);
        }
    }

    @FXML
    private void alternarStatus() {
        Tarefa tarefa = tarefaSelecionada();
        if (tarefa == null) {
            return;
        }

        Tarefa atualizada = new Tarefa(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), !tarefa.getStatus());
        try {
            tarefaService.atualizarTarefa(atualizada);
        } catch (TarefaNaoEncontradaException | DatabaseException | IllegalArgumentException e) {
            mostrarErro(e);
        }
        carregarTarefas(atualizada);
    }

    @FXML
    private void removerTarefa() {
        Tarefa tarefa = tarefaSelecionada();
        if (tarefa == null || !confirmarRemocao(tarefa)) {
            return;
        }

        try {
            tarefaService.excluirTarefa(tarefa);
        } catch (TarefaNaoEncontradaException | DatabaseException | IllegalArgumentException e) {
            mostrarErro(e);
        }
        carregarTarefas(null);
    }

    private void abrirFormulario(Tarefa tarefa) {
        try {
            FXMLLoader loader = Telas.carregador(Telas.FORMULARIO);
            Parent raiz = loader.load();
            TarefaFormController formulario = loader.getController();
            formulario.configurar(tarefaService, tarefa);

            Scene cena = new Scene(raiz);
            cena.getStylesheets().add(Telas.estilo());

            Stage janelaFormulario = new Stage();
            janelaFormulario.initOwner(janela());
            janelaFormulario.initModality(Modality.WINDOW_MODAL);
            janelaFormulario.setTitle(tarefa == null ? "Nova tarefa" : "Editar tarefa");
            janelaFormulario.setResizable(false);
            janelaFormulario.setScene(cena);
            janelaFormulario.showAndWait();

            Tarefa salva = formulario.getTarefaSalva();
            if (salva != null) {
                // Sem isso, uma tarefa nova (pendente) salva com o filtro em
                // "Concluídas" some da lista e parece que o cadastro falhou.
                if (!comboFiltro.getValue().test(salva)) {
                    comboFiltro.setValue(FiltroTarefa.TODAS);
                }
                carregarTarefas(salva);
            }
        } catch (IOException e) {
            mostrarErro(new IllegalStateException("Não foi possível abrir o formulário.", e));
        }
    }

    private boolean confirmarRemocao(Tarefa tarefa) {
        ButtonType remover = new ButtonType("Remover", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Essa ação não pode ser desfeita.", cancelar, remover);
        confirmacao.initOwner(janela());
        confirmacao.setTitle("Confirmar remoção");
        confirmacao.setHeaderText("Remover a tarefa \"" + tarefa.getTitulo() + "\"?");
        Telas.aplicarEstilo(confirmacao);

        // Enter confirma a opção segura: uma remoção acidental não tem volta.
        Button botaoConfirmar = (Button) confirmacao.getDialogPane().lookupButton(remover);
        botaoConfirmar.setDefaultButton(false);
        botaoConfirmar.getStyleClass().add("botao-perigo");
        ((Button) confirmacao.getDialogPane().lookupButton(cancelar)).setDefaultButton(true);

        return confirmacao.showAndWait().orElse(cancelar) == remover;
    }

    private void carregarTarefas() {
        carregarTarefas(tarefaSelecionada());
    }

    private void carregarTarefas(Tarefa paraSelecionar) {
        try {
            List<Tarefa> todas = tarefaService.listarTarefas();
            List<Tarefa> visiveis = tarefaService.organizar(
                    todas, comboFiltro.getValue(), comboOrdenacao.getValue().getComparador());

            listaTarefas.getItems().setAll(visiveis);
            atualizarResumo(todas);
        } catch (DatabaseException e) {
            listaTarefas.getItems().clear();
            labelResumo.setText("");
            mostrarErro(e);
        }

        // equals() compara pelo id, então a tarefa é reencontrada mesmo sendo outra instância.
        if (paraSelecionar != null && listaTarefas.getItems().contains(paraSelecionar)) {
            listaTarefas.getSelectionModel().select(paraSelecionar);
            listaTarefas.scrollTo(paraSelecionar);
        } else {
            listaTarefas.getSelectionModel().clearSelection();
        }
        atualizarBotoes();
    }

    private void atualizarResumo(List<Tarefa> todas) {
        if (ListaUtils.estaVazia(todas)) {
            labelResumo.setText("");
            placeholder.setText("Nenhuma tarefa cadastrada. Clique em \"+ Nova tarefa\" para começar.");
            return;
        }

        long concluidas = ListaUtils.contar(todas, Tarefa::getStatus);
        labelResumo.setText(plural(todas.size(), "tarefa", "tarefas")
                + " · " + plural(concluidas, "concluída", "concluídas"));
        placeholder.setText("Nenhuma tarefa neste filtro.");
    }

    private void atualizarBotoes() {
        Tarefa tarefa = tarefaSelecionada();
        boolean nenhumaSelecionada = tarefa == null;

        botaoEditar.setDisable(nenhumaSelecionada);
        botaoAlternarStatus.setDisable(nenhumaSelecionada);
        botaoRemover.setDisable(nenhumaSelecionada);
        botaoAlternarStatus.setText(tarefa != null && tarefa.getStatus() ? "Reabrir" : "Concluir");
    }

    private void mostrarErro(Exception e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.initOwner(janela());
        alerta.setTitle("Erro");
        alerta.setHeaderText(e.getMessage());
        alerta.setContentText(e.getCause() != null ? e.getCause().getMessage() : null);
        Telas.aplicarEstilo(alerta);
        alerta.showAndWait();
    }

    private Tarefa tarefaSelecionada() {
        return listaTarefas.getSelectionModel().getSelectedItem();
    }

    private Window janela() {
        return listaTarefas.getScene() != null ? listaTarefas.getScene().getWindow() : null;
    }

    private static String plural(long quantidade, String singular, String plural) {
        return quantidade + " " + (quantidade == 1 ? singular : plural);
    }
}

package com.br.taskmanager.views;

import com.br.taskmanager.models.Tarefa;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Desenha cada tarefa da lista como um cartão: título, descrição e status. */
public class TarefaListCell extends ListCell<Tarefa> {
    private static final String CLASSE_CONCLUIDA = "concluida";
    private static final String CLASSE_STATUS_PENDENTE = "status-pendente";
    private static final String CLASSE_STATUS_CONCLUIDA = "status-concluida";

    private final Label titulo = new Label();
    private final Label descricao = new Label();
    private final Label status = new Label();
    private final HBox conteudo;

    public TarefaListCell() {
        titulo.getStyleClass().add("tarefa-titulo");
        descricao.getStyleClass().add("tarefa-descricao");
        status.getStyleClass().add("status");
        status.setMinWidth(Region.USE_PREF_SIZE);

        VBox textos = new VBox(4, titulo, descricao);
        textos.setMinWidth(0);
        HBox.setHgrow(textos, Priority.ALWAYS);

        conteudo = new HBox(12, textos, status);
        conteudo.setAlignment(Pos.CENTER_LEFT);

        getStyleClass().add("tarefa-cell");
        // Impede a célula de forçar rolagem horizontal; textos longos ganham "..."
        setPrefWidth(0);
    }

    @Override
    protected void updateItem(Tarefa tarefa, boolean vazia) {
        super.updateItem(tarefa, vazia);

        // Células são reaproveitadas pelo ListView: limpa o estado anterior sempre.
        getStyleClass().remove(CLASSE_CONCLUIDA);
        status.getStyleClass().removeAll(CLASSE_STATUS_PENDENTE, CLASSE_STATUS_CONCLUIDA);

        if (vazia || tarefa == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        titulo.setText(tarefa.getTitulo());
        descricao.setText(tarefa.getDescricao().isEmpty()
                ? "Sem descrição"
                : tarefa.getDescricao().replaceAll("\\s+", " "));

        if (tarefa.getStatus()) {
            status.setText("Concluída");
            status.getStyleClass().add(CLASSE_STATUS_CONCLUIDA);
            getStyleClass().add(CLASSE_CONCLUIDA);
        } else {
            status.setText("Pendente");
            status.getStyleClass().add(CLASSE_STATUS_PENDENTE);
        }

        setText(null);
        setGraphic(conteudo);
    }
}

package com.br.taskmanager.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;

import java.net.URL;

/** Localiza os arquivos .fxml e .css, que ficam neste mesmo pacote. */
public final class Telas {
    public static final String PRINCIPAL = "TelaPrincipal.fxml";
    public static final String FORMULARIO = "TarefaForm.fxml";
    private static final String ESTILO = "estilo.css";

    private Telas() {
    }

    public static FXMLLoader carregador(String arquivoFxml) {
        return new FXMLLoader(recurso(arquivoFxml));
    }

    public static String estilo() {
        return recurso(ESTILO).toExternalForm();
    }

    /**
     * Diálogos (Alert, confirmações) abrem numa janela própria e não herdam o CSS
     * da tela. {@code Dialog<?>} aceita qualquer diálogo, seja qual for o tipo
     * do resultado dele, porque aqui só mexemos no visual.
     */
    public static void aplicarEstilo(Dialog<?> dialogo) {
        dialogo.getDialogPane().getStylesheets().add(estilo());
    }

    private static URL recurso(String nome) {
        URL url = Telas.class.getResource(nome);
        if (url == null) {
            throw new IllegalStateException("Arquivo de tela não encontrado: " + nome);
        }
        return url;
    }
}

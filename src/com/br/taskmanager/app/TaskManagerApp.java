package com.br.taskmanager.app;

import com.br.taskmanager.database.DatabaseInitializer;
import com.br.taskmanager.exceptions.DatabaseException;
import com.br.taskmanager.views.Telas;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskManagerApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            DatabaseInitializer.initDatabase();
        } catch (DatabaseException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro ao iniciar");
            alerta.setHeaderText(e.getMessage());
            alerta.setContentText(e.getCause() != null ? e.getCause().getMessage() : null);
            Telas.aplicarEstilo(alerta);
            alerta.showAndWait();
            Platform.exit();
            return;
        }

        FXMLLoader loader = Telas.carregador(Telas.PRINCIPAL);
        Scene cena = new Scene(loader.load(), 900, 620);
        cena.getStylesheets().add(Telas.estilo());

        stage.setTitle("TaskManager");
        stage.setMinWidth(720);
        stage.setMinHeight(480);
        stage.setScene(cena);
        stage.show();
    }
}

package com.br.taskmanager.app;

import javafx.application.Application;

/**
 * Ponto de entrada do jar.
 *
 * Não estende {@link Application} de propósito: quando a classe principal de um
 * jar estende Application, o Java exige os módulos do JavaFX no module path e se
 * recusa a iniciar. Delegar para {@link TaskManagerApp} evita essa verificação.
 */
public class Main {

    public static void main(String[] args) {
        Application.launch(TaskManagerApp.class, args);
    }
}

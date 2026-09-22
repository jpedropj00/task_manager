package com.br.taskmanager.database;

import com.br.taskmanager.exceptions.DatabaseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    /**
     * Propriedade de sistema que troca o arquivo do banco, por exemplo:
     * {@code java -Dtaskmanager.db=C:\dados\tarefas.db -jar task-manager.jar}.
     * Os testes usam isso para nunca tocar no banco real.
     */
    public static final String PROPRIEDADE_CAMINHO = "taskmanager.db";

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws DatabaseException {
        Path caminho = caminhoDoBanco();
        try {
            Path pasta = caminho.toAbsolutePath().getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            return DriverManager.getConnection("jdbc:sqlite:" + caminho);
        } catch (IOException | SQLException e) {
            throw new DatabaseException("Erro ao conectar ao banco de dados", e);
        }
    }

    private static Path caminhoDoBanco() {
        String configurado = System.getProperty(PROPRIEDADE_CAMINHO);
        if (configurado != null && !configurado.isBlank()) {
            return Path.of(configurado);
        }
        return Path.of(System.getProperty("user.home"), ".taskmanager", "tarefas.db");
    }
}

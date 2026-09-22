package com.br.taskmanager.database;

import com.br.taskmanager.exceptions.DatabaseConnException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private static final Path DATABASE_PATH =
            Path.of(System.getProperty("user.home"), ".taskmanager", "tarefas.db");
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws DatabaseConnException {
        try {
            Files.createDirectories(DATABASE_PATH.getParent());
            return DriverManager.getConnection(DATABASE_URL);
        } catch (IOException | SQLException e) {
            throw new DatabaseConnException("Erro ao conectar ao banco de dados", e);
        }
    }

    public static Path getDatabasePath() {
        return DATABASE_PATH;
    }
}

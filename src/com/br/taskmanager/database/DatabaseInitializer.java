package com.br.taskmanager.database;

import com.br.taskmanager.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initDatabase() throws DatabaseException {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id        TEXT    PRIMARY KEY,
                    titulo    TEXT    NOT NULL,
                    descricao TEXT    NOT NULL,
                    status    BOOLEAN NOT NULL
                )
                """;
        try (
                Connection conn = ConnectionFactory.getConnection();
                Statement statement = conn.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inicializar banco de dados", e);
        }
    }
}

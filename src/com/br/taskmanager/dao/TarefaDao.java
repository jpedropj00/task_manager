package com.br.taskmanager.dao;

import com.br.taskmanager.database.ConnectionFactory;
import com.br.taskmanager.exceptions.DatabaseConnException;
import com.br.taskmanager.models.Tarefa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TarefaDao implements GenericDao<Tarefa, UUID> {

    @Override
    public void salvar(Tarefa tarefa) throws DatabaseConnException {
        String sql = """
                INSERT INTO tasks (id, titulo, descricao, status)
                VALUES (?, ?, ?, ?)
                """;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, tarefa.getId().toString());
            statement.setString(2, tarefa.getTitulo());
            statement.setString(3, tarefa.getDescricao());
            statement.setBoolean(4, tarefa.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnException("Erro ao salvar tarefa no banco de dados", e);
        }
    }

    @Override
    public boolean atualizar(Tarefa tarefa) throws DatabaseConnException {
        String sql = """
                UPDATE tasks
                SET titulo = ?, descricao = ?, status = ?
                WHERE id = ?
                """;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, tarefa.getTitulo());
            statement.setString(2, tarefa.getDescricao());
            statement.setBoolean(3, tarefa.getStatus());
            statement.setString(4, tarefa.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseConnException("Erro ao atualizar tarefa no banco de dados", e);
        }
    }

    @Override
    public boolean excluir(Tarefa tarefa) throws DatabaseConnException {
        String sql = """
                DELETE FROM tasks
                WHERE id = ?
                """;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, tarefa.getId().toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseConnException("Erro ao remover tarefa do banco de dados", e);
        }
    }

    @Override
    public Optional<Tarefa> buscarPorId(UUID id) throws DatabaseConnException {
        String sql = """
                SELECT id, titulo, descricao, status
                FROM tasks
                WHERE id = ?
                """;
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            statement.setString(1, id.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapear(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseConnException("Erro ao buscar tarefa no banco de dados", e);
        }
    }

    @Override
    public List<Tarefa> buscarTodos() throws DatabaseConnException {
        String sql = """
                SELECT id, titulo, descricao, status
                FROM tasks
                """;
        List<Tarefa> tarefas = new ArrayList<>();
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                tarefas.add(mapear(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseConnException("Erro ao listar tarefas", e);
        }
        return tarefas;
    }

    private Tarefa mapear(ResultSet resultSet) throws SQLException {
        return new Tarefa(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("titulo"),
                resultSet.getString("descricao"),
                resultSet.getBoolean("status")
        );
    }
}

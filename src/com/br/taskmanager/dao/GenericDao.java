package com.br.taskmanager.dao;

import com.br.taskmanager.exceptions.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    void salvar(T entidade) throws DatabaseException;

    boolean atualizar(T entidade) throws DatabaseException;

    boolean excluir(T entidade) throws DatabaseException;

    Optional<T> buscarPorId(ID id) throws DatabaseException;

    List<T> buscarTodos() throws DatabaseException;
}

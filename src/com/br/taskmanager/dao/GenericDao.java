package com.br.taskmanager.dao;

import com.br.taskmanager.exceptions.DatabaseConnException;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    void salvar(T entidade) throws DatabaseConnException;

    boolean atualizar(T entidade) throws DatabaseConnException;

    boolean excluir(T entidade) throws DatabaseConnException;

    Optional<T> buscarPorId(ID id) throws DatabaseConnException;

    List<T> buscarTodos() throws DatabaseConnException;
}

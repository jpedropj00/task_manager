package com.br.taskmanager.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

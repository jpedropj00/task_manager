package com.br.taskmanager.exceptions;

public class DatabaseConnException extends RuntimeException {
    public DatabaseConnException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

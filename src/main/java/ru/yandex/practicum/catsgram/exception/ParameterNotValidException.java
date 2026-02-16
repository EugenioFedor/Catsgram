package ru.yandex.practicum.catsgram.exception;

public class ParameterNotValidException extends IllegalArgumentException {
    public String parameter;
    public String reason;

    public ParameterNotValidException(String message) {
        super(message);
    }
}

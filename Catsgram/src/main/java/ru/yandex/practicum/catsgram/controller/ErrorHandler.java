package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ParameterNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValid(
            ParameterNotValidException exception
    ) {
        ErrorResponse body = new ErrorResponse(
                "Некорректное значение параметра " + exception.getParameter() + ": " + exception.getReason()
        );

        return body;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(
            NotFoundException exception
    ) {
        ErrorResponse body = new ErrorResponse(
                "Произошла непредвиденная ошибка."
        );

        return body;
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedData(
            DuplicatedDataException exception
    ) {
        ErrorResponse body = new ErrorResponse(
                exception.getMessage()
        );

        return body;
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleConditionsNotMet(
            ConditionsNotMetException exception
    ) {
        ErrorResponse body = new ErrorResponse(
                exception.getMessage()
        );

        return body;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(
            Throwable exception
    ) {
        ErrorResponse body = new ErrorResponse(
                exception.getMessage()
        );

        return body;
    }
}

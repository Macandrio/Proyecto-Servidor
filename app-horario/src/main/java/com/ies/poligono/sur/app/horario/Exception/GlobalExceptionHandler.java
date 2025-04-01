package com.ies.poligono.sur.app.horario.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Esta anotación indica que esta clase maneja excepciones para todos los controladores
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Captura excepciones de validación
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Responde con un código 400 (Bad Request) cuando la validación falla
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Itera sobre todos los errores de validación
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); // Obtiene el nombre del campo que falló
            String errorMessage = error.getDefaultMessage();   // Obtiene el mensaje de error asociado
            errors.put(fieldName, errorMessage);               // Guarda el campo y su mensaje de error
        });

        return errors; // Retorna un mapa con los errores de validación
    }
}
package es.readtoowell.api_biblioteca.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja excepciones de validación.
     *
     * @param e Excepción de validación.
     * @return Respuesta con los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Maneja excepciones genéricas y devuelve un error con el mensaje de la excepción.
     *
     * @param e Excepción lanzada.
     * @return Respuesta con el mensaje de error y el código HTTP correspondiente.
     */
    @ExceptionHandler({
            ValidationException.class,
            EntityNotFoundException.class,
            IllegalStateException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, String>> handleGenericException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        return buildErrorResponse(e.getMessage(), status);
    }

    /**
     * Maneja excepciones de acceso denegado y devuelve un error 403.
     *
     * @param e Excepción de acceso denegado.
     * @return Respuesta con el mensaje de error y código 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Construye la respuesta de error.
     *
     * @param message Mensaje de error.
     * @param status Código de estado HTTP.
     * @return ResponseEntity con el error y el código HTTP.
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

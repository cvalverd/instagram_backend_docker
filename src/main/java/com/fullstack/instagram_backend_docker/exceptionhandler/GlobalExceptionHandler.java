package com.fullstack.instagram_backend_docker.exceptionhandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

   private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

   @ExceptionHandler(Exception.class)
   public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
      logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);

      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("message", ex.getMessage());
      body.put("details", request.getDescription(false));

      return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
      logger.error("Resource not found. {}", ex.getMessage());
      logger.debug("Error details: {}", ex);
      Map<String, Object> body = new HashMap<>();
      body.put("message", ex.getMessage());
      body.put("details", request.getDescription(false));

      return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
      logger.error("Bad request. {}", ex.getMessage());
      logger.debug("Error details: {}", ex);
      Map<String, Object> body = new HashMap<>();
      body.put("message", ex.getMessage());
      body.put("details", request.getDescription(false));

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(DatabaseTransactionException.class)
   public ResponseEntity<Object> handleDatabaseTransactionException(DatabaseTransactionException ex,
         WebRequest request) {
      logger.error("Database transaction error. {}", ex.getMessage(), ex); // Capturar stack trace completo
      Throwable rootCause = ex.getCause(); // Obtenemos la causa raíz del error
      Map<String, Object> body = new HashMap<>();
      body.put("message", ex.getMessage());
      body.put("details", request.getDescription(false));

      if (rootCause != null) {
         body.put("cause", rootCause.getMessage()); // Mostrar el mensaje de la causa raíz
      }

      return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
   }

}

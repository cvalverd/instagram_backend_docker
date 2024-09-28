package com.fullstack.instagram_backend_docker.exceptionhandler;

public class DatabaseTransactionException extends RuntimeException {

   public DatabaseTransactionException(String message, Throwable cause) {
      super(message);
      super.initCause(cause);
   }
}

package com.fullstack.instagram_backend_docker.exceptionhandler;

public class ResourceNotFoundException extends RuntimeException {

   public ResourceNotFoundException(String message) {
       super(message);
   }
   
}
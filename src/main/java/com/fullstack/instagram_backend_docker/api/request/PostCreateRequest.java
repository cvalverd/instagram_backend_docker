package com.fullstack.instagram_backend_docker.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostCreateRequest {
   
   @NotNull
   @Size(max = 150)
   private String caption;

   @NotNull
   private String imageUrl;

   @NotNull
   private Long profileInfoId;
}

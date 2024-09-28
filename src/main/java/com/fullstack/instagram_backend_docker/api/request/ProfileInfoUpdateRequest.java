package com.fullstack.instagram_backend_docker.api.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileInfoUpdateRequest {
   private String title;

   @Size(max = 150)
   private String description;
   
   private String profilePicUrl;
}

package com.fullstack.instagram_backend_docker.service.profileinfo;

import java.util.List;
import java.util.Optional;

import com.fullstack.instagram_backend_docker.api.request.ProfileInfoUpdateRequest;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;

public interface ProfileInfoService {
   ProfileInfo updateProfileInfo(Long id, ProfileInfoUpdateRequest updateRequest);
   Optional<ProfileInfo> findProfileInfoById(Long id);
   List<ProfileInfo> findAllProfileInfos();
   void deleteProfileInfoById(Long id);
}

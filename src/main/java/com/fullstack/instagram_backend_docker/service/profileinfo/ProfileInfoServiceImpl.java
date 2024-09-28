package com.fullstack.instagram_backend_docker.service.profileinfo;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fullstack.instagram_backend_docker.api.request.ProfileInfoUpdateRequest;
import com.fullstack.instagram_backend_docker.exceptionhandler.ResourceNotFoundException;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import com.fullstack.instagram_backend_docker.repository.ProfileInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileInfoServiceImpl implements ProfileInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileInfoServiceImpl.class);

    private final ProfileInfoRepository profileInfoRepository;

    @Override
    public ProfileInfo updateProfileInfo(Long id, ProfileInfoUpdateRequest updateRequest) {
        logger.info("Updating profile info with id: {} - method updateProfileInfo", id);
        ProfileInfo profileInfo = profileInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfileInfo not found"));

        if (updateRequest.getTitle() != null) {
            logger.info("Updating title to: {} - method updateProfileInfo", updateRequest.getTitle());
            profileInfo.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            logger.info("Updating description to: {} - method updateProfileInfo", updateRequest.getDescription());
            profileInfo.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getProfilePicUrl() != null) {
            logger.info("Updating profile pic url to: {} - method updateProfileInfo", updateRequest.getProfilePicUrl());
            profileInfo.setProfilePicUrl(updateRequest.getProfilePicUrl());
        }

        logger.info("Saving profile info - method updateProfileInfo");
        return profileInfoRepository.save(profileInfo);
    }

    @Override
    public Optional<ProfileInfo> findProfileInfoById(Long id) {
        logger.info("Getting profile info by id: {} - method findProfileInfoById", id);
        return profileInfoRepository.findById(id);
    }

    @Override
    public List<ProfileInfo> findAllProfileInfos() {
        logger.info("Getting all profile infos - method findAllProfileInfos");
        return profileInfoRepository.findAll();
    }

    @Override
    public void deleteProfileInfoById(Long id) {
        logger.info("Deleting profile info with id: {} - method deleteProfileInfoById", id);
        try {
            profileInfoRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("ProfileInfo not found");
        }
    }
}


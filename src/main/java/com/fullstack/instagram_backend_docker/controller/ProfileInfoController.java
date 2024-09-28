package com.fullstack.instagram_backend_docker.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.instagram_backend_docker.api.request.ProfileInfoUpdateRequest;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import com.fullstack.instagram_backend_docker.service.profileinfo.ProfileInfoService;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/profile-info")
@RequiredArgsConstructor
public class ProfileInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileInfoController.class);

    private final ProfileInfoService profileInfoService;

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ProfileInfo>> patchProfileInfo(@PathVariable Long id,
                                                                     @RequestBody ProfileInfoUpdateRequest updateRequest) {
        logger.info("Updating profile info with id: {}", id);
        ProfileInfo updatedProfileInfo = profileInfoService.updateProfileInfo(id, updateRequest);
        EntityModel<ProfileInfo> profileInfoModel = EntityModel.of(updatedProfileInfo);
        profileInfoModel.add(linkTo(methodOn(ProfileInfoController.class).getProfileInfoById(id)).withSelfRel());

        return ResponseEntity.ok(profileInfoModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProfileInfo>> getProfileInfoById(@PathVariable Long id) {
        logger.info("Getting profile info by id: {}", id);
        Optional<ProfileInfo> profileInfo = profileInfoService.findProfileInfoById(id);
        if (profileInfo.isPresent()) {
            EntityModel<ProfileInfo> profileInfoModel = EntityModel.of(profileInfo.get());
            profileInfoModel.add(linkTo(methodOn(ProfileInfoController.class).getProfileInfoById(id)).withSelfRel());

            return ResponseEntity.ok(profileInfoModel);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<ProfileInfo>>> getAllProfileInfos() {
        logger.info("Getting all profile infos");
        List<EntityModel<ProfileInfo>> profileInfos = profileInfoService.findAllProfileInfos().stream()
                .map(profile -> {
                    EntityModel<ProfileInfo> profileInfoModel = EntityModel.of(profile);
                    profileInfoModel.add(linkTo(methodOn(ProfileInfoController.class).getProfileInfoById(profile.getId())).withSelfRel());
                    return profileInfoModel;
                }).toList();

        return ResponseEntity.ok(CollectionModel.of(profileInfos,
                linkTo(methodOn(ProfileInfoController.class).getAllProfileInfos()).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileInfo(@PathVariable Long id) {
        logger.info("Deleting profile info with id: {}", id);
        profileInfoService.deleteProfileInfoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

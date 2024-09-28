package com.fullstack.instagram_backend_docker.controller;

import com.fullstack.instagram_backend_docker.api.request.ProfileInfoUpdateRequest;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import com.fullstack.instagram_backend_docker.service.profileinfo.ProfileInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileInfoControllerTest {

    @Mock
    private ProfileInfoService profileInfoService;

    @InjectMocks
    private ProfileInfoController profileInfoController;

    private ProfileInfo profileInfo;
    private ProfileInfoUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        profileInfo = new ProfileInfo();
        profileInfo.setId(1L);
        profileInfo.setTitle("Test Title");

        updateRequest = new ProfileInfoUpdateRequest();
        updateRequest.setTitle("Updated Title");
    }

    @Test
    void patchProfileInfo() {
        // Arrange
        when(profileInfoService.updateProfileInfo(eq(1L), any(ProfileInfoUpdateRequest.class))).thenReturn(profileInfo);

        // Act
        ResponseEntity<EntityModel<ProfileInfo>> response = profileInfoController.patchProfileInfo(1L, updateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<ProfileInfo> updatedProfileModel = response.getBody();
        assertNotNull(updatedProfileModel);
        assertEquals(profileInfo.getId(), updatedProfileModel.getContent().getId());
        assertTrue(updatedProfileModel.hasLink("self"));
        verify(profileInfoService).updateProfileInfo(eq(1L), any(ProfileInfoUpdateRequest.class));
    }

    @Test
    void getProfileInfoById_found() {
        // Arrange
        when(profileInfoService.findProfileInfoById(1L)).thenReturn(Optional.of(profileInfo));

        // Act
        ResponseEntity<EntityModel<ProfileInfo>> response = profileInfoController.getProfileInfoById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EntityModel<ProfileInfo> foundProfileModel = response.getBody();
        assertNotNull(foundProfileModel);
        assertEquals(profileInfo.getId(), foundProfileModel.getContent().getId());
        assertTrue(foundProfileModel.hasLink("self"));
        verify(profileInfoService).findProfileInfoById(1L);
    }

    @Test
    void getProfileInfoById_notFound() {
        // Arrange
        when(profileInfoService.findProfileInfoById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EntityModel<ProfileInfo>> response = profileInfoController.getProfileInfoById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(profileInfoService).findProfileInfoById(1L);
    }

    @Test
    void getAllProfileInfos() {
        // Arrange
        List<ProfileInfo> profileInfoList = List.of(profileInfo);
        when(profileInfoService.findAllProfileInfos()).thenReturn(profileInfoList);

        // Act
        ResponseEntity<CollectionModel<EntityModel<ProfileInfo>>> response = profileInfoController.getAllProfileInfos();
        CollectionModel<EntityModel<ProfileInfo>> foundProfilesModel = response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(foundProfilesModel);
        assertTrue(foundProfilesModel.hasLink("self"));
        verify(profileInfoService).findAllProfileInfos();
    }

    @Test
    void deleteProfileInfo() {
        // Arrange
        doNothing().when(profileInfoService).deleteProfileInfoById(1L);

        // Act
        ResponseEntity<Void> response = profileInfoController.deleteProfileInfo(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(profileInfoService).deleteProfileInfoById(1L);
    }
}
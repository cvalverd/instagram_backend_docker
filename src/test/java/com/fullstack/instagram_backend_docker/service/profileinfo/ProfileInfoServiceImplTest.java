package com.fullstack.instagram_backend_docker.service.profileinfo;

import com.fullstack.instagram_backend_docker.api.request.ProfileInfoUpdateRequest;
import com.fullstack.instagram_backend_docker.model.ProfileInfo;
import com.fullstack.instagram_backend_docker.repository.ProfileInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileInfoServiceImplTest {

    @Mock
    private ProfileInfoRepository profileInfoRepository;

    @InjectMocks
    private ProfileInfoServiceImpl profileInfoService;

    private ProfileInfo profileInfo;

    @BeforeEach
    void setup() {
        profileInfo = ProfileInfo.builder()
                .id(1L)
                .title("Test title")
                .description("Test description")
                .profilePicUrl("https://example.com/image.jpg")
                .build();
    }

    @Test
    void updateProfileInfo() {
        // Arrange
        ProfileInfo existingProfileInfo = profileInfo();
        ProfileInfoUpdateRequest updateRequest = updateRequest();

        when(profileInfoRepository.findById(existingProfileInfo.getId()))
                .thenReturn(Optional.of(existingProfileInfo));

        ProfileInfo updatedProfileInfo = new ProfileInfo();
        updatedProfileInfo.setId(existingProfileInfo.getId());
        updatedProfileInfo.setTitle(updateRequest.getTitle());
        updatedProfileInfo.setDescription(updateRequest.getDescription());
        updatedProfileInfo.setProfilePicUrl(updateRequest.getProfilePicUrl());

        when(profileInfoRepository.save(any(ProfileInfo.class)))
                .thenReturn(updatedProfileInfo);

        // Act
        ProfileInfo result = profileInfoService.updateProfileInfo(
                existingProfileInfo.getId(),
                updateRequest
        );

        // Assert
        assertEquals(existingProfileInfo.getId(), result.getId());
        assertEquals(updateRequest.getTitle(), result.getTitle());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getProfilePicUrl(), result.getProfilePicUrl());
    }


    private ProfileInfo profileInfo() {
        return ProfileInfo.builder()
                .id(1L)
                .title("Test title")
                .description("Test description")
                .profilePicUrl("https://example.com/image.jpg")
                .build();
    }

    private ProfileInfoUpdateRequest updateRequest() {
        return ProfileInfoUpdateRequest.builder()
                .title("Updated title")
                .description("Updated description")
                .profilePicUrl("https://example.com/image2.jpg")
                .build();
    }

    @Test
    void findProfileInfoById() {
        // Arrange
        when(profileInfoRepository.findById(profileInfo.getId())).thenReturn(Optional.of(profileInfo));

        // Act
        Optional<ProfileInfo> foundProfileInfo = profileInfoService.findProfileInfoById(profileInfo.getId());

        // Assert
        assertTrue(foundProfileInfo.isPresent());
        assertEquals(profileInfo, foundProfileInfo.get());
    }

    @Test
    void findAllProfileInfos() {
        // Arrange
        when(profileInfoRepository.findAll()).thenReturn(List.of(profileInfo));

        // Act
        List<ProfileInfo> foundProfileInfos = profileInfoService.findAllProfileInfos();

        // Assert
        assertEquals(List.of(profileInfo), foundProfileInfos);
    }

    @Test
    void deleteProfileInfoById() {
        // Act
        profileInfoService.deleteProfileInfoById(profileInfo.getId());

        // Assert
        verify(profileInfoRepository).deleteById(profileInfo.getId());
    }
}
package com.fullstack.instagram_backend_docker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.instagram_backend_docker.model.ProfileInfo;

@Repository
public interface ProfileInfoRepository extends JpaRepository<ProfileInfo, Long> {}
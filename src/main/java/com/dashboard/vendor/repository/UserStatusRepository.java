package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.UserStatus;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}
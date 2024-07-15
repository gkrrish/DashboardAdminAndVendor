package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.UserNewspaperSubscription;

@Repository
public interface AdminDashboardRepository extends JpaRepository<UserNewspaperSubscription, Long> {
}

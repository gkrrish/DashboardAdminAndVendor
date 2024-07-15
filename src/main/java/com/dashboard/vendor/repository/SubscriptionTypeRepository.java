package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.SubscriptionType;

@Repository
public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, Long> {
}
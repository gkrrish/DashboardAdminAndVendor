package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.VendorDetails;

@Repository
public interface VendorDetailsRepository extends JpaRepository<VendorDetails, Integer> {
}
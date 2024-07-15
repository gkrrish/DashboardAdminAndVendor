package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.CategoryType;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {
}
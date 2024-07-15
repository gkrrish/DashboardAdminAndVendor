package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.NewspaperFiles;

@Repository
public interface NewspaperFilesRepository extends JpaRepository<NewspaperFiles, Long> {

}

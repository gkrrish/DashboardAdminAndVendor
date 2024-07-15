package com.dashboard.vendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
	
	Country findByCountryName(String countryName);
}
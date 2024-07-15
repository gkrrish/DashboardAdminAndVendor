package com.dashboard.vendor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dashboard.entity.Invoice;
import com.dashboard.entity.UserDetails;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	List<Invoice> findByUser(UserDetails userDetails);
	List<Invoice> findByUserOrderByInvoiceDateDesc(UserDetails userDetails);
}
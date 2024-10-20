package com.project.repository;

import com.project.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;



public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


}
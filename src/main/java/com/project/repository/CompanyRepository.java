package com.project.repository;

import com.project.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findByCompanyNameAndCompanyEmail(String companyName, String companyEmail);
}
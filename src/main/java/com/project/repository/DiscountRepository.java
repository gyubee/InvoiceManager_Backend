package com.project.repository;

import com.project.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    @Query("SELECT d FROM Discount d " +
            "WHERE d.product.productId = :productId " +
            "AND d.daysBeforeExpiration <= :daysBeforeExpiration " +
            "ORDER BY d.daysBeforeExpiration DESC")
    Optional<Discount> findTopByProductProductIdAndDaysBeforeExpirationLessThanEqualOrderByDaysBeforeExpirationDesc(
            @Param("productId") Integer productId,
            @Param("daysBeforeExpiration") Integer daysBeforeExpiration);

}

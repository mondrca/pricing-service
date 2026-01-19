package com.inditex.pricing.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataPriceRepository extends JpaRepository<PriceJpaEntity, Long> {

  @Query(
      """
        SELECT p
        FROM PriceJpaEntity p
        WHERE p.brandId = :brandId
          AND p.productId = :productId
          AND :applicationDate BETWEEN p.startDate AND p.endDate
        ORDER BY p.priority DESC
    """)
  List<PriceJpaEntity> findApplicableOrderedByPriorityDesc(
      @Param("applicationDate") LocalDateTime applicationDate,
      @Param("productId") Long productId,
      @Param("brandId") Long brandId);
}

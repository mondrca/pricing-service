package com.inditex.pricing.adapter.out.persistence;

import com.inditex.pricing.adapter.out.persistence.mapper.PriceJpaMapper;
import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.model.Price;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final SpringDataPriceRepository springRepo;

    public PriceRepositoryAdapter(SpringDataPriceRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Optional<Price> findApplicable(LocalDateTime applicationDate, Long productId, Long brandId) {
        return springRepo
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        brandId, productId, applicationDate, applicationDate
                )
                .map(PriceJpaMapper::toDomain);
    }
}

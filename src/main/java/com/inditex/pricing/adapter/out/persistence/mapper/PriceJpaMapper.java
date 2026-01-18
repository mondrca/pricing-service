package com.inditex.pricing.adapter.out.persistence.mapper;

import com.inditex.pricing.adapter.out.persistence.PriceJpaEntity;
import com.inditex.pricing.domain.model.Price;

public final class PriceJpaMapper {

    private PriceJpaMapper() {}

    public static Price toDomain(PriceJpaEntity e) {
        return new Price(
                e.getProductId(),
                e.getBrandId(),
                e.getPriceList(),
                e.getPriority(),
                e.getStartDate(),
                e.getEndDate(),
                e.getPrice(),
                e.getCurrency()
        );
    }
}

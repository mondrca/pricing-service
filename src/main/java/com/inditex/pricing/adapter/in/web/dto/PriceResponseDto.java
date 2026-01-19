package com.inditex.pricing.adapter.in.web.dto;

import com.inditex.pricing.domain.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDto(
        Long productId,
        Long brandId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency
) {
    public static PriceResponseDto from(Price p) {
        return new PriceResponseDto(
                p.productId(),
                p.brandId(),
                p.priceList(),
                p.startDate(),
                p.endDate(),
                p.price(),
                p.currency()
        );
    }
}

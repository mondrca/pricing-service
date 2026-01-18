package com.inditex.pricing.adapter.in.web;

import com.inditex.pricing.adapter.in.web.dto.PriceResponseDto;
import com.inditex.pricing.application.port.in.GetApplicablePriceUseCase;
import com.inditex.pricing.domain.model.Price;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceQueryController {

    private final GetApplicablePriceUseCase useCase;

    public PriceQueryController(GetApplicablePriceUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/applicable")
    public ResponseEntity<PriceResponseDto> getApplicable(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Long brandId
    ) {
        return useCase.getApplicablePrice(applicationDate, productId, brandId)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private PriceResponseDto toDto(Price p) {
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

package com.inditex.pricing.adapter.in.web;

import com.inditex.pricing.adapter.in.web.dto.PriceResponseDto;
import com.inditex.pricing.application.port.in.GetApplicablePriceUseCase;
import com.inditex.pricing.domain.model.Price;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@Validated
public class PriceQueryController {

    private final GetApplicablePriceUseCase useCase;

    public PriceQueryController(GetApplicablePriceUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping(value = "/applicable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceResponseDto> getApplicable(
            @RequestParam("applicationDate")
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @RequestParam("productId") @NotNull @Positive Long productId,
            @RequestParam("brandId") @NotNull @Positive Long brandId
    ) {
        Price price = useCase.getApplicablePrice(applicationDate, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(
                        "No applicable price found for productId=%d, brandId=%d at %s"
                                .formatted(productId, brandId, applicationDate)
                ));

        return ResponseEntity.ok(PriceResponseDto.from(price));
    }
}

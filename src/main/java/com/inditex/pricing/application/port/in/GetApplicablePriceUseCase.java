package com.inditex.pricing.application.port.in;

import com.inditex.pricing.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

public interface GetApplicablePriceUseCase {
  Optional<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}

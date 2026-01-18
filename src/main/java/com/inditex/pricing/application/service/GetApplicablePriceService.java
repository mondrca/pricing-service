package com.inditex.pricing.application.service;

import com.inditex.pricing.application.port.in.GetApplicablePriceUseCase;
import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.model.Price;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort repositoryPort;

    public GetApplicablePriceService(PriceRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Optional<Price> getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return repositoryPort.findApplicable(applicationDate, productId, brandId);
    }
}

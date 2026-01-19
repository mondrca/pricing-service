package com.inditex.pricing.application.service;

import com.inditex.pricing.application.port.out.PriceRepositoryPort;
import com.inditex.pricing.domain.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetApplicablePriceServiceTest {

    @Test
    void shouldReturnApplicablePriceWhenFound() {
        PriceRepositoryPort port = mock(PriceRepositoryPort.class);
        GetApplicablePriceService service = new GetApplicablePriceService(port);

        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");
        Long productId = 35455L;
        Long brandId = 1L;

        Price expected = new Price(
                productId,
                brandId,
                1L,
                0,
                LocalDateTime.parse("2020-06-14T00:00:00"),
                LocalDateTime.parse("2020-12-31T23:59:59"),
                BigDecimal.valueOf(35.50),
                "EUR"
        );

        when(port.findApplicableWithHighestPriority(applicationDate, productId, brandId))
                .thenReturn(Optional.of(expected));

        Optional<Price> result = service.getApplicablePrice(applicationDate, productId, brandId);

        assertThat(result).contains(expected);

        verify(port, times(1)).findApplicableWithHighestPriority(applicationDate, productId, brandId);
        verifyNoMoreInteractions(port);

    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        PriceRepositoryPort port = mock(PriceRepositoryPort.class);
        GetApplicablePriceService service = new GetApplicablePriceService(port);

        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");
        Long productId = 35455L;
        Long brandId = 1L;

        when(port.findApplicableWithHighestPriority(applicationDate, productId, brandId))
                .thenReturn(Optional.empty());

        Optional<Price> result = service.getApplicablePrice(applicationDate, productId, brandId);

        assertThat(result).isEmpty();

        verify(port, times(1)).findApplicableWithHighestPriority(applicationDate, productId, brandId);
        verifyNoMoreInteractions(port);
    }
}

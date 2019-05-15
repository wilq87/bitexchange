package com.wilq.bitexchange.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BitcoinStockHealthIndicator implements HealthIndicator {

    @Autowired
    private BitcoinStockService service;

    @Override
    public Health health() {
        return service.hasLatestRates() ?
                Health.up().build() :
                Health.down().withDetail("errorMessage", "No connection to stock after startup").build();
    }
}

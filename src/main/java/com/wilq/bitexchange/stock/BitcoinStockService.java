package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import com.wilq.bitexchange.stock.client.BitcoinRates;
import com.wilq.bitexchange.stock.client.BlockchainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;

@Service
@Slf4j
public class BitcoinStockService {

    @Autowired
    private BlockchainClient client;

    @Autowired
    private BitcoinStockRepository repository;

    private BitcoinRates latestRates;

    public boolean hasLatestRates() {
        return latestRates != null;
    }

    public BitcoinRate getLatestRate(Currency currency) {
        if (!hasLatestRates())
            fetchRates();

        return latestRates.getRate(currency)
                .orElseThrow(() -> new IllegalArgumentException("Rate not supported"));
    }

    @Scheduled(fixedRateString = "${blockchain.fetch.rate.millis}")
    public BitcoinRates fetchRates() {
        log.info("Fetching rates ...");
        latestRates = client.getRates();
        log.info("Newest rate fetched at: {}", latestRates.getFetchTimestamp());

        repository.save(latestRates);

        return latestRates;
    }

    public Collection<BitcoinRate> getRates(Currency currency, LocalDate startDate, LocalDate endDate) {
        return repository.findRatesInInclusiveDateRange(currency, startDate, endDate);
    }
}

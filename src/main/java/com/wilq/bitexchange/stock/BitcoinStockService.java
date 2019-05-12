package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import com.wilq.bitexchange.stock.client.BitcoinRates;
import com.wilq.bitexchange.stock.client.BlockchainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Optional;

@Service
@Slf4j
public class BitcoinStockService {

    @Autowired
    private BlockchainClient client;

    private BitcoinRates latestRates;

    public BitcoinRate getLatestRate(Currency currency){
        if(latestRates == null)
            fetchRates();

        return latestRates.getRate(currency)
                .orElseThrow(() -> new IllegalArgumentException("Rate not supported"));
    }

    @Scheduled(fixedRateString = "${blockchain.fetch.rate.millis}")
    public BitcoinRates fetchRates(){
        log.info("Fetching rates ...");
        latestRates = client.getRates();
        log.info("Newest rate fetched at: {}", latestRates.getFetchTimestamp());
        return latestRates;
    }
}

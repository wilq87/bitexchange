package com.wilq.bitexchange.stock.client;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BitcoinRates {
    @Getter
    private LocalDateTime fetchTimestamp = LocalDateTime.now();
    private Map<String, BitcoinRate> rates = new HashMap<>();

    @JsonAnyGetter
    public Map<String, BitcoinRate> getRates() {
        return rates;
    }

    @JsonAnySetter
    public void setRate(String currency, BitcoinRate rate) {
        rate.setFetchTimestamp(fetchTimestamp);
        this.rates.put(currency, rate);
    }

    public Optional<BitcoinRate> getRate(Currency currency){
        return Optional.ofNullable(rates.get(currency.toString()));
    }
}

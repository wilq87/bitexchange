package com.wilq.bitexchange.stock.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockchainClient {

    @Value("${blockchain.rates.api.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public BitcoinRates getRates(){
        return restTemplate.getForObject(url, BitcoinRates.class);
    }
}

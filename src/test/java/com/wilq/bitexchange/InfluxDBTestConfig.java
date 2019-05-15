package com.wilq.bitexchange;

import com.wilq.bitexchange.stock.BitcoinStockRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(InfluxDBConfiguration.class)
public class InfluxDBTestConfig {

    @Bean
    public BitcoinStockRepository bitcoinStockRepository(){
        return new BitcoinStockRepository();
    }
}

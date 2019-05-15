package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.InfluxDBTestConfig;
import com.wilq.bitexchange.stock.client.BitcoinRate;
import com.wilq.bitexchange.stock.client.BitcoinRates;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= InfluxDBTestConfig.class, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
@Slf4j
public class BitcoinStockRepositoryTest {

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private BitcoinStockRepository repository;

    @Before
    public void before(){
        log.info("Cleaning DB before test: " + influxDBTemplate.getDatabase());
        influxDBTemplate.query(new Query("DROP SERIES FROM \"rate\" WHERE \"source\" = 'USD'", influxDBTemplate.getDatabase()));
        influxDBTemplate.query(new Query("DROP SERIES FROM \"rate\" WHERE \"destination\" = 'USD'", influxDBTemplate.getDatabase()));
    }

    @Test
    public void shouldFindSavedRates() throws Exception {
        BitcoinRates rates = new BitcoinRates();
        rates.setRate("USD", new BitcoinRate(11.33, 33.2, null));
        repository.save(rates);


        Currency usd = Currency.getInstance("USD");
        Collection<BitcoinRate> savedRates = repository.findRatesInInclusiveDateRange(usd, LocalDate.now(), LocalDate.now());
        assertThat(savedRates.size(), is(1));
        assertThat(savedRates.iterator().next(), is(rates.getRate(usd).get()));
    }

}
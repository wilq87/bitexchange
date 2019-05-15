package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import com.wilq.bitexchange.stock.client.BitcoinRates;
import com.wilq.bitexchange.stock.client.BlockchainClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Currency;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class BitcoinStockServiceTest {

    @Mock
    private BlockchainClient client;

    @Mock
    private BitcoinStockRepository repository;

    @InjectMocks
    private BitcoinStockService service;

    private BitcoinRates fakeRates = generateRates();

    @Test
    public void shouldReturnLatestRateWhenPresent() throws Exception {
        ReflectionTestUtils.setField(service, "latestRates", fakeRates);

        Currency usd = Currency.getInstance("USD");
        BitcoinRate usdRate = service.getLatestRate(usd);

        assertThat(usdRate, is(fakeRates.getRate(usd).get()));
        verifyZeroInteractions(client);
    }

    @Test
    public void shouldFetchAndSaveBeforeGetLatest() throws Exception {
        when(client.getRates()).thenReturn(fakeRates);
        Currency usd = Currency.getInstance("USD");
        BitcoinRate usdRate = service.getLatestRate(usd);

        assertThat(usdRate, is(fakeRates.getRate(usd).get()));
        verify(client).getRates();
    }

    @Test
    public void shouldFetchRates() throws Exception {
        when(client.getRates()).thenReturn(fakeRates);

        BitcoinRates rates = service.fetchRates();

        assertThat(rates, is(fakeRates));
    }

    @Test
    public void shouldSaveRates() throws Exception {
        when(client.getRates()).thenReturn(fakeRates);

        service.fetchRates();

        verify(repository).save(fakeRates);
    }

    @Test
    public void shouldGetRangeOfRatesForToday() throws Exception {
        when(client.getRates()).thenReturn(fakeRates);

        LocalDate today = LocalDate.now();
        Currency usd = Currency.getInstance("USD");
        service.getRates(usd, today, today);

        verify(repository).findRatesInInclusiveDateRange(usd, today, today);
    }


    private BitcoinRates generateRates() {
        BitcoinRates rates = new BitcoinRates();
        rates.setRate("USD", new BitcoinRate(5.4,3.3, rates.getFetchTimestamp()));
        rates.setRate("EUR", new BitcoinRate(6.4,4.3, rates.getFetchTimestamp()));
        return rates;
    }

}
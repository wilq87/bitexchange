package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BitcoinStockController.class)
public class BitcoinStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BitcoinStockService service;

    private Currency usd = Currency.getInstance("USD");

    private BitcoinRate rate = new BitcoinRate(234.44, 44.33, LocalDateTime.now());

    @Test
    public void shouldReturnLatestUsd() throws Exception {
        when(service.getLatestRate(usd)).thenReturn(rate);

        this.mockMvc.perform(get("/rate/USD/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buy", is(rate.getBuy())))
                .andExpect(jsonPath("$.sell", is(rate.getSell())))
                .andExpect(jsonPath("$.fetchTimestamp", is(rate.getFetchTimestamp().toString())));
    }

    @Test
    public void shouldReturnHistoryCurrencies() throws Exception {
        when(service.getRates(any(), any(), any())).thenReturn(Collections.singleton(rate));

        this.mockMvc.perform(get("/rate/USD?startDate=2019-05-01&endDate=2019-05-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].buy", is(rate.getBuy())))
                .andExpect(jsonPath("$[0].sell", is(rate.getSell())))
                .andExpect(jsonPath("$[0].fetchTimestamp", is(rate.getFetchTimestamp().toString())));
    }
}
package com.wilq.bitexchange.stock.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BlockchainClientTest {

    public static final String FAKE_URL = "blockchain_url";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BlockchainClient client;

    @Before
    public void before(){
        ReflectionTestUtils.setField(client, "url", FAKE_URL);
    }

    @Test
    public void shouldGetRates() throws Exception {
        BitcoinRates expectedRates = new BitcoinRates();
        Mockito.when(restTemplate.getForObject(FAKE_URL, BitcoinRates.class))
            .thenReturn(expectedRates);

        BitcoinRates rates = client.getRates();

        assertThat(rates, is(expectedRates));
    }

}
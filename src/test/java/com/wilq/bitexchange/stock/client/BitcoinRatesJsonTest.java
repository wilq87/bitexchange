package com.wilq.bitexchange.stock.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Currency;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BitcoinRatesJsonTest {

    private String ratesString="{\n" +
            "  \"USD\" : {\"15m\" : 7144.79, \"last\" : 7144.79, \"buy\" : 7144.79, \"sell\" : 7140.79, \"symbol\" : \"$\"},\n" +
            "  \"AUD\" : {\"15m\" : 10215.62, \"last\" : 10215.62, \"buy\" : 10215.62, \"sell\" : 10215.62, \"symbol\" : \"$\"},\n" +
            "  \"BRL\" : {\"15m\" : 28289.2, \"last\" : 28289.2, \"buy\" : 28289.2, \"sell\" : 28289.2, \"symbol\" : \"R$\"},\n" +
            "  \"CAD\" : {\"15m\" : 9593.03, \"last\" : 9593.03, \"buy\" : 9593.03, \"sell\" : 9593.03, \"symbol\" : \"$\"},\n" +
            "  \"CHF\" : {\"15m\" : 7229.22, \"last\" : 7229.22, \"buy\" : 7229.22, \"sell\" : 7229.22, \"symbol\" : \"CHF\"},\n" +
            "  \"CLP\" : {\"15m\" : 4901295.98, \"last\" : 4901295.98, \"buy\" : 4901295.98, \"sell\" : 4901295.98, \"symbol\" : \"$\"},\n" +
            "  \"CNY\" : {\"15m\" : 48756.64, \"last\" : 48756.64, \"buy\" : 48756.64, \"sell\" : 48756.64, \"symbol\" : \"¥\"},\n" +
            "  \"DKK\" : {\"15m\" : 47482.06, \"last\" : 47482.06, \"buy\" : 47482.06, \"sell\" : 47482.06, \"symbol\" : \"kr\"},\n" +
            "  \"EUR\" : {\"15m\" : 6358.63, \"last\" : 6358.63, \"buy\" : 6358.63, \"sell\" : 6358.63, \"symbol\" : \"€\"},\n" +
            "  \"GBP\" : {\"15m\" : 5496.08, \"last\" : 5496.08, \"buy\" : 5496.08, \"sell\" : 5496.08, \"symbol\" : \"£\"},\n" +
            "  \"HKD\" : {\"15m\" : 56076.89, \"last\" : 56076.89, \"buy\" : 56076.89, \"sell\" : 56076.89, \"symbol\" : \"$\"},\n" +
            "  \"INR\" : {\"15m\" : 499645.61, \"last\" : 499645.61, \"buy\" : 499645.61, \"sell\" : 499645.61, \"symbol\" : \"₹\"},\n" +
            "  \"ISK\" : {\"15m\" : 870677.22, \"last\" : 870677.22, \"buy\" : 870677.22, \"sell\" : 870677.22, \"symbol\" : \"kr\"},\n" +
            "  \"JPY\" : {\"15m\" : 784371.51, \"last\" : 784371.51, \"buy\" : 784371.51, \"sell\" : 784371.51, \"symbol\" : \"¥\"},\n" +
            "  \"KRW\" : {\"15m\" : 8396560.53, \"last\" : 8396560.53, \"buy\" : 8396560.53, \"sell\" : 8396560.53, \"symbol\" : \"₩\"},\n" +
            "  \"NZD\" : {\"15m\" : 10837.14, \"last\" : 10837.14, \"buy\" : 10837.14, \"sell\" : 10837.14, \"symbol\" : \"$\"},\n" +
            "  \"PLN\" : {\"15m\" : 27326.25, \"last\" : 27326.25, \"buy\" : 27326.25, \"sell\" : 27326.25, \"symbol\" : \"zł\"},\n" +
            "  \"RUB\" : {\"15m\" : 465437.25, \"last\" : 465437.25, \"buy\" : 465437.25, \"sell\" : 465437.25, \"symbol\" : \"RUB\"},\n" +
            "  \"SEK\" : {\"15m\" : 68752.33, \"last\" : 68752.33, \"buy\" : 68752.33, \"sell\" : 68752.33, \"symbol\" : \"kr\"},\n" +
            "  \"SGD\" : {\"15m\" : 9738.68, \"last\" : 9738.68, \"buy\" : 9738.68, \"sell\" : 9738.68, \"symbol\" : \"$\"},\n" +
            "  \"THB\" : {\"15m\" : 225434.25, \"last\" : 225434.25, \"buy\" : 225434.25, \"sell\" : 225434.25, \"symbol\" : \"฿\"},\n" +
            "  \"TWD\" : {\"15m\" : 221138.81, \"last\" : 221138.81, \"buy\" : 221138.81, \"sell\" : 221138.81, \"symbol\" : \"NT$\"}\n" +
            "}";

    @Test
    public void shouldDeserializeCorrectly() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        BitcoinRates bitcoinRates = objectMapper.readValue(ratesString, BitcoinRates.class);

        BitcoinRate usd = bitcoinRates.getRate(Currency.getInstance("USD")).get();
        assertThat(usd.getBuy(), is(7144.79));
        assertThat(usd.getSell(), is(7140.79));
    }
}
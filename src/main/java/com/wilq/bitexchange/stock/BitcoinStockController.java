package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;

@RestController
@Slf4j
public class BitcoinStockController {


    @Autowired
    private BitcoinStockService service;

    @GetMapping("/rate/{currency}/latest")
    public BitcoinRate getLatestRate(@PathVariable String currency) {
        return service.getLatestRate(Currency.getInstance(currency));
    }

    @GetMapping("/rate/{currency}")
    public Collection<BitcoinRate> getLatestRate(
            @PathVariable String currency,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (endDate == null)
            endDate = LocalDate.now();

        return service.getRates(Currency.getInstance(currency), startDate, endDate);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    String handleBadRequest(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException", ex);
        return "Bad request";
    }
}

package com.wilq.bitexchange.stock;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
public class RateQueryResult {
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private Double rate;
    private String source;
    private String destination;
    private LocalDateTime time;

    public RateQueryResult(ArrayList<Object> values) {
        this.time = LocalDateTime.parse((String)values.get(0), ISO_ZONED_DATE_TIME);
        this.rate = (Double) values.get(1);
        this.source = (String) values.get(2);
        this.destination = (String) values.get(3);
    }
}

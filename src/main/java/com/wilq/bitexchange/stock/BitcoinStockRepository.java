package com.wilq.bitexchange.stock;

import com.wilq.bitexchange.stock.client.BitcoinRate;
import com.wilq.bitexchange.stock.client.BitcoinRates;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BitcoinStockRepository {

    private static final String BTC_CURRENCY = "BTC";

    //TODO group to get hourly/daily avg?
    private static final String FIND_CURRENCY_RATES_IN_RANGE =
            "select rate, source, destination " +
            "from rate " +
            "where time >= '%s' " +
            "and time < '%s' " +
            "and (source = '%s' or destination = '%s')";

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    public void save(BitcoinRates rates) {
        log.info("Storing to influxdb");
        long timestamp = rates.getFetchTimestamp().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();

        for (Map.Entry<String, BitcoinRate> rateEntry : rates.getRates().entrySet()) {
            BitcoinRate rate = rateEntry.getValue();
            String currency = rateEntry.getKey();
            influxDBTemplate.write(asPoint(timestamp, BTC_CURRENCY, currency, rate.getBuy()));
            influxDBTemplate.write(asPoint(timestamp, currency, BTC_CURRENCY, rate.getSell()));
        }
    }

    public Collection<BitcoinRate> findRatesInInclusiveDateRange(Currency currency, LocalDate startDate, LocalDate endDate) {
        String startTimestamp = startDate.toString();
        String endTimestamp = endDate.plusDays(1).toString();
        String currencyString = currency.toString();

        String query = String.format(FIND_CURRENCY_RATES_IN_RANGE, startTimestamp, endTimestamp, currencyString, currencyString);

        QueryResult queryResult = influxDBTemplate.query(new Query(query, influxDBTemplate.getDatabase()));

        List<RateQueryResult> results = queryResult.getResults().stream()
                .map(QueryResult.Result::getSeries)
                .flatMap(List::stream)
                .map(QueryResult.Series::getValues)
                .flatMap(List::stream)
                .map(v -> new RateQueryResult((ArrayList<Object>) v))
                .collect(Collectors.toList());

        return groupByTime(results);
    }

    private Collection<BitcoinRate> groupByTime(List<RateQueryResult> results) {
        Map<LocalDateTime, BitcoinRate> group = new HashMap<>();

        for (RateQueryResult result : results) {
            BitcoinRate groupedRate = group
                    .computeIfAbsent(result.getTime(),
                            k -> new BitcoinRate(0.0, 0.0, result.getTime()));

            Double rate = result.getRate();
            String source = result.getSource();
            if (BTC_CURRENCY.equals(source))
                groupedRate.setBuy(rate);
            else
                groupedRate.setSell(rate);
        }

        return group.values();
    }

    private Point asPoint(long timestamp, String source, String destination, Double rate) {
        return Point.measurement("rate")
                .time(timestamp, TimeUnit.MILLISECONDS)
                .tag("source", source)
                .tag("destination", destination)
                .addField("rate", rate)
                .build();
    }
}

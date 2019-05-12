package com.wilq.bitexchange.stock.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitcoinRate {
    private Double buy;
    private Double sell;
    private LocalDateTime fetchTimestamp;
}
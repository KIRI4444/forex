package com.example.domain.CoinGeckoAPI.coin;

import lombok.Data;

@Data
public class CoinMarketInfo {

    private String id;
    private String name;
    private String symbol;

    private MarketData market_data;
    private CoinImage image;

}

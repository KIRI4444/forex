package com.example.domain.CoinGeckoAPI.top10;

import lombok.Data;

@Data
public class Coin {

    private String id;
    private String name;
    private String symbol;
    private double current_price;
    private String image;
    private double price_change_percentage_24h;

}

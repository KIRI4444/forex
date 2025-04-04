package com.example.domain.CoinGeckoAPI.coin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MarketData {
    private CurrentPrice current_price;
    private double price_change_24h;
    private double price_change_percentage_24h;
    private double price_change_percentage_7d;
    private String formatted_total_volume;
    private String formatted_market_cap;
    private MarketCap market_cap;
    private TotalVolume total_volume;
    private Sparkline sparkline_7d;
}

package com.example.domain.CoinGeckoAPI.coin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MarketData {
    private CurrentPrice current_price;
    private double price_change_24h;
    private double price_change_percentage_24h;
    @SerializedName("sparkline_7d")
    private Sparkline sparkline_7d;
}

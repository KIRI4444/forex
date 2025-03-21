package com.example.domain.CoinGeckoAPI.coin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Sparkline {
    @SerializedName("price")
    private List<Double> price;
}

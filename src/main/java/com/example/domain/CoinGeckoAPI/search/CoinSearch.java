package com.example.domain.CoinGeckoAPI.search;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoinSearch {
    private String id;
    private String name;
    private String symbol;
    @SerializedName("large")
    private String image;
}

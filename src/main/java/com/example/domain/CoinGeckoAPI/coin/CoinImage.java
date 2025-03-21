package com.example.domain.CoinGeckoAPI.coin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoinImage {
    @SerializedName("large")
    private String image;
}

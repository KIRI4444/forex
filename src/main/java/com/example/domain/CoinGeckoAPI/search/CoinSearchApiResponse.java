package com.example.domain.CoinGeckoAPI.search;

import lombok.Data;

import java.util.List;

@Data
public class CoinSearchApiResponse {
    private List<CoinSearch> coins;
}

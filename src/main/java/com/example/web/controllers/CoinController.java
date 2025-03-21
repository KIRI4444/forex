package com.example.web.controllers;

import com.example.domain.CoinGeckoAPI.coin.CoinMarketInfo;
import com.example.domain.CoinGeckoAPI.top10.Coin;
import com.example.domain.CoinGeckoAPI.CoinGeckoResponse;
import com.example.domain.CoinGeckoAPI.search.CoinSearch;
import com.example.service.CoinsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@RequiredArgsConstructor
@Tag(name = "Coins Controller", description = "Coins API")
public class CoinController {

    private final CoinsService coinsService;

    @GetMapping("/top10")
    public CoinGeckoResponse<List<Coin>> getTop10CoinsByMarketCap () {
        return coinsService.getTop10Coins();
    }

    @GetMapping("/search")
    public CoinGeckoResponse<List<CoinSearch>> SearchCoinsByName(@RequestParam String query) {
        return coinsService.getCoinSearchResponse(query);
    }

    @GetMapping("/{id}")
    public CoinGeckoResponse<CoinMarketInfo> getCoinById(@PathVariable String id) {
        return coinsService.getCoinById(id);
    }
}

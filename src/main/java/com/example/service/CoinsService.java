package com.example.service;

import com.example.domain.CoinGeckoAPI.coin.CoinMarketInfo;
import com.example.domain.CoinGeckoAPI.top10.Coin;
import com.example.domain.CoinGeckoAPI.CoinGeckoResponse;
import com.example.domain.CoinGeckoAPI.search.CoinSearch;
import com.example.domain.CoinGeckoAPI.search.CoinSearchApiResponse;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

@Service
public interface CoinsService {
    Call<List<Coin>> sendTop10Request();
    <T> CoinGeckoResponse<T> parseResponse(Response<T> response);
    CoinGeckoResponse<List<Coin>> getTop10Coins();
    Call<CoinSearchApiResponse> sendCoinSearchRequest(String query);
    CoinGeckoResponse<List<CoinSearch>> getCoinSearchResponse(String query);
    CoinGeckoResponse<CoinMarketInfo> getCoinById(String id);
    Call<CoinMarketInfo> sendCoinMarketInfo(String id);
}

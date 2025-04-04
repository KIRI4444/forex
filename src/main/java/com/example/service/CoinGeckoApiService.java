package com.example.service;

import com.example.domain.CoinGeckoAPI.coin.CoinMarketInfo;
import com.example.domain.CoinGeckoAPI.top10.Coin;
import com.example.domain.CoinGeckoAPI.search.CoinSearchApiResponse;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

@Service
public interface CoinGeckoApiService {

    @GET("coins/markets")
    Call<List<Coin>> getCoinMarkets(
            @Query("x_cg_demo_api_key") String apiKey,
            @Query("vs_currency") String vsCurrency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("sparkline") boolean sparkline,
            @Query("price_change_percentage") String priceChangePercentage
    );

    @GET("search")
    Call<CoinSearchApiResponse> getCoinSearch(
            @Query("x_cg_demo_api_key") String apikey,
            @Query("query") String query
    );

    @GET("coins/{id}")
    Call<CoinMarketInfo> getCoinById(
            @Path("id") String id,
            @Query("x_cg_demo_api_key") String apiKey,
            @Query("tickers") String tickers,
            @Query("community_data") String community_data,
            @Query("developer_data") String developer_data,
            @Query("sparkline") String sparkline,
            @Query("localization") String localization
    );
}

package com.example.service.impl;

import com.example.domain.CoinGeckoAPI.CoinGeckoResponse;
import com.example.domain.CoinGeckoAPI.coin.CoinMarketInfo;
import com.example.domain.CoinGeckoAPI.coin.MarketData;
import com.example.domain.CoinGeckoAPI.search.CoinSearch;
import com.example.domain.CoinGeckoAPI.search.CoinSearchApiResponse;
import com.example.domain.CoinGeckoAPI.top10.Coin;
import com.example.service.CoinGeckoApiService;
import com.example.service.CoinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinsServiceImpl implements CoinsService {

    private final CoinGeckoApiService coinGeckoApiService;

    @Override
    public Call<List<Coin>> sendTop10Request() {
        String apiKey = "CG-CY7wN4U7H1FysQjEcyot8Yvc";
        String vsCurrency = "usd";
        String order = "market_cap_desc";
        int perPage = 10;
        boolean sparkline = true;
        String priceChangePercentage = "24h";

        return coinGeckoApiService.getCoinMarkets(
                apiKey,
                vsCurrency,
                order,
                perPage,
                sparkline,
                priceChangePercentage
        );
    }

    @Override
    public <T> CoinGeckoResponse<T> parseResponse(Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            return new CoinGeckoResponse<>(response.body(), true, null);
        } else {
            String errorMessage = response.message();
            if (response.errorBody() != null) {
                try {
                    errorMessage = response.errorBody().string();
                } catch (Exception e) {
                    errorMessage = "Failed to parse error body";
                }
            }
            return new CoinGeckoResponse<>(null, false, "Failed to parse data: " + errorMessage);
        }
    }

    @Override
    public CoinGeckoResponse<List<Coin>> getTop10Coins() {
        Call<List<Coin>> call = sendTop10Request();

        try {
            Response<List<Coin>> response = call.execute();
            return parseResponse(response);
        } catch (Exception e) {
            return new CoinGeckoResponse<>(null, false, "Network error: " + e.getMessage());
        }
    }

    @Override
    public Call<CoinSearchApiResponse> sendCoinSearchRequest(String query) {
        String apiKey = "CG-CY7wN4U7H1FysQjEcyot8Yvc";
        return coinGeckoApiService.getCoinSearch(
                apiKey,
                query
        );
    }

    @Override
    public CoinGeckoResponse<List<CoinSearch>> getCoinSearchResponse(String query) {
        Call<CoinSearchApiResponse> call = sendCoinSearchRequest(query);

        try {
            Response<CoinSearchApiResponse> response = call.execute();
            if (response.isSuccessful()) {
                List<CoinSearch> coins = response.body().getCoins();
                return parseResponse(Response.success(coins));
            } else {
                return new CoinGeckoResponse<>(null, false, "Network error: " + response.message());
            }

        } catch (Exception e) {
            return new CoinGeckoResponse<>(null, false, "Network error: " + e.getMessage());
        }
    }

    @Override
    public Call<CoinMarketInfo> sendCoinMarketInfo(String id) {
        String apiKey = "CG-CY7wN4U7H1FysQjEcyot8Yvc";
        String tickers = "false";
        String community_data = "false";
        String developer_data = "false";
        String sparkline = "true";
        String localization = "false";

        return coinGeckoApiService.getCoinById(
                id,
                apiKey,
                tickers,
                community_data,
                developer_data,
                sparkline,
                localization
        );
    }

    @Override
    public CoinGeckoResponse<CoinMarketInfo> getCoinById(String id) {
        Call<CoinMarketInfo> call = sendCoinMarketInfo(id);

        try {
            Response<CoinMarketInfo> response = call.execute();
            CoinMarketInfo coinMarketInfo = response.body();

            long totalVolume = coinMarketInfo.getMarket_data().getTotal_volume().getUsd();
            long marketCap = coinMarketInfo.getMarket_data().getMarket_cap().getUsd();
            String formattedMarketCap = formatNumber(marketCap);
            String formattedTotalVolume = formatNumber(totalVolume);

            MarketData marketData = coinMarketInfo.getMarket_data();
            marketData.setFormatted_total_volume(formattedTotalVolume);
            marketData.setFormatted_market_cap(formattedMarketCap);

            return parseResponse(response);
        } catch (Exception e) {
            return new CoinGeckoResponse<>(null, false, "Network error: " + e.getMessage());
        }
    }

    public static String formatNumber(long num) {
        if (num >= 1_000_000_000) {
            return String.format("%.2fB", num / 1_000_000_000.0).replaceAll("\\.?0*B$", "B");
        } else if (num >= 1_000_000) {
            return String.format("%.2fM", num / 1_000_000.0).replaceAll("\\.?0*M$", "M");
        } else if (num >= 1_000) {
            return String.format("%.2fK", num / 1_000.0).replaceAll("\\.?0*K$", "K");
        } else {
            return Long.toString(num);
        }
    }

}
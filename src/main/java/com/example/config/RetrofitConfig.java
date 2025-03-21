package com.example.config;

import com.example.service.CoinGeckoApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";

    @Bean
    public CoinGeckoApiService coinGeckoApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CoinGeckoApiService.class);
    }
}

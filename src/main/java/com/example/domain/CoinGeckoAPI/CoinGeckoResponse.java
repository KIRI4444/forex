package com.example.domain.CoinGeckoAPI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoinGeckoResponse<T> {
    private T data;
    private boolean success;
    private String errorMessage;
}

package com.index.bankTransfer.providers.flutterwave.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BankListResponse {
    String status;

    String message;

    List<FlutterwaveBank> data;

    @Data
    public static class FlutterwaveBank {
        Long id;
        String name;
        String code;
    }
}


package com.index.bankTransfer.providers.flutterwave.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyAccountResponse {
    String status;

    String message;

    VerifyAccountResponseData data;

    @Data
    public static class VerifyAccountResponseData {
        String account_name;
        String account_number;
    }
}

package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyTransactionResponse {
    String status;

    String message;

    VerifyTransactionResponseData data;

    @Data
    public static class VerifyTransactionResponseData {
        Long integration;
        VerifyTransactionResponseDataRecipient recipient;
        String domain;
        Long amount;
        String currency;
        String reference;
        String source;
        String source_details;
        String reason;
        String status;
        String failures;
        String transfer_code;
        String titan_code;
        String transferred_at;
        Long id;
        String createdAt;
        String updatedAt;
    }

    @Data
    public static class VerifyTransactionResponseDataRecipient {
        String domain;
        String type;
        String currency;
        String name;
        VerifyTransactionResponseDataDetails details;
        String description;
        String metadata;
        String recipient_code;
        Boolean active;
        String email;
        Long id;
        Long integration;
        String createdAt;
        String updatedAt;
    }

    @Data
    public static class VerifyTransactionResponseDataDetails {
        String account_number;
        String account_name;
        String bank_code;
        String bank_name;
    }
}



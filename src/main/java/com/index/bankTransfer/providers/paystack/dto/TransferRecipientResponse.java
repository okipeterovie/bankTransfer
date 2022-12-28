package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferRecipientResponse {
    String status;

    String message;

    TransferRecipientData data;

    @Data
    public static class TransferRecipientData {
        Boolean active;
        String createdAt;
        String currency;
        String description;
        String domain;
        String email;
        Long id;
        Long integration;
        Object metadata;
        String name;
        String recipient_code;
        String type;
        String updatedAt;
        Boolean is_deleted;
        Boolean isDeleted;
        TransferRecipientDataDetails details;
    }

    @Data
    public static class TransferRecipientDataDetails {
        String authorization_code;
        String account_number;
        String account_name;
        String bank_code;
        String bank_name;
    }
}

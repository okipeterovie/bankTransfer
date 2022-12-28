package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferResponse {
    String status;

    String message;

    TransferData data;

    @Data
    public static class TransferData {
       String reference;
       Long integration;
       String domain;
       Long amount;
       String currency;
       String source;
       String reason;
       Long recipient;
       String status;
       String transfer_code;
       Long id;
       String createdAt;
       String updatedAt;
    }
}

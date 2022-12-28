package com.index.bankTransfer.providers.flutterwave.dto;

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
       Long id;
       String account_number;
       String bank_code;
       String full_name;
       String created_at;
       String currency;
       String debit_currency;
       Long amount;
       Float fee;
       String status;
       String reference;
       Object meta;
       String narration;
       String complete_message;
       Long requires_approval;
       Long is_approved;
       String bank_name;
    }
}

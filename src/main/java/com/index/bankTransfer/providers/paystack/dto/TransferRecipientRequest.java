package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferRecipientRequest {
    String type;
    String name;
    String account_number;
    String bank_code;
    String currency;
}

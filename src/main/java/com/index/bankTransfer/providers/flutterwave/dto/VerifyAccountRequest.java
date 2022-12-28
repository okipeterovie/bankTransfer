package com.index.bankTransfer.providers.flutterwave.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyAccountRequest {
    String account_number;

    String account_bank;
}

package com.index.bankTransfer.providers.flutterwave.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferRequest {
   String account_bank;
   String account_number;
   Long amount;
   String narration;
   String currency;
   String reference;
   String callback_url;
   String debit_currency;
}

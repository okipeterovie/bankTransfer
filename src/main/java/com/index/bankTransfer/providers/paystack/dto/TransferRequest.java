package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferRequest {
   String source;
   String reason;
   Long amount;
   String reference;
   String recipient;
}

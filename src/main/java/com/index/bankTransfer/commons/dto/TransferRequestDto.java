package com.index.bankTransfer.commons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Arrays;
import java.util.Optional;

@Data
@Builder
public class TransferRequestDto {

    String amount;
    String currencyCode;
    String narration;
    String beneficiaryAccountNumber;
    String beneficiaryAccountName;
    String beneficiaryBankCode;
    String transactionReference;
    Long maxRetryAttempt;
    String callBackUrl;
    String provider;

    @Tolerate
    public TransferRequestDto() {

    }
}

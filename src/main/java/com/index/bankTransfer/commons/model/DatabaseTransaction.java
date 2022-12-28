package com.index.bankTransfer.commons.model;

import com.index.bankTransfer.commons.dto.TransferResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Arrays;
import java.util.Optional;

@Data
@Builder
public class DatabaseTransaction {

    String amount;
    String provider;
    String beneficiaryAccountNumber;
    String beneficiaryAccountName;
    String beneficiaryBankCode;
    String transactionReference;
    String transactionDateTime;
    String currencyCode;
    String responseMessage;
    String responseCode;
    String callbackResponse;
    String errorResponse;
    TransferResponseDto.TransferStatus status;

    @Tolerate
    public DatabaseTransaction() {

    }
}

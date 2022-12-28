package com.index.bankTransfer.commons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Arrays;
import java.util.Optional;

@Data
@Builder
public class TransferResponseDto {

    String amount;
    String beneficiaryAccountNumber;
    String beneficiaryAccountName;
    String beneficiaryBankCode;
    String transactionReference;
    String transactionDateTime;
    String currencyCode;
    String responseMessage;
    String responseCode;
    String sessionId;
    TransferStatus status;


    @Tolerate
    public TransferResponseDto() {

    }

    public enum TransferStatus {
        CREATED("CREATED"),
        PENDING("PENDING"),
        SUCCESS("SUCCESS"),
        FAILURE("FAILURE"),
        RETRY("RETRY");

        private final String name;

        TransferStatus(String name) {
            this.name = name;
        }

        public static Optional<TransferStatus> getTransferStatusByName(String value) {
            return Arrays.stream(TransferStatus.values())
                .filter(transferStatus -> transferStatus.name.equals(value.toLowerCase()))
                .findFirst();
        }

        public String getName() {
            return name;
        }


    }
}

package com.index.bankTransfer.commons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class AccountVerificationResponseDto {
        String accountNumber;
        String accountName;
        String bankCode;
        String bankName;
        @Tolerate
        public AccountVerificationResponseDto() {

        }
}

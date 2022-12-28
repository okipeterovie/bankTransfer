package com.index.bankTransfer.commons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class AccountVerificationRequestDto {
        String accountNumber;
        String code;
        @Tolerate
        public AccountVerificationRequestDto() {

        }
}

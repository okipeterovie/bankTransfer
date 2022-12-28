package com.index.bankTransfer.commons.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class BankDto {
        String bankName;
        String code;
        String longCode;
        @Tolerate
        public BankDto() {

        }
}

package com.index.bankTransfer.providers.flutterwave.mapper;

import com.index.bankTransfer.commons.dto.AccountVerificationResponseDto;
import com.index.bankTransfer.providers.flutterwave.dto.VerifyAccountResponse;

public class VerifyAccountMapper {

    public static AccountVerificationResponseDto mapToDomain(final VerifyAccountResponse.VerifyAccountResponseData model) {
        return AccountVerificationResponseDto.builder()
                .accountNumber(model.getAccount_number() != null ? model.getAccount_number() : "")
                .accountName(model.getAccount_name() != null ? model.getAccount_name() : "")
                .bankCode("")
                .bankName("")
                .build();
    }
}

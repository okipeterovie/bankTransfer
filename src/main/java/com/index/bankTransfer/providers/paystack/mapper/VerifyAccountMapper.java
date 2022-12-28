package com.index.bankTransfer.providers.paystack.mapper;

import com.index.bankTransfer.commons.dto.AccountVerificationResponseDto;
import com.index.bankTransfer.commons.dto.BankDto;
import com.index.bankTransfer.providers.paystack.dto.BankListResponse;
import com.index.bankTransfer.providers.paystack.dto.VerifyAccountResponse;

public class VerifyAccountMapper {

    public static AccountVerificationResponseDto mapToDomain(final VerifyAccountResponse.VerifyAccountResponseData model) {
        return AccountVerificationResponseDto.builder()
                .accountNumber(model.getAccount_number() != null ? model.getAccount_number() : "")
                .accountName(model.getAccount_name() != null ? model.getAccount_name() : "")
                .bankCode(model.getBank_id() != null ? model.getBank_id() : "")
                .bankName("")
                .build();
    }
}

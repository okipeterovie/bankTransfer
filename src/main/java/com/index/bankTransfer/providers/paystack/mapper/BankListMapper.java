package com.index.bankTransfer.providers.paystack.mapper;

import com.index.bankTransfer.commons.dto.BankDto;
import com.index.bankTransfer.providers.paystack.dto.BankListResponse;

public class BankListMapper {

    public static BankDto mapToDomain(final BankListResponse.PaystackBank model) {
        return BankDto.builder()
                .code(model.getCode() != null ? model.getCode() : "")
                .bankName(model.getName() != null ? model.getName() : "")
                .longCode(model.getLongcode() != null ? model.getLongcode() : "")
                .build();
    }
}

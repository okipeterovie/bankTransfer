package com.index.bankTransfer.providers.flutterwave.mapper;

import com.index.bankTransfer.commons.dto.BankDto;
import com.index.bankTransfer.providers.flutterwave.dto.BankListResponse;

public class BankListMapper {

    public static BankDto mapToDomain(final BankListResponse.FlutterwaveBank model) {
        return BankDto.builder()
                .code(model.getCode() != null ? model.getCode() : "")
                .bankName(model.getName() != null ? model.getName() : "")
                .longCode("")
                .build();
    }
}

package com.index.bankTransfer.providers.flutterwave.mapper;

import com.index.bankTransfer.commons.dto.TransferRequestDto;
import com.index.bankTransfer.commons.dto.TransferResponseDto;
import com.index.bankTransfer.providers.flutterwave.dto.TransferResponse;

import java.util.Objects;

public class TransferMapper {

    public static TransferResponseDto mapToDomain(final TransferResponse.TransferData model) {
        return TransferResponseDto.builder()
            .amount(model.getAmount() != null ? String.valueOf(model.getAmount()) : "")
            .beneficiaryAccountNumber(model.getAccount_number())
            .beneficiaryAccountName(model.getFull_name())
            .beneficiaryBankCode(model.getBank_code())
            .transactionReference(model.getReference())
            .transactionDateTime(model.getCreated_at())
            .currencyCode(model.getCurrency())
            .responseMessage(model.getStatus())
            .responseCode("")
            .sessionId("")
            .status(getStatus(model.getStatus()))
            .build();
    }

    public static TransferResponseDto.TransferStatus getStatus(String transferStatus){
        if(Objects.equals(transferStatus.toLowerCase(), "success")) {
            return TransferResponseDto.TransferStatus.SUCCESS;
        } else {
            return TransferResponseDto.TransferStatus.PENDING;
        }
    }
}

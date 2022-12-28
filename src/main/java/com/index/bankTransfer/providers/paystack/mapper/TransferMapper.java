package com.index.bankTransfer.providers.paystack.mapper;

import com.index.bankTransfer.commons.dto.AccountVerificationResponseDto;
import com.index.bankTransfer.commons.dto.TransferRequestDto;
import com.index.bankTransfer.commons.dto.TransferResponseDto;
import com.index.bankTransfer.providers.paystack.dto.TransferResponse;
import com.index.bankTransfer.providers.paystack.dto.VerifyAccountResponse;

import java.util.Objects;

public class TransferMapper {

    public static TransferResponseDto mapToDomain(final TransferResponse.TransferData model, final TransferRequestDto transferRequestDto) {
        return TransferResponseDto.builder()
            .amount(model.getAmount() != null ? String.valueOf(model.getAmount()) : "")
            .beneficiaryAccountNumber(transferRequestDto.getBeneficiaryAccountNumber())
            .beneficiaryAccountName(transferRequestDto.getBeneficiaryAccountName())
            .beneficiaryBankCode(transferRequestDto.getBeneficiaryBankCode())
            .transactionReference(model.getReference())
            .transactionDateTime(model.getCreatedAt())
            .currencyCode(model.getCurrency())
            .responseMessage(model.getStatus())
            .responseCode("")
            .sessionId("")
            .status(getStatus(model.getStatus()))
            .build();
    }

    public static TransferResponseDto.TransferStatus getStatus(String transferStatus){
        if(Objects.equals(transferStatus, "success")) {
            return TransferResponseDto.TransferStatus.SUCCESS;
        } else {
            return TransferResponseDto.TransferStatus.PENDING;
        }
    }
}

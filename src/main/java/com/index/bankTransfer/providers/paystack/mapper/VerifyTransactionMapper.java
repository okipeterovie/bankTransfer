package com.index.bankTransfer.providers.paystack.mapper;

import com.index.bankTransfer.commons.dto.TransferResponseDto;
import com.index.bankTransfer.providers.paystack.dto.VerifyTransactionResponse;

import java.util.Objects;

public class VerifyTransactionMapper {

    public static TransferResponseDto mapToDomain(final VerifyTransactionResponse.VerifyTransactionResponseData model) {
        return TransferResponseDto.builder()
            .amount(model.getAmount() != null ? String.valueOf(model.getAmount()) : "")
            .beneficiaryAccountNumber(model.getRecipient() != null
                && model.getRecipient().getDetails() != null
                && model.getRecipient().getDetails().getAccount_number()!= null
                ?
                model.getRecipient().getDetails().getAccount_number()
                :
                "")
            .beneficiaryAccountName(model.getRecipient() != null
                && model.getRecipient().getDetails() != null
                && model.getRecipient().getDetails().getAccount_name()!= null
                ?
                model.getRecipient().getDetails().getAccount_name()
                :
                "")
            .beneficiaryBankCode(model.getRecipient() != null
                && model.getRecipient().getDetails() != null
                && model.getRecipient().getDetails().getBank_code()!= null
                ?
                model.getRecipient().getDetails().getBank_code()
                :
                "")
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

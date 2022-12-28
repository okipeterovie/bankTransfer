package com.index.bankTransfer.service;

import com.index.bankTransfer.commons.dto.*;
import com.index.bankTransfer.commons.model.DatabaseTransaction;
import com.index.bankTransfer.providers.ProviderSelectionService;
import com.index.bankTransfer.providers.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankTransferServiceImpl implements BankTransferService{

    private final ProviderSelectionService providerSelectionService;

    HashMap<String, DatabaseTransaction> inMemory = new HashMap<>();

    @Override
    public List<BankDto> getBankList(String serviceProvider) {
        ProviderService currentTransferService = providerSelectionService.currentTransferService(serviceProvider);

        log.info("getting bank list with service provider [" + serviceProvider + "] using vendor [" + currentTransferService.getProvider() + "]");

        return currentTransferService.getBankList();
    }

    @Override
    public AccountVerificationResponseDto verifyAccount(String serviceProvider, AccountVerificationRequestDto accountVerificationRequestDto) {
        ProviderService currentTransferService = providerSelectionService.currentTransferService(serviceProvider);

        log.info("verifying account  with service provider [" + serviceProvider + "] using vendor [" + currentTransferService.getProvider() + "]");

        return currentTransferService.verifyAccountNumber(accountVerificationRequestDto);
    }

    @Override
    @Async("asyncTaskExecutor")
    public CompletableFuture<TransferResponseDto> transfer(String serviceProvider, TransferRequestDto transferRequestDto) {

        CompletableFuture<TransferResponseDto> task = new CompletableFuture<>();

        TransferResponseDto transferResponseDto = new TransferResponseDto();
        DatabaseTransaction databaseTransaction = inMemory.get(transferRequestDto.getTransactionReference());

        try {
            ProviderService currentTransferService = providerSelectionService.currentTransferService(serviceProvider);
            log.info("processing transfer request with service provider [" + serviceProvider + "] using vendor [" + currentTransferService.getProvider() + "]");

            log.info(String.valueOf(inMemory));
            log.info("transfer request dto is: ["+transferRequestDto +"]");
            if(databaseTransaction == null) {
                log.info("processing new transfer request with reference [" + transferRequestDto.getTransactionReference() + "]");
                inMemory.put(transferRequestDto.getTransactionReference(), toDatabaseTransaction(transferRequestDto, serviceProvider));
                transferResponseDto = currentTransferService.transfer(transferRequestDto);
            } else if(databaseTransaction.getStatus() == TransferResponseDto.TransferStatus.SUCCESS) {
                log.info("transaction with reference [" + transferRequestDto.getTransactionReference() + "] already successful");
            } else if(databaseTransaction.getStatus() != TransferResponseDto.TransferStatus.SUCCESS) {
                log.info("transaction with reference [" + transferRequestDto.getTransactionReference() + "] already in the database, now requerying");
                transferResponseDto = currentTransferService.reQueryTransfer(transferRequestDto);
            }
            task.complete(transferResponseDto);
        } catch (Exception ex){
            log.info(ex.getMessage());
            databaseTransaction = inMemory.get(transferRequestDto.getTransactionReference());
            databaseTransaction.setErrorResponse(ex.getMessage());
            inMemory.put(transferRequestDto.getTransactionReference(), databaseTransaction);
            log.info(String.valueOf(databaseTransaction));
        } finally{
            log.info("finally block: " + inMemory);
        }

        return task;
    }

    @Override
    public TransferResponseDto verifyTransaction(String transactionReference) {
        DatabaseTransaction databaseTransaction = inMemory.get(transactionReference);

        if(databaseTransaction == null){
            throw new InvalidParameterException("no database transaction for reference: ["+ transactionReference + "]");
        }

        ProviderService currentTransferService = providerSelectionService.currentTransferService(databaseTransaction.getProvider());
        log.info("processing transfer request with service provider [" + databaseTransaction.getProvider()
            + "] using vendor [" + currentTransferService.getProvider() + "]");


        TransferResponseDto transferResponseDto = new TransferResponseDto();
        if(databaseTransaction.getStatus() == TransferResponseDto.TransferStatus.SUCCESS){
            transferResponseDto = toTransferResponseDto(databaseTransaction);
        } else {
            transferResponseDto = currentTransferService.verifyTransaction(transactionReference);
        }

        databaseTransaction.setStatus(transferResponseDto.getStatus());
        inMemory.put(transactionReference, databaseTransaction);
        log.info(String.valueOf(databaseTransaction));

        return transferResponseDto;
    }

    public DatabaseTransaction toDatabaseTransaction(TransferRequestDto transferRequestDto, String serviceProvider){
        return DatabaseTransaction.builder()
            .amount(transferRequestDto.getAmount())
            .provider(serviceProvider)
            .beneficiaryAccountNumber(transferRequestDto.getBeneficiaryAccountNumber())
            .beneficiaryAccountName(transferRequestDto.getBeneficiaryAccountName())
            .beneficiaryBankCode(transferRequestDto.getBeneficiaryBankCode())
            .transactionReference(transferRequestDto.getTransactionReference())
            .transactionDateTime("")
            .currencyCode(transferRequestDto.getCurrencyCode())
            .responseMessage("")
            .responseCode("")
            .callbackResponse("")
            .status(TransferResponseDto.TransferStatus.CREATED)
            .build();
    }

    public DatabaseTransaction toDatabaseTransaction(TransferResponseDto transferResponseDto, String serviceProvider){
        return DatabaseTransaction.builder()
            .amount(transferResponseDto.getAmount())
            .provider(serviceProvider)
            .beneficiaryAccountNumber(transferResponseDto.getBeneficiaryAccountNumber())
            .beneficiaryAccountName(transferResponseDto.getBeneficiaryAccountName())
            .beneficiaryBankCode(transferResponseDto.getBeneficiaryBankCode())
            .transactionReference(transferResponseDto.getTransactionReference())
            .transactionDateTime(transferResponseDto.getTransactionDateTime())
            .currencyCode(transferResponseDto.getCurrencyCode())
            .responseMessage(transferResponseDto.getResponseMessage())
            .responseCode(transferResponseDto.getResponseCode())
            .callbackResponse("")
            .status(transferResponseDto.getStatus())
            .build();
    }

    public TransferResponseDto toTransferResponseDto(DatabaseTransaction databaseTransaction){
        return TransferResponseDto.builder()
            .amount(databaseTransaction.getAmount())
            .beneficiaryAccountNumber(databaseTransaction.getBeneficiaryAccountNumber())
            .beneficiaryAccountName(databaseTransaction.getBeneficiaryAccountName())
            .beneficiaryBankCode(databaseTransaction.getBeneficiaryBankCode())
            .transactionReference(databaseTransaction.getTransactionReference())
            .transactionDateTime(databaseTransaction.getTransactionDateTime())
            .currencyCode(databaseTransaction.getCurrencyCode())
            .responseMessage(databaseTransaction.getResponseMessage())
            .responseCode(databaseTransaction.getResponseCode())
            .status(databaseTransaction.getStatus())
            .build();
    }
}

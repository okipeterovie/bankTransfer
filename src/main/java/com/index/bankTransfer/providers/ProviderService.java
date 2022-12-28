package com.index.bankTransfer.providers;

import com.index.bankTransfer.commons.dto.*;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProviderService {

  ServiceProvider getProvider();

  List<BankDto> getBankList();

  AccountVerificationResponseDto verifyAccountNumber(AccountVerificationRequestDto accountVerificationRequestDto);

  TransferResponseDto transfer(TransferRequestDto transferRequestDto);

  TransferResponseDto reQueryTransfer(TransferRequestDto transferRequestDto);

  TransferResponseDto verifyTransaction(String transactionReference);




}

package com.index.bankTransfer.service;

import com.index.bankTransfer.commons.dto.*;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BankTransferService {

  List<BankDto> getBankList(String serviceProvider);

  AccountVerificationResponseDto verifyAccount(String serviceProvider, AccountVerificationRequestDto accountVerificationRequestDto);

  @Async("asyncTaskExecutor")
  CompletableFuture<TransferResponseDto> transfer(String serviceProvider, TransferRequestDto transferRequestDto);

  TransferResponseDto verifyTransaction(String transactionReference);


}

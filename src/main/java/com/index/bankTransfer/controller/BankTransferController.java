package com.index.bankTransfer.controller;

import com.index.bankTransfer.commons.Response;
import com.index.bankTransfer.commons.dto.*;
import com.index.bankTransfer.service.BankTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/core-banking")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankTransferController {


  private final BankTransferService bankTransferService;

  @Value("${vendors.default}")
  String defaultVendor;

  @CrossOrigin
  @GetMapping("/health")
  public ResponseEntity<Response<?>> healthCheck(){
    return new ResponseEntity<>(new Response<>(true, "Welcome to Index Bank Transfer Service"), HttpStatus.OK);
  }

  @CrossOrigin
  @GetMapping("/banks")
  public ResponseEntity<Response<?>> bankList(
          @RequestParam(value = "provider", required = false) final String provider){
    try {

      String providerToBeUsed = provider != null ? provider : defaultVendor;
      List<BankDto> bankDtoList =  bankTransferService.getBankList(providerToBeUsed);
      return new ResponseEntity<>(new Response<>(true, bankDtoList), HttpStatus.OK);

    } catch (Exception ex){

      ex.printStackTrace();
      return new ResponseEntity<>(new Response<>(false, ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

  }

  @CrossOrigin
  @GetMapping("/validateBankAccount")
  public ResponseEntity<Response<?>> validateBankAccount(
      @RequestParam(value = "accountNumber") final String accountNumber,
      @RequestParam(value = "bankCode") final String bankCode,
      @RequestParam(value = "provider", required = false) final String provider){

    try {

      String providerToBeUsed = provider != null ? provider : defaultVendor;
      AccountVerificationRequestDto accountVerificationRequestDto = new AccountVerificationRequestDto();
      accountVerificationRequestDto.setAccountNumber(accountNumber);
      accountVerificationRequestDto.setCode(bankCode);

      AccountVerificationResponseDto accountVerificationResponseDto =  bankTransferService.verifyAccount(providerToBeUsed, accountVerificationRequestDto);
      return new ResponseEntity<>(new Response<>(true, accountVerificationResponseDto), HttpStatus.OK);

    } catch (Exception ex){

      ex.printStackTrace();
      return new ResponseEntity<>(new Response<>(false, ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

  }

  @CrossOrigin
  @PostMapping("/bankTransfer")
  public ResponseEntity<Response<?>> bankTransfer(@RequestBody final TransferRequestDto transferRequestDto) {

    try {

      String providerToBeUsed = transferRequestDto.getProvider() != null ? transferRequestDto.getProvider() : defaultVendor;

      bankTransferService.transfer(providerToBeUsed, transferRequestDto);

      return new ResponseEntity<>(new Response<>(true, "Transaction is in process"), HttpStatus.OK);

    } catch (Exception ex){
      ex.printStackTrace();
      return new ResponseEntity<>(new Response<>(false, ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

  }

  @CrossOrigin
  @GetMapping("/transaction/{transactionReference}")
  public ResponseEntity<Response<?>> validateBankAccount(
      @PathVariable String transactionReference ){

    try {

      TransferResponseDto transferResponseDto =  bankTransferService.verifyTransaction(transactionReference);
      return new ResponseEntity<>(new Response<>(true, transferResponseDto), HttpStatus.OK);

    } catch (Exception ex){

      ex.printStackTrace();
      return new ResponseEntity<>(new Response<>(false, ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

  }
}

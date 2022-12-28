package com.index.bankTransfer.providers.paystack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.index.bankTransfer.commons.RestTemplateUtils;
import com.index.bankTransfer.commons.dto.*;
import com.index.bankTransfer.providers.ProviderService;
import com.index.bankTransfer.providers.ServiceProvider;
import com.index.bankTransfer.providers.paystack.dto.*;
import com.index.bankTransfer.providers.paystack.mapper.BankListMapper;
import com.index.bankTransfer.providers.paystack.mapper.TransferMapper;
import com.index.bankTransfer.providers.paystack.mapper.VerifyAccountMapper;
import com.index.bankTransfer.providers.paystack.mapper.VerifyTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaystackService implements ProviderService {

    private final RestTemplate restTemplate;

    @Value("${vendors.paystack.secret-key}")
    String secretKey;

    @Value("${vendors.paystack.base-url}")
    String baseUrl;

    private static final String BANKS_PATH = "%s/bank";

    private static final String VERIFY_ACCOUNT_NUMBER = "%s/bank/resolve";

    private static final String TRANSFER_RECIPIENT = "%s/transferrecipient";

    private static final String TRANSFER = "%s/transfer";

    private static final String VERIFY_TRANSACTION = "%s/transfer/%s";

    @Override
    public ServiceProvider getProvider() {
        return ServiceProvider.PAYSTACK;
    }

    @Override
    public List<BankDto> getBankList() {

        final String requestUrl = String.format(BANKS_PATH, baseUrl);
        try {
            final HttpHeaders headers =
                    RestTemplateUtils.createPaystackHeaders(secretKey);
            final HttpEntity<Object> entity = new HttpEntity<>(headers);
            final ResponseEntity<BankListResponse> exchange =
                    restTemplate.exchange(requestUrl, HttpMethod.GET, entity, BankListResponse.class);

            log.info("bank list response from [" + getProvider() +"] is + [" + exchange +"]");

            List<BankDto> bankDtoList = new ArrayList<>();

            if(exchange.getBody()!= null && exchange.getBody().getData() != null){
                bankDtoList = exchange.getBody().getData().stream().map(BankListMapper::mapToDomain).toList();
            }

            return bankDtoList;
        } catch (final HttpClientErrorException ex) {
            ex.printStackTrace();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            throw new InvalidParameterException(ex.getResponseBodyAsString());
        }

    }

    @Override
    public AccountVerificationResponseDto verifyAccountNumber(AccountVerificationRequestDto accountVerificationRequestDto) {
        if(accountVerificationRequestDto.getAccountNumber() == null || Objects.equals(accountVerificationRequestDto.getAccountNumber(), "")
        || accountVerificationRequestDto.getCode() == null || Objects.equals(accountVerificationRequestDto.getCode(), "")){
            throw new InvalidParameterException("Account Number or Code cannot be null or empty");
        }
        final String requestUrl = String.format(VERIFY_ACCOUNT_NUMBER, baseUrl);

        try {
            UriComponentsBuilder builder =
                    UriComponentsBuilder.fromHttpUrl(requestUrl)
                            .queryParam("account_number", accountVerificationRequestDto.getAccountNumber())
                            .queryParam("bank_code", accountVerificationRequestDto.getCode());

            final HttpHeaders headers =
                    RestTemplateUtils.createPaystackHeaders(secretKey);
            final HttpEntity<Object> entity = new HttpEntity<>(headers);
            final ResponseEntity<VerifyAccountResponse> exchange =
                    restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, VerifyAccountResponse.class);

            log.info("account number verification response from [" + getProvider() +"] is + [" + exchange +"]");

            AccountVerificationResponseDto accountVerificationResponseDto = new AccountVerificationResponseDto();

            if(exchange.getBody()!= null){
                accountVerificationResponseDto = VerifyAccountMapper.mapToDomain(exchange.getBody().getData());
            }

            return accountVerificationResponseDto;
        } catch (final HttpClientErrorException ex) {
            ex.printStackTrace();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            throw new InvalidParameterException(ex.getResponseBodyAsString());
        }
    }

    @Override
    public TransferResponseDto transfer(TransferRequestDto transferRequestDto) {
        if(transferRequestDto.getAmount() == null || Objects.equals(transferRequestDto.getAmount(), "")
            || transferRequestDto.getCurrencyCode() == null || Objects.equals(transferRequestDto.getCurrencyCode(), "")
            || transferRequestDto.getNarration() == null || Objects.equals(transferRequestDto.getNarration(), "")
            || transferRequestDto.getBeneficiaryAccountName() == null || Objects.equals(transferRequestDto.getBeneficiaryAccountName(), "")
            || transferRequestDto.getBeneficiaryAccountNumber() == null || Objects.equals(transferRequestDto.getBeneficiaryAccountNumber(), "")
            || transferRequestDto.getBeneficiaryBankCode() == null || Objects.equals(transferRequestDto.getBeneficiaryBankCode(), "")
            || transferRequestDto.getTransactionReference() == null || Objects.equals(transferRequestDto.getTransactionReference(), "")){
            throw new InvalidParameterException("request body for transfer request is not fulfilled. one or more of " +
                "[amount, currency code, narration, beneficiary account Name, beneficiary account number, beneficiary " +
                " bank code, transaction reference] is missing");
        }

        if(!transferRequestDto.getAmount().matches("[0-9]+")){
            throw new InvalidParameterException("request body for transfer request is not fulfilled. amount cannot contain letters");
        };

        String type = transferRequestDto.getBeneficiaryBankCode().matches("[a-zA-Z]+") ? "mobile_money" : "nuban";

        TransferRecipientRequest transferRecipientRequest = TransferRecipientRequest.builder()
            .type(type).account_number(transferRequestDto.getBeneficiaryAccountNumber()).name(transferRequestDto.getBeneficiaryAccountName())
            .bank_code(transferRequestDto.getBeneficiaryBankCode()).currency(transferRequestDto.getCurrencyCode())
            .build();

        TransferRecipientResponse  transferRecipientResponse = generateTransferRecipient(transferRecipientRequest);

        TransferResponseDto transferResponseDto = new TransferResponseDto();

        if(transferRecipientResponse.getData() != null && transferRecipientResponse.getData().getRecipient_code() != null){
            final String requestUrl = String.format(TRANSFER, baseUrl);

            TransferRequest transferRequest = TransferRequest.builder()
                .source("balance").reason(transferRequestDto.getNarration()).amount(Long.valueOf(transferRequestDto.getAmount()))
                .reference(transferRequestDto.getTransactionReference()).recipient(transferRecipientResponse.getData().getRecipient_code())
                .build();

            try {

                final HttpHeaders headers =
                    RestTemplateUtils.createPaystackHeaders(secretKey);
                final HttpEntity<Object> entity = new HttpEntity<>(transferRequest, headers);
                final ResponseEntity<TransferResponse> exchange =
                    restTemplate.exchange(requestUrl, HttpMethod.POST, entity, TransferResponse.class);

                log.info("transfer request [" + transferRequest +"] with provider [" + getProvider() +"] has a response of + [" + exchange +"]");
                if (exchange.getBody() != null && exchange.getBody().getData() != null) {
                    transferResponseDto = TransferMapper.mapToDomain(exchange.getBody().getData(), transferRequestDto);
                }
            } catch (final HttpClientErrorException ex) {
                ex.printStackTrace();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                throw new InvalidParameterException("could not transfer because: ["  + ex.getResponseBodyAsString() + "]");
            }
        }

        return transferResponseDto;
    }

    @Override
    public TransferResponseDto reQueryTransfer(TransferRequestDto transferRequestDto) {
        TransferResponseDto transferResponseDto = verifyTransaction(transferRequestDto.getTransactionReference());
        if(transferResponseDto.getStatus() == TransferResponseDto.TransferStatus.SUCCESS){
            return transferResponseDto;
        }
        return transfer(transferRequestDto);
    }

    @Override
    public TransferResponseDto verifyTransaction(String transactionReference) {
        if(transactionReference == null || Objects.equals(transactionReference, "")){
            throw new InvalidParameterException("Transaction Reference cannot be null or empty");
        }
        final String requestUrl = String.format(VERIFY_TRANSACTION, baseUrl, transactionReference);

        try {

            final HttpHeaders headers =
                RestTemplateUtils.createPaystackHeaders(secretKey);
            final HttpEntity<Object> entity = new HttpEntity<>(headers);
            final ResponseEntity<VerifyTransactionResponse> exchange =
                restTemplate.exchange(requestUrl, HttpMethod.GET, entity, VerifyTransactionResponse.class);

            log.info("transaction verification response from [" + getProvider() +"] is + [" + exchange +"]");

            TransferResponseDto transferResponseDto = new TransferResponseDto();

            if(exchange.getBody()!= null){
                transferResponseDto = VerifyTransactionMapper.mapToDomain(exchange.getBody().getData());
            }

            return transferResponseDto;
        } catch (final HttpClientErrorException ex) {
            ex.printStackTrace();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            throw new InvalidParameterException(ex.getResponseBodyAsString());
        }
    }


    public TransferRecipientResponse generateTransferRecipient(TransferRecipientRequest transferRecipientRequest) {

        if(transferRecipientRequest.getType() == null || Objects.equals(transferRecipientRequest.getType(), "")
            || transferRecipientRequest.getName() == null || Objects.equals(transferRecipientRequest.getName(), "")
            || transferRecipientRequest.getAccount_number() == null || Objects.equals(transferRecipientRequest.getAccount_number(), "")
            || transferRecipientRequest.getBank_code() == null || Objects.equals(transferRecipientRequest.getBank_code(), "")
            || transferRecipientRequest.getCurrency() == null || Objects.equals(transferRecipientRequest.getCurrency(), "")){
            throw new InvalidParameterException("request body for transfer recipient is not fulfilled. one or more of " +
                "[type, name, account_number, bank_code, currency] is missing");
        }

        log.info("transfer recipient request [" + transferRecipientRequest +"] with provider [" + getProvider() +"]");

        final String requestUrl = String.format(TRANSFER_RECIPIENT, baseUrl);

        try {

            final HttpHeaders headers =
                RestTemplateUtils.createPaystackHeaders(secretKey);
            final HttpEntity<Object> entity = new HttpEntity<>(transferRecipientRequest, headers);
            final ResponseEntity<TransferRecipientResponse> exchange =
                restTemplate.exchange(requestUrl, HttpMethod.POST, entity, TransferRecipientResponse.class);

            log.info("transfer recipient request [" + transferRecipientRequest +"] with provider [" + getProvider() +"] has a response of + [" + exchange +"]");

            return exchange.getBody();
        } catch (final HttpClientErrorException ex) {
            ex.printStackTrace();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            throw new InvalidParameterException("could not get transfer recipient because [" + ex.getResponseBodyAsString() + "]");
        }
    }



}

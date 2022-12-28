package com.index.bankTransfer.providers.paystack.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BankListResponse {
    String status;

    String message;

    List<PaystackBank> data;

    @Data
    public static class PaystackBank {
        Long id;
        String name;
        String slug;
        String code;
        String longcode;
        String gateway;
        Boolean pay_with_bank;
        Boolean active;
        String country;
        String currency;
        String type;
        Boolean is_deleted;
        String createdAt;
        String updatedAt;
    }
}


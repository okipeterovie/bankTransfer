package com.index.bankTransfer.providers;

import com.index.bankTransfer.providers.flutterwave.FlutterwaveService;
import com.index.bankTransfer.providers.paystack.PaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProviderSelectionService {

    final private PaystackService paystackService;

    final private FlutterwaveService flutterwaveService;

    @Value("${vendors.default}")
    String defaultVendor;

    public String getDefaultVendor() {
        return defaultVendor;
    }

    public ProviderService currentTransferService(String vendor) {
        Optional<ServiceProvider> serviceProvider = ServiceProvider.getServiceProviderByName(vendor);

        if(serviceProvider.isEmpty()) {
            throw new  IllegalArgumentException("Invalid Vendor [" + vendor + "]");
        }

        switch (serviceProvider.get()) {
            case PAYSTACK:
                return paystackService;
            case FLUTTERWAVE:
                return flutterwaveService;
            default:
                throw new IllegalArgumentException("Could not pick service becuase Invalid Vendor [" + vendor + "]");
        }
    }
}

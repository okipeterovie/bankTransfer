package com.index.bankTransfer.providers;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceProvider {
    PAYSTACK("paystack"),
    FLUTTERWAVE("flutterwave");

    private final String name;

    ServiceProvider(String name) {
            this.name = name;
        }

    public String getName() {
        return name;
    }

    public static Optional<ServiceProvider> getServiceProviderByName(String value) {
        return Arrays.stream(ServiceProvider.values())
                .filter(serviceProvider -> serviceProvider.name.equals(value.toLowerCase()))
                .findFirst();
    }


}

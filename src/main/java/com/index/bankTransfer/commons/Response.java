package com.index.bankTransfer.commons;

import lombok.Getter;

@Getter
public class Response<T> {
    boolean status;
    String message;
    String code;
    T data;

    public Response(final boolean status, final String message) {
        this.status = status;
        this.message = message;
    }

    public Response(final boolean status, final T data) {
        this.status = status;
        this.data = data;
    }

    public Response(final boolean status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

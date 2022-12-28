package com.index.bankTransfer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.index.bankTransfer.commons.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RestAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        OutputStream outputStream = response.getOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(outputStream, new Response(false, "Invalid authentication"));
        outputStream.flush();
    }
}

package com.index.bankTransfer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.index.bankTransfer.commons.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Intercepts every request to ensure only valid token can access resource
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class HeaderRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        logger.info(authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.equals("test")) {
//                current session management for authenticated user
            final List<GrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken("test", null, list);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                                     .setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
            } else {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);

                final ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter()
                        .write(objectMapper
                                .writeValueAsString(new Response<>(false, "Token is invalid")));
        }
    }
}


package com.index.bankTransfer.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final HeaderRequestFilter headerRequestFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
            .addFilterBefore(headerRequestFilter, BasicAuthenticationFilter.class)
            .anonymous().and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .permitAll()
            .antMatchers("/")
            .permitAll()
//            .antMatchers("/api/v1/core-banking/health")
//            .permitAll()
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
                    "/configuration/security",
                    "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui",
                    "/swagger-ui.html")
            .permitAll()
            .anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS); // disable spring from managing session, the whole point of jwt
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}
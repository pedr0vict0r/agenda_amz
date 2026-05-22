package org.amz.agenda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

@Value("${APP_SECURITY_USERNAME:admin}")
private String username;

@Value("${APP_SECURITY_PASSWORD:admin123}")
private String password;

@Bean
public PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}

@Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
UserDetails user = User.builder().username(username).password(passwordEncoder.encode(password)).roles("EDITOR")
.build();
return new InMemoryUserDetailsManager(user);
}

@Bean
@Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
http.antMatcher("/api/**")
.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.authorizeRequests(auth -> auth.antMatchers(HttpMethod.GET, "/api/contatos/**").permitAll().anyRequest().hasRole("EDITOR"))
.httpBasic(Customizer.withDefaults());
return http.build();
}

@Bean
@Order(2)
public SecurityFilterChain uiFilterChain(HttpSecurity http) throws Exception {
http.authorizeRequests(auth -> auth
.antMatchers(HttpMethod.POST, "/cadastrarContato", "/contatos/**/editar", "/contatos/**/excluir")
.hasRole("EDITOR")
.anyRequest().permitAll())
.httpBasic(Customizer.withDefaults())
.formLogin(Customizer.withDefaults());
return http.build();
}
}

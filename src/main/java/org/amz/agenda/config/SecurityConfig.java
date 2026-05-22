package org.amz.agenda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
http.csrf(csrf -> csrf.ignoringAntMatchers("/api/**"))
.authorizeRequests(auth -> auth
.antMatchers(HttpMethod.POST, "/cadastrarContato", "/contatos/*/editar", "/contatos/*/excluir")
.hasRole("EDITOR")
.antMatchers(HttpMethod.POST, "/api/contatos/**").hasRole("EDITOR")
.antMatchers(HttpMethod.PUT, "/api/contatos/**").hasRole("EDITOR")
.antMatchers(HttpMethod.DELETE, "/api/contatos/**").hasRole("EDITOR").anyRequest().permitAll())
.httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults());
return http.build();
}
}

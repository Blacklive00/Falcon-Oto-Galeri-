package com.alemdar_energy.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.alemdar_energy.backend.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService UserDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(UserDetailsService);
    provider.setPasswordEncoder(encoder());
    return provider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(configurer -> configurer
        .requestMatchers(HttpMethod.POST, "/products/list").hasRole("MANAGER")
        .requestMatchers(HttpMethod.PUT, "/products/list").hasRole("MANAGER")
        .requestMatchers(HttpMethod.DELETE, "/products/list").hasRole("MANAGER")
        .requestMatchers(HttpMethod.GET, "/user/findUserForm").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/user/showFindingUser").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/user/showFormForAddUser").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/user/save").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/user/editRoles/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/user/updateRoles").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET,"/admin/showPanel").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET,"/cart/**").authenticated()  // Sepet işlemleri için giriş zorunlu
        .requestMatchers(HttpMethod.POST,"cart/order").authenticated()
        .requestMatchers(HttpMethod.GET,"/admin/orders").hasRole("ADMIN")
        .anyRequest().permitAll())
        .formLogin(form -> form.loginPage("/showMyLoginPage").loginProcessingUrl("/authenticateTheUser")
            .defaultSuccessUrl("/products/list", true)
            .permitAll())
        .logout(logout -> logout.logoutUrl("/logout")
            .logoutSuccessUrl("/products/list").permitAll());

    return http.build();
  }

}

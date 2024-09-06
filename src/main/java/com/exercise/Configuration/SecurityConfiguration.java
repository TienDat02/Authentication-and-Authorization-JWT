package com.exercise.Configuration;

import com.exercise.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(re -> {
                    re.requestMatchers("/js/**", "/css/**", "/images/**").permitAll(); // Add this line
                    re.requestMatchers("/home", "/forgot-password/**", "/register/**", "/login", "/authenticate", "/verify").permitAll();
                    re.requestMatchers("/admin/**").hasRole("ADMIN");
                    re.requestMatchers("/user/customer-management").hasAuthority("PERMISSION_READ");
                    re.requestMatchers("/user/create-customer").hasAuthority("PERMISSION_CREATE");
                    re.requestMatchers("/user/**").hasRole("USER");
                    re.anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer.loginPage("/login")
                            .successHandler(new AuthenticationSuccessHandler())
                            .failureUrl("/login?error=true") // Add this line
                            .permitAll();
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    // UserDetails normalUser =
    // User.builder().username("user").password("$2a$12$UGWxPhaRBlnZcGahorrpSONz6VPLHt4fFgidD5nCO8O6.C7f5WUXi").roles("USER").build();
    // UserDetails adminUser =
    // User.builder().username("admin").password("$2a$12$UGWxPhaRBlnZcGahorrpSONz6VPLHt4fFgidD5nCO8O6.C7f5WUXi").roles("ADMIN",
    // "USER").build();
    // return new InMemoryUserDetailsManager(normalUser, adminUser);
    // }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
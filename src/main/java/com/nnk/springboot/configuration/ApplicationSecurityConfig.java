package com.nnk.springboot.configuration;

import com.nnk.springboot.security.CustomAuthenticationSuccessHandler;
import com.nnk.springboot.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The Security configuration of the application
 *
 */
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    /**
     * Password encoder initialization.
     *
     * @return the password encoder
     */
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     * Configuration of the security filter chain.
     * Enable the csrf protection
     * Access regarding the role of the users
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(Customizer.withDefaults()) //to enable the csrf protection
                .authorizeHttpRequests((authorize) -> //to handle the requests
                        authorize.requestMatchers("/").permitAll() //to let access to the home page
                                .requestMatchers("*.png").permitAll()
                                .requestMatchers("/user/**").hasRole("ADMIN") //only admin can reach the user's pages
                                .requestMatchers("*/update/**").hasRole("ADMIN") //only admin can reach the update pages
                                .requestMatchers("*/delete/**").hasRole("ADMIN") //only admin can reach the delete pages
                                .anyRequest().authenticated()) //only authenticated user can make requests
                .formLogin( //to handle the login form
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(customAuthenticationSuccessHandler()) //to redirect user according to their role
                                .permitAll()) //to let access to the login page
                .logout( //to handle the logout
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout"))
                                .permitAll())
                .userDetailsService(customUserDetailsService) //to use our own configuration to load the user authenticated
        ;
        return http.build();
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}

package com.andrew.productcatalogue2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
                            prePostEnabled=true,
                            jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception
    {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("h2-console/**","/home", "/product-search/**", "/product-cat/**", "/dynamic-resources/**","/resources/**", "/webjars/**","/assets/**","/css/**","/images/**")   // ignore all these paths
                    .permitAll()
                    .antMatchers("/").permitAll()
//                    .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasAnyRole("ADMIN","SUPER")
                    .antMatchers("/super/**").hasRole("SUPER") //maybe put this in later?
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/login-home",false)
                    .failureUrl("/login?error")
                    .permitAll()
//                .successHandler(successHandler())
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                    .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")
                .and()
                .rememberMe()
                        .key("my-secure-key")
                        .rememberMeCookieName("andrews-remember-me-cookie")
                        .tokenValiditySeconds(24 * 60 * 60); //valid for a 24 hours


        //for the sake of using an embedded h2 database for demo purposes, I will
        //disable CRSF and enable frames to allow access to the h2 console
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }



}
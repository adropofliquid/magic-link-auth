package com.adropofliquid.magiclinkauth.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //Spring Security configuration class
    //access control is done here
    //some features are disabled and some are modified

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable()//disable the default spring security login page
                .httpBasic().disable()//disable basic http auth mechanism

                //access control
                .authorizeRequests()
                // Allows public access to the specified URLs
                // /resource/** is required for css to load
                .antMatchers("/","/login","/login/**","/register", "/resource/**").permitAll() //permit all
                .anyRequest().authenticated()// Requires authentication for any other request that doesn't match the ones specified above

                //incase a guest try to access a page that doesn't exist
                //they are redirected to homepage here
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException)
                        -> response.sendRedirect(request.getContextPath() + "/"));


    }
}

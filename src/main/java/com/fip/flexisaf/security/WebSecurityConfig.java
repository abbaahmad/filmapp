package com.fip.flexisaf.security;

import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    private String URI = "/api/v1/film";
   
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.PUT, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.DELETE, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.POST, URI + "/**").hasAuthority(Role.ADMINISTRATOR.toString())
                    .antMatchers(HttpMethod.GET, URI + "/**").permitAll()
                    .antMatchers(URI+"/**").hasAuthority(Role.ADMINISTRATOR.toString())
                    .antMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/register").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder);
    }
}

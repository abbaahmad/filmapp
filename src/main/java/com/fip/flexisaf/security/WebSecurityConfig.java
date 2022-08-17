package com.fip.flexisaf.security;

import com.fip.flexisaf.jwt.AuthenticationFilter;
import com.fip.flexisaf.jwt.CustomAuthorizationFilter;
import com.fip.flexisaf.models.Role;
import com.fip.flexisaf.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Autowired
    CustomAuthorizationFilter customAuthorizationFilter;
    
    private String URI = "/api/v1/film";
    private String LOGIN_URI = "/api/v1/auth";
    
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
   
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        AuthenticationFilter customAuthenticationFilter =
                new AuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_URI);
        http
                .csrf().and().cors().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, LOGIN_URI+"/**").permitAll()
                    .antMatchers(HttpMethod.POST, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.PUT, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.DELETE, URI + "/review/**").hasAnyAuthority(Role.ADMINISTRATOR.toString(), Role.REGISTERED.toString())
                    .antMatchers(HttpMethod.POST, URI + "/**").hasAuthority(Role.ADMINISTRATOR.toString())
                    .antMatchers(HttpMethod.GET, URI + "/**").permitAll()
                    .antMatchers(URI+"/**").hasAuthority(Role.ADMINISTRATOR.toString())
                    .antMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/register").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder);
    }
}

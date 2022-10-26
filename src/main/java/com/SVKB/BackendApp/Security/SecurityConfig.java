package com.SVKB.BackendApp.Security;

import com.SVKB.BackendApp.Auth.ApplicationUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
public class  SecurityConfig extends WebSecurityConfigurerAdapter {

    private UnAuthEntryPoint unAuthEntryPoint;

    private ApplicationUserService applicationUserService;

    private JwtUtils jwtUtils;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils, ApplicationUserService applicationUserService) {
        return new AuthTokenFilter(jwtUtils,applicationUserService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unAuthEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/articles/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/category/**").permitAll()
                .antMatchers("/api/v1/category/**").permitAll()
//                .access("hasAuthority('ROLE_IT_ADMIN') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/api/v1/articles/**").permitAll()
//                .access("hasAuthority('ROLE_IT_ADMIN') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/auth/signup").permitAll()
//                .hasAuthority("ROLE_IT_ADMIN")
                .anyRequest()
                .authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(jwtUtils,applicationUserService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    
}

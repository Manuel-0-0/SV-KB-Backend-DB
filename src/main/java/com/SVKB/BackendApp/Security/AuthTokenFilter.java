package com.SVKB.BackendApp.Security;

import com.SVKB.BackendApp.Auth.ApplicationUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    private ApplicationUserService applicationUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (isEmpty(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return ;
            }else {
                try {

                    final String token = header.split(" ")[1].trim();

                    if (jwtUtils.validateJwtToken(token)) {
                        String username = jwtUtils.getusername(token);
                        UserDetails userDetails = applicationUserService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request,response);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Error Processing Request, Reason: "+ e.getCause()+", Message: "+e.getMessage() );
                }
            }
        filterChain.doFilter(request, response);
    }
}

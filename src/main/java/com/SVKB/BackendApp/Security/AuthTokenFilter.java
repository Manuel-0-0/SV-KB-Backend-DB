package com.SVKB.BackendApp.Security;

import com.SVKB.BackendApp.Auth.ApplicationUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    private ApplicationUserService applicationUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            // get header from request ...
            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (isEmpty(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token= header.split(" ")[1].trim();

//            ResponseCookie cookie = jwtUtils.generateJwtCookie();
//            String signature  = parseJwt(cookie);

//            String token = tokenFromFrontend + "." + signature;

//            log.info(token);


            if (jwtUtils.validateJwtToken(token)) {
                // get email from the token ...e
                String username = jwtUtils.getusername(token);

                // find email + password against repository/database ...
                UserDetails userDetails = applicationUserService.loadUserByUsername(username);

                // use in built class for simple presentation of username and password...
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                // WebAuthenticationDetailsSource - Builds the details object from a request object, creating a WebAuthenticationDetails.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set details to security context - this stores the details of the currently authenticated user.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            throw new IllegalStateException(String.format("Token % can not be trusted "));
        }
        filterChain.doFilter(request, response);
    }
}

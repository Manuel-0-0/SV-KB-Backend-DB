package com.SVKB.BackendApp.Security;

import com.SVKB.BackendApp.Auth.ApplicationUserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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


            // get header from request ...

            final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            log.info(header);
            if (isEmpty(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                log.info("i think its empty");
                return ;
            }else {
                try {

                    final String token = header.split(" ")[1].trim();
                    log.info("token is " + token);

//            ResponseCookie cookie = jwtUtils.generateJwtCookie();
//            String signature  = parseJwt(cookie);

//            String token = tokenFromFrontend + "." + signature;

//            log.info(token);


                    if (jwtUtils.validateJwtToken(token)) {
                        String username = jwtUtils.getusername(token);
                        log.info("username is "+username);


                        log.info("validated");
                        // get email from the token ...e
//                        String username = jwtUtils.getusername(token);

                        // find email + password against repository/database ...
                        UserDetails userDetails = applicationUserService.loadUserByUsername(username);

                        log.info("userdetails is "+userDetails);
                        // use in built class for simple presentation of username and password...
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );

                        // WebAuthenticationDetailsSource - Builds the details object from a request object, creating a WebAuthenticationDetails.
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // set details to security context - this stores the details of the currently authenticated user.
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request,response);

                    }else{
                        log.info("did not validate");
                    }

                } catch (Exception e) {
                    throw new IllegalStateException("Token can not be trusted "+ e.getCause()+e.getMessage() );
                }
            }
        filterChain.doFilter(request, response);
    }
}

package com.SVKB.BackendApp.Security;

import com.SVKB.BackendApp.Auth.ApplicationUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;



    private String tokenSignature;

    // generate the jwt using the email as the subject ...
    public String generateTokenFromUsername(ApplicationUser user) {


        String Token= Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS512")
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();

        setTokenSignature(Token);
        return Token;
    }

    public String getTokenSignature() {
        return tokenSignature;
    }

    public void setTokenSignature(String tokenSignature) {
        this.tokenSignature = tokenSignature;
    }

    public String getSignature (String signature) {
        return signature;
    }

    public String getJwtFromCookies(ResponseCookie cookie) {
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String getusername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {

            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken).getBody();
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}

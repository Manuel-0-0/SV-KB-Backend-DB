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

//    @Value("${app.cookieName}")
//    private String jwtCookie;

    private String tokenSignature;

    // generate the jwt using the email as the subject ...
    public String generateTokenFromUsername(ApplicationUser user) {


        String Token= Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // split token...
//        String[] tokenParts = Token.split("\\.");
//        String tokenHeader = tokenParts[0];
//        String tokenPayload = tokenParts[1];
//        String tokenSignature = tokenParts[2];
//        String frontendToken = tokenHeader + "." + tokenPayload;

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

//    public ResponseCookie generateJwtCookie() {
//        String jwt = getTokenSignature();
//
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/admin")
//                .maxAge(24 * 60 * 60).httpOnly(true).build();
//
//        return cookie;
//    }

//    public ResponseCookie getCleanJwtCookie() {
//        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
//        return cookie;
//    }

    // get the email from the generated token for authentication (SecurityContextHolder) purposes ...
    public String getusername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // validate the token, return true if authentic : false otherwise ...
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
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

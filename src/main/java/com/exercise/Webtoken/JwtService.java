package com.exercise.Webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Service
public class JwtService {
    public static final String SECRET = "8EEF9C488A412F46EFEBD7B77C2A45DD286B682FF049BC1C1CFD7760F10D08B6B56AB916B7240BCF0AE910EBC8C6C483E6AA17474205EEAF24077D7604881715";
    public static final long EXPIRATION = TimeUnit.MINUTES.toSeconds(30);


    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(EXPIRATION)))
                .signWith(getSecretKey())
                .compact();
    }
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}

package Webtoken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

public class JwtService {
    public static final String SECRET = "D292851B078582A63803580930AC0CCE65D187192490E69682A39244144D0EFB5B7DBF5E66A6B2D8743B92053A17FBE361485AB0BF763036D322CA065CB0CEB5";
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
}

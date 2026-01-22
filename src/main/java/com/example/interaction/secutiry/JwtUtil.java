package com.example.interaction.secutiry;

import com.example.interaction.model.Employee;
import com.example.interaction.model.HRManager;
import com.example.interaction.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.issuer:ups18}")
    private String issuer;

    @Value("${jwt.audience:everyone}")
    private String audience;

    private SecretKey getSigningKey() {
        if (!secret.matches("^[A-Za-z0-9+/]+={0,2}$")) {
            byte[] keyBytes = new byte[32];
            byte[] secretBytes = secret.getBytes();
            System.arraycopy(secretBytes, 0, keyBytes, 0, Math.min(secretBytes.length, 32));
            return Keys.hmacShaKeyFor(keyBytes);
        }
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    public String extractAudience(String token) {
        return extractClaim(token, Claims::getAudience).stream().findFirst().orElse(null);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        User user = (User) userDetails;
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        if (user instanceof Employee) {
            Employee emp = (Employee) user;
            claims.put("firstName", emp.getFirstName());
            claims.put("lastName", emp.getLastName());
            claims.put("type", "employee");
        } else if (user instanceof HRManager) {
            HRManager hr = (HRManager) user;
            claims.put("firstName", hr.getFirstName());
            claims.put("lastName", hr.getLastName());
            claims.put("type", "hr");
        }

        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String tokenIssuer = extractIssuer(token);
        final String tokenAudience = extractAudience(token);

        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && issuer.equals(tokenIssuer)
                && audience.equals(tokenAudience));
    }

    public Boolean validateTokenStructure(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
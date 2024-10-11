package edu.eci.cvds.Task.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // @Value("{spring.jwt.secret.key}")
    private final String SECRET_KEY = "Xksn2kLIxKetMqF7GwY3XxLmz5A6kVFb9iRTQlqKyZiEeN5gO1Gr7G72Wx2FBq3CSJfTrYYhvW5Wx5xwGxIeoKF3S5dyk";
    public String getToken(String subject) {
        return getToken(new HashMap<>(),subject);
    }

    private String getToken(Map<String,Object> extraClaims, String subject) {
        return Jwts.builder().
                setClaims(extraClaims).
                setSubject(subject).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+1000*60*60*12)).
                signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }

    public boolean isUserValid(String token, UserDetails user) {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Date getExpirationDate(String token){
        return getClaim(token,Claims::getExpiration);
    }
    private boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }
    private Claims getAllClaims(String token){
        return Jwts.
                parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);

    }
}
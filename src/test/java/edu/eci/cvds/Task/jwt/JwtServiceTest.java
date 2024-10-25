package edu.eci.cvds.Task.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService = new JwtService();
    private String token;

    @BeforeEach
    void setUp() {
        token = jwtService.getToken("user123");
    }

    @AfterEach
    void tearDown() {
        token = null;
    }

    @Test
    void getToken() {
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUsernameFromToken() {
        String username = jwtService.getUsernameFromToken(token);
        assertEquals("user123", username);
    }

    @Test
    void isUserValid() {
        UserDetails user = User.withUsername("user123").password("password").roles("USER").build();
        assertTrue(jwtService.isUserValid(token, user));
    }

    @Test
    void getClaim() {
        String username = jwtService.getClaim(token, Claims::getSubject);
        assertEquals("user123", username);
    }
}

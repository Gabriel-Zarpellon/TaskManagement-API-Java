package com.task_management.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.task_management.config.JwtConfig;
import com.task_management.modules.user.UserEntity;
import com.task_management.modules.user.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Component
@Data
public class JwtTokenService {
    private static final String AUTHORITIES_KEY = "roles";

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    UserRepository userRepo;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String secret = Base64.getEncoder().encodeToString(jwtConfig.getSecretKey().getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        ClaimsBuilder claimsBuilder = Jwts.claims().subject(username);

        if (!authorities.isEmpty()) {
            String authoritiesCollector = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            claimsBuilder.add(AUTHORITIES_KEY, authoritiesCollector);
        }

        Claims claims = claimsBuilder.build();
        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + jwtConfig.getExpiresInMs());

        return Jwts.builder().claims(claims)
                .issuedAt(now).expiration(expiresIn)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        List<GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

        return true;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UserEntity getUser(HttpServletRequest req) {
        String token = req.getHeader("Authorization");

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username = extractUsername(token);
        UserEntity user = userRepo.findByUsername(username).get();

        return user;
    }

    public Long getUserId(HttpServletRequest req) {
        UserEntity user = getUser(req);

        return user.getId();
    }
}

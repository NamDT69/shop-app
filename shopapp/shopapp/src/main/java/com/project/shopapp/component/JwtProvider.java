package com.project.shopapp.component;

import com.project.shopapp.model.User;
import com.project.shopapp.repository.UserRepository;
import com.project.shopapp.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;
    @Value("${jwt.secretKey}")
    private String secretKey;

//    private final UserDetailsService userDetails;

    // Get the private and public keys
    private PrivateKey privateKey ;
    private PublicKey publicKey ;
    private KeyPair generateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }
    public String generateAccessToken(User user) throws Exception {
        return generateJwt(user, accessTokenExpiration);
    }
    public String generateRefreshToken(User user) throws Exception {
        return generateJwt(user, refreshTokenExpiration);
    }
    private String generateJwt(User user, Long expiration) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        try {
            return Jwts.builder()
                    .claims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(jwtSecretKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
    private Key jwtSecretKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
    public <T> T extractClaim(String token, Function<Claims, T> resolverClaims){
        Claims claims = extractAllClaims(token);
        return resolverClaims.apply(claims);
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Date getExpiration(String token){return extractClaim(token, Claims::getExpiration);}
    public String getEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public Boolean isTokenValid(String token, UserDetails userDetails ){
        try {
            final String email = getEmail(token);
            return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }catch (JwtException ignored){
            throw ignored;
        }
    }
    private boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).after(getExpiration(token)) ;
    }
}

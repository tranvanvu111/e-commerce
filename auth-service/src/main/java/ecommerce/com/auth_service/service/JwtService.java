package ecommerce.com.auth_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import ecommerce.com.auth_service.entity.Role;
import ecommerce.com.auth_service.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;
    @Value("${jwt.secret-key}")
    private String secretKey;

    public String createAccessToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = Date.from(Instant.now());
        Date expiredTime = Date.from(Instant.now().plus(accessTokenExpiration,ChronoUnit.SECONDS));
        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .claim("roles",roles)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    public String createRefreshToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = Date.from(Instant.now());
        Date expiredTime = Date.from(Instant.now().plus(refreshTokenExpiration,ChronoUnit.SECONDS));
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueTime)
                .expirationTime(expiredTime)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }


    public String isValidToken(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
        jwt.verify(new MACVerifier(secretKey.getBytes()));
        if(expiration.before(Date.from(Instant.now()))) {
            throw new RuntimeException("Token expired");
        }

        return jwt.getJWTClaimsSet().getSubject();
    }


}

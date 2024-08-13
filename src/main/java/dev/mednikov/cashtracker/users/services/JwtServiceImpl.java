package dev.mednikov.cashtracker.users.services;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.mednikov.cashtracker.users.exceptions.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{

    private final JWSVerifier verifier;
    private final JWSSigner signer;

    public JwtServiceImpl(@Value("${application.security.jwt-secret}") String secretKey) throws Exception {
        byte[] keyBytes = secretKey.getBytes();
        this.signer = new MACSigner(keyBytes);
        this.verifier = new MACVerifier(keyBytes);
    }

    @Override
    public String generateToken(String username) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date issuedAt = Timestamp.valueOf(currentDateTime);
        Date expiresAt = Timestamp.valueOf(currentDateTime.plusHours(12));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("CashTracker")
                .issueTime(issuedAt)
                .expirationTime(expiresAt)
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(this.signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (signedJWT.verify(this.verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                throw new AuthenticationException();
            }
        } catch (Exception ex){
            throw new AuthenticationException();
        }
    }
}

package com.example.ecommerce.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class JwtHelper {

    private final String secret = "secredsdfsfsadsffdsfdadscdcaefEFEAFAFdvdfvdgbdbrthreherhtrhwhtwrtwhtwdsfsfsfst";

    public String generateToken(String userDetails) throws KeyLengthException {
        Date now = new Date();
        long expirationTimeMs = 24 * 60 * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);

        JWSSigner signer = new MACSigner(secret);
        String issuer = "shiv";
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userDetails)
                .expirationTime(expiryDate)
                .issuer(issuer)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            // Handle exception
        }
        return signedJWT.serialize();
    }

    public boolean verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);
            return signedJWT.verify(verifier) && !signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        } catch (ParseException | JOSEException e) {
            // Handle exception
            return false;
        }
    }

    public String getEmailFromToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            // Handle exception
            return "Unable to parse.";
        }
    }
}

package com.mycareportal.identity.configuration;

import java.text.ParseException;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			
			log.info("token in identity service: {}", token);

			return new Jwt(token, signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
					signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), signedJWT.getHeader().toJSONObject(),
					signedJWT.getJWTClaimsSet().getClaims());

		} catch (ParseException e) {
			throw new JwtException("Invalid token");
		}
	}

}

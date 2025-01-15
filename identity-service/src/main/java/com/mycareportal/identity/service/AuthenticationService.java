package com.mycareportal.identity.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mycareportal.identity.dto.request.AuthenticationRequest;
import com.mycareportal.identity.dto.response.AuthenticationResponse;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;

	@NonFinal
	@Value("${jwt.signerKey}")
	private String signerKey;

	@NonFinal
	@Value("${jwt.valid-duration}")
	private long validDuration;

	// to authenticate the username and password from user login
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		log.info("start authenticate");
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

		if (!authenticated)
			throw new AppException(ErrorCode.UNAUTHENTICATED);

		// if authenticated, generate token
		String token = generateToken(user);

		return AuthenticationResponse.builder().token(token).build();
	}

	// to generate token from user info
	private String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(user.getUsername()).issuer("mycareportal.com")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString()).claim("scope", buildScope(user)).build();

		SignedJWT signedJWT = new SignedJWT(header, claimsSet);

		try {
			signedJWT.sign(new MACSigner(signerKey.getBytes()));
		} catch (JOSEException e) {
			log.error("Cannot create token", e);
			throw new RuntimeException(e);
		}

		return signedJWT.serialize();
	}

	// build scope for claimsSet from user roles
	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");

		if (!CollectionUtils.isEmpty(user.getRoles())) {
			user.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
		}

		return stringJoiner.toString();
	}
}

package com.mycareportal.identity.service;

import java.text.ParseException;
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
import com.mycareportal.identity.dto.request.IntrospectRequest;
import com.mycareportal.identity.dto.request.RefreshRequest;
import com.mycareportal.identity.dto.response.AuthenticationResponse;
import com.mycareportal.identity.dto.response.IntrospectResponse;
import com.mycareportal.identity.entity.RefreshToken;
import com.mycareportal.identity.entity.User;
import com.mycareportal.identity.exception.AppException;
import com.mycareportal.identity.exception.ErrorCode;
import com.mycareportal.identity.repository.RefreshTokenRepository;
import com.mycareportal.identity.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
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
	RefreshTokenRepository refreshTokenRepository;
	PasswordEncoder passwordEncoder;

	@NonFinal
	@Value("${jwt.signerKey}")
	private String signerKey;

	@NonFinal
	@Value("${jwt.valid-duration}")
	private long validDuration;

	@NonFinal
	@Value("${jwt.refresh-token.duration}")
	private long refreshTokenDuration;

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
		// generate refresh token
		String refreshToken = generateRefreshToken(user);

		return AuthenticationResponse.builder().accessToken(token).refreshToken(refreshToken).build();
	}

	// introspect access token
	public IntrospectResponse introspect(IntrospectRequest request) {
		String token = request.getToken();
		boolean isValid = false;
		try {
			isValid = verifyAccessToken(token);
		} catch (ParseException | JOSEException e) {
			isValid = false;
		}

		return IntrospectResponse.builder().valid(isValid).build();
	}

	// refresh access token
	public AuthenticationResponse refreshToken(RefreshRequest request) {
		var token = request.getToken();
		
		log.info("token: {}", token);

		var existingRefreshToken = refreshTokenRepository.findByToken(token);
		if (existingRefreshToken.isEmpty() || existingRefreshToken.get().getExpiryTime().isBefore(Instant.now())) {
			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		// update refresh token with new value and valid duration
		var refreshToken = existingRefreshToken.get();
		refreshToken.setToken(passwordEncoder.encode(UUID.randomUUID().toString()));
		refreshToken.setExpiryTime(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS));
		refreshToken = refreshTokenRepository.save(refreshToken);

		var accessToken = generateToken(refreshToken.getUser());

		return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
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

	// generate a refresh token
	private String generateRefreshToken(User user) {
		String token = passwordEncoder.encode(UUID.randomUUID().toString());
		Instant expiryTime = Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS);

		// Check if the user already has a refresh token
		return refreshTokenRepository.findByUser(user).map(existingToken -> {
			// Update existing token
			existingToken.setToken(token);
			existingToken.setExpiryTime(expiryTime);
			refreshTokenRepository.save(existingToken);
			return existingToken.getToken();
		}).orElseGet(() -> {
			// Create a new refresh token
			RefreshToken newToken = RefreshToken.builder().token(token).expiryTime(expiryTime).user(user).build();
			refreshTokenRepository.save(newToken);
			return newToken.getToken();
		});

	}

	// verify token
	private boolean verifyAccessToken(String token) throws ParseException, JOSEException {
		JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean isVerified = signedJWT.verify(verifier);

		return isVerified && expiryTime.after(new Date());
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

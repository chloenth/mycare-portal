package com.mycareportal.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.identity.entity.RefreshToken;
import com.mycareportal.identity.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUser(User user);
	Optional<RefreshToken> findByToken(String token);
}

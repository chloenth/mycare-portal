package com.mycareportal.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mycareportal.identity.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}

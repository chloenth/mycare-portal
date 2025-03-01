package com.mycareportal.search.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mycareportal.search.entity.UserProfileIndex;


public interface UserProfileRepository extends ElasticsearchRepository<UserProfileIndex, Long> {
    
	Optional<UserProfileIndex> findByProfileId(Long profileId);

}

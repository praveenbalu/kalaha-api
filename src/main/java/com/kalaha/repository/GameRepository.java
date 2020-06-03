package com.kalaha.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kalaha.model.Game;

/**
 * The Spring JPA GameRepository.
 */
@Repository
public interface GameRepository extends MongoRepository<Game, String> {
	
}

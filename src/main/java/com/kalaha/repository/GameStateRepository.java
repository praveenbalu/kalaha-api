package com.kalaha.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kalaha.model.GameState;

/**
 * The Spring JPA GameStateRepository.
 */
@Repository
public interface GameStateRepository extends MongoRepository<GameState, String> {

}

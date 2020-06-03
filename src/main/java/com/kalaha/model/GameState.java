package com.kalaha.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kalaha.enums.GameStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds the state of the game
 * For instance considering the below board
 * 
 *    6 6 6 6 6 6 -> Player 2 
 * 0              0  
 *    6 6 6 6 6 6 -> Player 1  
 *    ^
 *    Index 0
 * If player 1 chose to start to play position 0
 * The game will put 1 stone each on other houses to get,
 * 
 *    6 6 6 6 6 6 -> Player 2 
 * 0              1  
 *    0 7 7 7 7 7 -> Player 1  
 * 
 * To record the each stones places in every house, GameState helps
 * in understanding the stones places on each houses
 * 
 * Game state for above transition is,
 * 
 * [
 * 	{
 * 		'currentIndex'  : 0,
 *      'currentStones' : 0
 *  },
 * 	{
 * 		'currentIndex'  : 1,
 *      'currentStones' : 7
 *  },
 *  {
 * 		'currentIndex'  : 2,
 *      'currentStones' : 7
 *  },
 *  //..so on till
 * 	{
 * 		'currentIndex'  : 6,
 *      'currentStones' : 1
 *  },
 *  
 * ]
 * 
 * NOTE: This is only for the dynamic UI update
 * and not mandatory to implement the gameState
 * and can just get the updated game board directly
 * If not required
 * 
 * 
 */

/**
 * Checks if is player 2 updated.
 *
 * @return true, if is player 2 updated
 */
@Getter

/**
 * Sets the player 2 updated.
 *
 * @param isPlayer2Updated
 *            the new player 2 updated
 */
@Setter
@Document(collection = "GameState")
@JsonIgnoreProperties(value = { "isPlayer1Updated", "isPlayer2Updated" })
public class GameState implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8246580889163947370L;

	/** The id. */
	@Id
	private String id;

	/**
	 * Gets the next index.
	 *
	 * @return the next index
	 */
	@Getter

	/**
	 * Sets the next index.
	 *
	 * @param nextIndex
	 *            the new next index
	 */
	@Setter

	/**
	 * Instantiates a new state.
	 *
	 * @param currentIndex
	 *            the current index
	 * @param currentStones
	 *            the current stones
	 * @param nextIndex
	 *            the next index
	 */
	@AllArgsConstructor
	public static class State implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -7505573998188555129L;

		/** The current index. */
		private Integer currentIndex;

		/** The current stones. */
		private Integer currentStones;
	}

	/** The states. */
	private List<State> states;

	/** The game status. */
	private GameStatus gameStatus;

	/** The is player 1 updated. */
	private boolean isPlayer1Updated;

	/** The is player 2 updated. */
	private boolean isPlayer2Updated;

	/**
	 * Instantiates a new game state.
	 */
	public GameState() {
		states = new LinkedList<>();
		isPlayer1Updated = true;
		isPlayer2Updated = true;
		gameStatus = GameStatus.YET_TO_START;
	}

	/**
	 * Adds the current state.
	 *
	 * @param currentIndex
	 *            the current index
	 * @param currentStones
	 *            the current stones
	 * @param nextIndex
	 *            the next index
	 */
	public void addState(Integer currentIndex, Integer currentStones) {
		this.states.add(new State(currentIndex, currentStones));
	}

}

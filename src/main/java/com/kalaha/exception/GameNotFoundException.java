package com.kalaha.exception;

/**
 * Exception will be thrown if the game is not found
 * @author Praveen
 */
public class GameNotFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5008635005223795872L;

	/**
	 * Instantiates a new game not found exception.
	 *
	 * @param gameId unique identifier of the game
	 */
	public GameNotFoundException(String gameId) {
		super(String.format("Given Gameid -> %s is not available!", gameId));
	}
}

package com.kalaha.model.response;

import com.kalaha.enums.GameStatus;

import lombok.Data;

/**
 * Current Game status to check the 
 * current points of the players and 
 * stage of the game.
 */
@Data
public class GameStats {

	/** The game status. */
	private GameStatus gameStatus;

	/** The player 1 pts. */
	private Integer player1Pts;

	/** The player 2 pts. */
	private Integer player2Pts;

	/** The winner. */
	private String winner;
}

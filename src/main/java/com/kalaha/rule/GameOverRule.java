/*
 * 
 */
package com.kalaha.rule;

import org.springframework.stereotype.Service;

import com.kalaha.engine.RuleEngine;
import com.kalaha.enums.GameStatus;
import com.kalaha.model.Game;
import com.kalaha.model.GameState;
import com.kalaha.model.House;
import com.kalaha.service.BoardService;

/**
 * Rule Description :
 * After completing the play, If there are 
 * more than 37 stones in a store / if there are no more stones
 * to play in a players house, then the game is considered to be over.
 * All the stones will be put in their respective players
 * stores finally
 * 
 * Example:
 *    0 0 0 0 0 0  -> Player 1 
 * 20              22 
 *    4 5 7 2 1 8  -> Player 0
 *            ^ 
 * Considering player 0's turn complete,
 * the updated board would be like,
 * 
 *    0 0 0 0 0 0 
 * 20              49
 *    0 0 0 0 0 0
 *            ^
 */
@Service
public class GameOverRule extends GameRule {

	/** The board service. */
	private BoardService boardService;

	/** The houses. */
	private House[] houses;

	/** The store stones. */
	private Integer[] storeStones;

	/**
	 * Instantiates a new game over rule.
	 *
	 * @param game
	 *            the game
	 */
	public GameOverRule(Game game) {
		super(game);
		boardService = new BoardService();
	}

	public Boolean checkGameOver() {
		this.houses = game.getBoard().getHouses();
		this.storeStones = boardService.getStoreStones(houses, RuleEngine.BOARD_SIZE);
		if (game.getGameState().getGameStatus() == GameStatus.GAME_OVER || boardService.isPlayerHousesEmpty(houses, 0)
				|| boardService.isPlayerHousesEmpty(houses, 1))
			return true;

		return storeStones[0] >= 37 || storeStones[1] >= 37;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalaha.rule.GameRule#apply()
	 */
	@Override
	public void apply() {
		if (checkGameOver()) {
			GameState gameState = game.getGameState();
			gameState.setGameStatus(GameStatus.GAME_OVER);

			if (storeStones[0] < 37 || storeStones[1] < 37)
				boardService.finalizeStoreStones(houses, RuleEngine.BOARD_SIZE);
		}
	}
}

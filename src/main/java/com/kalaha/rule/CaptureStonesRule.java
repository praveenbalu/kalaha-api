package com.kalaha.rule;

import static com.kalaha.util.KalahaUtils.findOpponentIndex;
import static com.kalaha.util.KalahaUtils.findStoreIndexById;
import static com.kalaha.util.KalahaUtils.isStore;

import com.kalaha.engine.RuleEngine;
import com.kalaha.model.Game;
import com.kalaha.model.GameState;
import com.kalaha.model.House;

/**
 * Rule Description :
 * After completing the play, If the last house
 * belongs to the player and if the no. of stones in that 
 * house is 1, then it can be put it the players store
 * along with the opponent's house stone
 * 
 * Example:
 *    3 4 6 0 4 7  -> Player 1 
 * 20              22 
 *    4 5 7 2 1 8  -> Player 0
 *            ^ 
 * Considering '^' denotes the last position of player 0's turn,
 * the updated board would be like,
 * 
 *    3 4 6 0 0 7 
 * 20              27  --> (22+1+4) 
 *    4 5 7 2 0 8
 *            ^
 */
public class CaptureStonesRule extends GameRule {

	/** The player id. */
	private int playerId;

	/** The current house. */
	private House currentHouse;

	/**
	 * Instantiates a new capture stones rule.
	 *
	 * @param game
	 *            the game
	 * @param position
	 *            the position
	 */
	public CaptureStonesRule(Game game, int position) {
		super(game, position);
		playerId = game.getCurrentPlayerId();
		currentHouse = game.getBoard().getHouses()[position];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalaha.rule.GameRule#apply()
	 */
	@Override
	public void apply() {
		if (currentHouse.getStones() == 1 && !isStore(position, RuleEngine.BOARD_SIZE)
				&& currentHouse.getPlayerId() == playerId) {
			House[] houses = game.getBoard().getHouses();
			int opponentIndex = findOpponentIndex(position, RuleEngine.BOARD_SIZE);
			int storeIndex = findStoreIndexById(playerId, RuleEngine.BOARD_SIZE);
			House opponentHouse = houses[opponentIndex];
			House storeHouse = houses[storeIndex];
			GameState gameState = game.getGameState();

			storeHouse.addStones(opponentHouse.getStones() + 1);
			currentHouse.clearStones();
			opponentHouse.clearStones();
			gameState.addState(position, 0);
			gameState.addState(opponentIndex, 0);
			gameState.addState(storeIndex, storeHouse.getStones());
		}
	}

}

package com.kalaha.rule;

import static com.kalaha.util.KalahaUtils.isStore;
import static com.kalaha.util.KalahaUtils.switchPlayer;

import com.kalaha.engine.RuleEngine;
import com.kalaha.model.Game;
import com.kalaha.model.House;

/**
 * Rule Description :
 * After completing the play, If the last house
 * belongs to the player's store, then the turn 
 * will be continued by the current player else
 * it will be moved to the opponent
 * 
 * Example:
 *    3 4 6 0 4 7     -> Player 1 
 * 20              22 
 *    4 5 7 2 1 8  ^  -> Player 0
 *             
 * Considering '^' denotes the last position of player 0's turn,
 * then the new turn will be still be player 0
 * 
 * 
 */
public class PlayerTurnChangedRule extends GameRule {

	/**
	 * Instantiates a new player turn changed rule.
	 *
	 * @param game
	 *            the game
	 * @param position
	 *            the position
	 */
	public PlayerTurnChangedRule(Game game, int position) {
		super(game, position);
	}

	public Boolean checkTurnRequired() {
		House currentHouse = game.getBoard().getHouses()[position];
		return currentHouse.getPlayerId() == game.getCurrentPlayerId() && !isStore(position,RuleEngine.BOARD_SIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalaha.rule.GameRule#apply()
	 */
	@Override
	public void apply() {
		if(checkTurnRequired()) 
			game.setCurrentPlayerId(switchPlayer(game.getCurrentPlayerId()));
		
	}

}

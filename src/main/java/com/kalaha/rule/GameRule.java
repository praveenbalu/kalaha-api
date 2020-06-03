package com.kalaha.rule;

import org.springframework.stereotype.Service;

import com.kalaha.model.Game;

import lombok.Getter;

/**
 * The Rules of the games will be defined using
 * an abstract GameRule. This ensures the rules are
 * controlled in the game and can be customized easily
 *  
 * 
 */

/**
 * Gets the game.
 *
 * @return the game
 */
@Getter
@Service
public abstract class GameRule {

	/** The position. */
	protected int position;

	/** The game. */
	protected Game game;

	/**
	 * Game rules based on position
	 * the position might indicate, the last 
	 * position/first position the player has played
	 *
	 * @param game
	 *            the game
	 * @param position
	 *            the position
	 */
	public GameRule(Game game, int position) {
		this.game = game;
		this.position = position;
	}

	/**
	 * GameRule without position
	 *
	 * @param game
	 *            the game
	 */
	public GameRule(Game game) {
		this(game, -1);
	}

	/**
	 * Applies/executes the game rule
	 */
	public abstract void apply();

}

package com.kalaha.service;

import static com.kalaha.util.KalahaUtils.isStore;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kalaha.engine.RuleEngine;
import com.kalaha.enums.GameStatus;
import com.kalaha.exception.KalahaValidationException;
import com.kalaha.model.Board;
import com.kalaha.model.Game;
import com.kalaha.model.House;
import com.kalaha.model.Player;
import com.kalaha.model.request.Play;

/**
 * Validates the game play
 */
@Service
public class ValidationService {

	/**
	 * Validate game join.
	 *
	 * @param players
	 * @throws KalahaValidationException
	 *             the kalaha validation exception
	 */
	public void validateGameJoin(List<Player> players) throws KalahaValidationException {
		if (players.size() == 2)
			throw new KalahaValidationException("There are already 2 players in the given gameId !!");
	}

	/**
	 * Validate game play.
	 *
	 * @param game object
	 * @param play
	 * @throws KalahaValidationException
	 *             the kalaha validation exception
	 */
	public void validateGamePlay(Game game, Play play) throws KalahaValidationException {
		Board board = game.getBoard();
		House[] houses = board.getHouses();
		House currentHouse = houses[play.getCurrentMove()];

		if (game.getGameState().getGameStatus() == GameStatus.GAME_OVER) {
			throw new KalahaValidationException("Game has already ended!!");
		}

		if (game.getCurrentPlayerId() != play.getPlayer().getId()) {
			throw new KalahaValidationException(
					String.format("Sorry!! Its %s's turn", game.getPlayers().get(game.getCurrentPlayerId()).getName()));
		}

		if (isStore(play.getCurrentMove(),RuleEngine.BOARD_SIZE)) {
			throw new KalahaValidationException("Invalid Move!! You cannot pick stones from your Store!!");
		}

		if (currentHouse.getStones() == 0) {
			throw new KalahaValidationException("No Stones found in the pit!! Please try another move!");
		}

	}

}

package com.kalaha.service;

import static com.kalaha.engine.RuleEngine.*;
import static com.kalaha.util.KalahaUtils.isStore;
import static com.kalaha.util.KalahaUtils.switchPlayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.kalaha.engine.RuleEngine;
import com.kalaha.enums.GameStatus;
import com.kalaha.enums.Rule;
import com.kalaha.exception.GameNotFoundException;
import com.kalaha.exception.KalahaClientException;
import com.kalaha.exception.KalahaInvalidRuleException;
import com.kalaha.exception.KalahaServerException;
import com.kalaha.exception.KalahaValidationException;
import com.kalaha.model.Board;
import com.kalaha.model.Game;
import com.kalaha.model.GameState;
import com.kalaha.model.House;
import com.kalaha.model.Player;
import com.kalaha.model.GameState.State;
import com.kalaha.model.request.Play;
import com.kalaha.model.response.GameStats;
import com.kalaha.repository.GameRepository;
import com.kalaha.repository.GameStateRepository;
import com.kalaha.rule.GameRule;

/**
 * The Class GameService.
 * @author Praveen
 */
@Service
public class GameService {

	/** The game repository. */
	@Autowired
	private GameRepository gameRepository;

	/** The game state repository. */
	@Autowired
	private GameStateRepository gameStateRepository;

	/** The board service. */
	@Autowired
	private BoardService boardService;

	/** The rule engine. */
	@Autowired
	private RuleEngine ruleEngine;

	/** The validation service. */
	@Autowired
	private ValidationService validationService;

	/**
	 * Creates the game for a new player.
	 *
	 * @param player who creates the game
	 * @return {@link Game} object
	 */
	public Game createGame(Player player) {
		Board board = boardService.createBoard(BOARD_SIZE, INITIAL_STONES);
		int randomTurn = new Random().ints(0, 2).findFirst().getAsInt();
		player.setId(0);
		List<Player> players = Stream.of(player).collect(Collectors.toList());
		Game game = new Game(players, board, randomTurn);
		gameStateRepository.save(game.getGameState());
		gameRepository.save(game);
		game.setPlayer(player);
		return game;
	}
	
	/**
	 * Join a random game from the list of games yet to start
	 * @param player
	 * @return
	 */
	public Game joinRandomGame(Player player) {
		return joinGame(findRandomGame().getGameId(), player);
	}

	/**
	 * Joins the existing game where already a player is waiting
	 *
	 * @param gameId
	 *            the game id
	 * @param player
	 *            the player
	 * @return the game
	 */
	public Game joinGame(String gameId, Player player) {
		Game game = findGame(gameId);
		try {
			validationService.validateGameJoin(game.getPlayers());
			player.setId(1);
			game.setPlayer(player);
			game.getPlayers().add(player);
			game.getGameState().setGameStatus(GameStatus.IN_PROGRESS);
			gameStateRepository.save(game.getGameState());
			gameRepository.save(game);
		} catch (KalahaValidationException e) {
			throw new KalahaClientException(e.getMessage(), e);
		}
		return game;
	}

	/**
	 * Implements the play/move of the game for the given player.
	 *
	 * @param gameId
	 * @param play
	 * @return {@link GameState} current state of the game
	 */
	public Game play(String gameId, Play play) {
		Game game = findGame(gameId);

		try {
			validationService.validateGamePlay(game, play);

			GameState gameState = game.getGameState();
			int currentPlayerTurnId = game.getCurrentPlayerId();
			int currIndex = play.getCurrentMove();

			Board board = game.getBoard();
			House[] houses = board.getHouses();
			House currentHouse = houses[currIndex];
			int stonesInHand = currentHouse.getStones();

			currentHouse.clearStones();
			while (stonesInHand != 0) {
				currIndex = (currIndex + 1) % BOARD_SIZE;
				currentHouse = houses[currIndex];
				if (isStore(currIndex,BOARD_SIZE) && currentHouse.getPlayerId() != currentPlayerTurnId)
					continue;
				currentHouse.addOneStone();
				--stonesInHand;
				gameState.addState(currIndex, currentHouse.getStones());
			}

			ruleEngine.createRule(Rule.CAPTURE_STONES, currIndex, game).apply();
			ruleEngine.createRule(Rule.GAME_OVER, game).apply();
			ruleEngine.createRule(Rule.PLAYER_TURN_CHANGED, currIndex, game).apply();
			
			updateGameStateLookUp(gameState, currentPlayerTurnId, true);
			updateGameStateLookUp(gameState, switchPlayer(currentPlayerTurnId), false);
			gameRepository.save(game);

		} catch (KalahaInvalidRuleException e) {
			throw new KalahaServerException(String.format("Rule error : -> %s", e.getMessage()), e);
		} catch (KalahaValidationException e) {
			throw new KalahaClientException(e.getMessage(), e);
		}

		return game;
	}

	/**
	 * Gets the game stats.
	 *
	 * @param gameId
	 *            the game id
	 * @return the game stats
	 */
	public GameStats getGameStats(String gameId) {
		Game game = findGame(gameId);
		Integer[] storeStones = boardService.getStoreStones(game.getBoard().getHouses(), BOARD_SIZE);
		GameStatus gameStatus = game.getGameState().getGameStatus();
		GameStats gameStats = new GameStats();
		gameStats.setGameStatus(gameStatus);
		gameStats.setPlayer1Pts(storeStones[0]);
		gameStats.setPlayer2Pts(storeStones[1]);

		if (gameStatus == GameStatus.GAME_OVER) {
			int winnerIndex = findWinnerIndex(storeStones[0], storeStones[1]);
			gameStats.setWinner(winnerIndex == -1 ? "Tie" : game.getPlayers().get(winnerIndex).getName());
		}
		return gameStats;
	}

	/**
	 * Update game state look up.
	 *
	 * @param gameState
	 *            the game state
	 * @param playerId
	 *            the player id
	 */
	public void updateGameStateLookUp(GameState gameState, Integer playerId, boolean state) {
		if (playerId == 0)
			gameState.setPlayer1Updated(state);
		else
			gameState.setPlayer2Updated(state);
	}

	/**
	 * Gets the game state.
	 *
	 * @param gameId
	 *            the game id
	 * @param playerId
	 *            the player id
	 * @return the game state
	 */
	public GameState getGameState(String gameId, Integer playerId) {
		Game game;
		GameState gameState = null;

		game = findGame(gameId);
		gameState = game.getGameState();
		updateGameStateLookUp(gameState, playerId, true);

		if (gameState.isPlayer1Updated() && gameState.isPlayer2Updated()) {
			List<State> tempStates = new LinkedList<>(gameState.getStates());
			gameState.getStates().clear();
			gameStateRepository.save(gameState);
			gameState.getStates().addAll(tempStates);
		}

		if (gameState.getGameStatus() == GameStatus.IN_PROGRESS && game.getCurrentPlayerId() != playerId) {
			gameState.setGameStatus(GameStatus.WAIT_FOR_TURN);
		}
		return gameState;
	}

	/**
	 * Find winner index.
	 *
	 * @param store1Stones
	 *            the store 1 stones
	 * @param store2Stones
	 *            the store 2 stones
	 * @return the int
	 */
	private int findWinnerIndex(Integer store1Stones, Integer store2Stones) {
		if (store1Stones.intValue() == store2Stones.intValue())
			return -1;
		return store1Stones > store2Stones ? 0 : 1;
	}

	/**
	 * Find game.
	 *
	 * @param gameId
	 *            the game id
	 * @return the game
	 */
	private Game findGame(String gameId) {
		try {
			return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
		} catch (GameNotFoundException e) {
			throw new KalahaClientException(e.getMessage());
		}
	}
	
	/**
	 * Finds a random game which is yet to start
	 * @return
	 */
	private Game findRandomGame() {
		try {
			return gameRepository.findAll().stream()
				.filter(g -> g.getGameState().getGameStatus() == GameStatus.YET_TO_START)
				.findFirst()
				.orElseThrow(() -> new GameNotFoundException("")) ;
		} catch (GameNotFoundException e) {
			throw new KalahaClientException(e.getMessage());
		}
	}

}

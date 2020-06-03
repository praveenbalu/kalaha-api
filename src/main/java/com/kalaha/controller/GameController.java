package com.kalaha.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalaha.model.Game;
import com.kalaha.model.GameState;
import com.kalaha.model.Player;
import com.kalaha.model.request.Play;
import com.kalaha.model.response.GameStats;
import com.kalaha.service.GameService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for game operations
 * @author Praveen 
 */
@RestController
@Api(value = "Game Controls")
@RequestMapping("/game")
public class GameController {

	/** The game service. */
	@Autowired
	private GameService gameService;

	/**
	 * Creates a new game and wait for other player to join.
	 * Other player can join using the gameId created for the new game
	 *
	 * @param {@link Player} who creates the game
	 * @return the response entity of {@link Game} object
	 */
	@PostMapping
	@ApiOperation(value = "Creates a new game", notes = "New game for the player")
	public ResponseEntity<Game> newGame(@Valid @RequestBody Player player) {
		Game game = gameService.createGame(player);
		return ResponseEntity.ok(game);
	}

	/**
	 * Join a game with existing player who is waiting.
	 * The gameId can be a valid gameId or ''random' to just 
	 * join in a random game with the list of games waiting for players
	 * to join
	 *
	 * @param gameId to join (or) 'random' value
	 * @param 2nd {@link Player} who joins the game
	 * @return the response entity of {@link Game} object
	 */
	@PutMapping("/{gameId}")
	@ApiOperation(value = "Joins an existing game", notes = "By giving the game id, you can join the existing game")
	public ResponseEntity<Game> joinGame(@PathVariable(value = "gameId") String gameId,@Valid @RequestBody Player player) {
		Game game = gameId.equals("random")?gameService.joinRandomGame(player):
			gameService.joinGame(gameId, player);
		return ResponseEntity.ok(game);
	}

	/**
	 * Plays the game of the given player
	 * The game play uses the gameId to
	 * extract the current gameBoard and game status
	 *
	 * @param gameId unique identifier of the game
	 * @param {@link Play} to play
	 * @return the response entity of {@link Game} object
	 */
	@PutMapping("/play/{gameId}")
	@ApiOperation(value = "Play/Make a move for the player", 
	notes = "By providing the player details make their move in the board")
	public ResponseEntity<Object> play(@PathVariable(value = "gameId") String gameId, @RequestBody Play play) {
		Game game = gameService.play(gameId, play);
		return ResponseEntity.ok(game);
	}

	/**
	 * Gets the current game information/status
	 *
	 * @param gameId unique identifier of the game
	 * @return the response entity of {@link GameStats} object
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "Gets game infomration", notes = "Provides various updated information about the game")
	public ResponseEntity<GameStats> getGameInfo(@PathVariable(value = "id") String gameId) {
		GameStats gameStats = gameService.getGameStats(gameId);
		return ResponseEntity.ok(gameStats);
	}

	/**
	 * Gets the current state of the game.
	 *
	 * @param gameId unique identifier of the game
	 * @param playerId id of the player
	 * @return the response entity of {@link GameState} object
	 */
	@PatchMapping("/{id}/{playerid}")
	@ApiOperation(value = "Get State of the game", 
	notes = "Provides the game transitions happened. Like, how many stones were replaced in each house at a point of time")
	public ResponseEntity<GameState> getGameState(@PathVariable(value = "id") String gameId,
			@PathVariable(value = "playerid") Integer playerId) {
		GameState gameState = gameService.getGameState(gameId, playerId);
		return ResponseEntity.ok(gameState);
	}

}

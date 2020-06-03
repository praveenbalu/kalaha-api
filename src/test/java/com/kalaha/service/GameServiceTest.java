package com.kalaha.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
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
import com.kalaha.service.BoardService;
import com.kalaha.service.GameService;
import com.kalaha.service.ValidationService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
public class GameServiceTest {

	@Mock
	private GameRepository gameRepository;

	@Mock
	private Board mockBoard;

	@Mock
	private GameStateRepository gameStateRepository;

	@Mock
	private BoardService boardService;

	@Mock
	private ValidationService validationService;

	@Mock
	private RuleEngine ruleEngine;

	@Autowired
	private GameRule dummyGameRule;

	@Autowired
	private Game game;

	@Autowired
	private Play play;

	@InjectMocks
	private GameService gameService;

	private static Player player1Input = new Player(null, "Player 1");
	private static Player player1Output = new Player(0, "Player 1");
	private static Player player2Input = new Player(null, "Player 2");
	private static Player player2Output = new Player(1, "Player 2");

	@Test
	public void testCreateGame() {
		when(gameStateRepository.save(any(GameState.class))).thenReturn(mock(GameState.class));
		when(gameRepository.save(any(Game.class))).thenReturn(mock(Game.class));
		when(boardService.createBoard(anyInt(), anyInt())).thenReturn(mockBoard);
		Game game1 = gameService.createGame(player1Input);
		assertThat(game1.getPlayer()).isEqualTo(player1Output).withFailMessage("Player details didn't match");
		assertThat(game1.getPlayers()).hasSize(1)
				.withFailMessage("There should be only one player while creating game");
	}

	@Test
	public void testJoinGamePlayer() throws KalahaValidationException {
		Game game1 = new Game(Stream.of(player1Output).collect(Collectors.toList()), mockBoard, 0);
		when(gameStateRepository.save(any(GameState.class))).thenReturn(mock(GameState.class));
		when(gameRepository.save(any(Game.class))).thenReturn(mock(Game.class));
		when(gameRepository.findById(anyString())).thenReturn(Optional.of(game1));
		doNothing().when(validationService).validateGameJoin(anyList());
		game1 = gameService.joinGame("", player2Input);
		assertThat(game1.getPlayer()).isEqualTo(player2Output).withFailMessage("Player details didn't match");
		assertThat(game1.getPlayers()).hasSize(2).withFailMessage("There should be two player after joining game");
	}

	@Test
	public void testPlay() throws KalahaValidationException, KalahaInvalidRuleException {
		Optional<Game> optionalGame = Optional.of(game);

		when(gameRepository.findById(anyString())).thenReturn(optionalGame);
		when(gameRepository.save(any(Game.class))).thenReturn(mock(Game.class));
		when(ruleEngine.createRule(any(Rule.class), any(Game.class))).thenReturn(dummyGameRule);
		when(ruleEngine.createRule(any(Rule.class), anyInt(), any(Game.class))).thenReturn(dummyGameRule);
		when(ruleEngine.createRule(any(Rule.class), anyInt(), any(Game.class))).thenReturn(dummyGameRule);
		game = gameService.play("", play);
	}

	@Test
	public void testPlayClientExceptionNoGame() {
		given(gameRepository.findById(anyString())).willAnswer(inv -> {
			throw new GameNotFoundException("1234");
		});
		KalahaClientException ex = assertThrows(KalahaClientException.class, () -> gameService.play("", play),
				"Expected exception during validation");
		assertThat(ex.getMessage()).isEqualTo("Given Gameid -> 1234 is not available!");
	}
	
	@Test
	public void testPlayClientException() throws KalahaValidationException {
		Optional<Game> optionalGame = Optional.of(game);
		when(gameRepository.findById(anyString())).thenReturn(optionalGame);
		doThrow(new KalahaValidationException("Not Valid!")).when(validationService)
			.validateGamePlay(any(Game.class), any(Play.class));
		KalahaClientException ex = assertThrows(KalahaClientException.class, () -> gameService.play("", play),
				"Expected exception during validation");
		assertThat(ex.getMessage()).isEqualTo("Not Valid!");
	}

	@Test
	public void testPlayServerException() throws KalahaInvalidRuleException {
		Optional<Game> optionalGame = Optional.of(game);
		when(gameRepository.findById(anyString())).thenReturn(optionalGame);
		given(ruleEngine.createRule(any(Rule.class), anyInt(), any(Game.class))).willAnswer(inv -> {
			throw new KalahaInvalidRuleException("Invalid Rule");
		});
		KalahaServerException ex = assertThrows(KalahaServerException.class, () -> gameService.play("", play),
				"Expected exception during validation");
		assertThat(ex.getMessage()).isEqualTo("Rule error : -> Invalid Rule");
	}

	@Test
	public void testGameState() throws KalahaValidationException, KalahaInvalidRuleException {
		Optional<Game> optionalGame = Optional.of(game);
		List<State> state = new LinkedList<>();
		state.add(mock(State.class));
		game.getGameState().setPlayer1Updated(false);
		game.getGameState().setPlayer2Updated(true);
		game.getGameState().setStates(state);
		when(gameRepository.findById(anyString())).thenReturn(optionalGame);
		when(gameStateRepository.save(any(GameState.class))).thenReturn(mock(GameState.class));
		GameState gameState = gameService.getGameState("", 0);
		assertThat(gameState.isPlayer1Updated()).isTrue();
		assertThat(gameState.isPlayer2Updated()).isTrue();
		assertThat(gameState.getStates()).isEqualTo(state);
	}

	@Test
	public void testGameStats() {
		Game game1 = new Game(Stream.of(player1Output, player2Output).collect(Collectors.toList()),
				new Board(new House[] {}), 0);
		when(gameRepository.findById(anyString())).thenReturn(Optional.of(game1));
		game1.getGameState().setGameStatus(GameStatus.GAME_OVER);
		when(boardService.getStoreStones(any(House[].class), anyInt())).thenReturn(new Integer[] { 20, 30 });
		GameStats gameStats = gameService.getGameStats("");
		assertThat(gameStats.getGameStatus()).isEqualTo(GameStatus.GAME_OVER)
				.withFailMessage("Game status not updated");
		assertThat(gameStats.getPlayer1Pts()).isEqualTo(20).withFailMessage("Player 1 points should be 20");
		assertThat(gameStats.getPlayer2Pts()).isEqualTo(30).withFailMessage("Player 2 points shoule be 30");
		assertThat(gameStats.getWinner()).isEqualTo(player2Output.getName())
				.withFailMessage("Player details didn't match");
	}

}

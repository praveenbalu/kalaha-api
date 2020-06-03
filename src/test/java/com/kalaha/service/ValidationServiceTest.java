package com.kalaha.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.enums.GameStatus;
import com.kalaha.exception.KalahaValidationException;
import com.kalaha.model.Game;
import com.kalaha.model.Player;
import com.kalaha.model.request.Play;
import com.kalaha.service.ValidationService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
@TestMethodOrder(OrderAnnotation.class)
class ValidationServiceTest {
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	Game game;
	
	@Autowired
	Play play;

	@Autowired
	List<Player> players;
	
	@Mock
	Player player1;
	@Mock
	Player player2;
		
	@Test
	@Order(1)
	void testValidGameJoin() throws KalahaValidationException {
		validationService.validateGameJoin(Arrays.asList(players.get(0)));
	}
	
	@Test
	@Order(2)
	void testValidGamePlay() throws KalahaValidationException {
		game.setPlayers(players);
		validationService.validateGamePlay(game, play);
	}
	
	@Test
	@Order(3)
	void testInvalidGameJoin() {
		KalahaValidationException ex=
				assertThrows(KalahaValidationException.class, ()->validationService.validateGameJoin(players)
						,"Expected exception during validation");
		assertThat(ex.getMessage()).isEqualTo("There are already 2 players in the given gameId !!");
	}
	
	@Test
	@Order(4)
	void testInvalidPlayNoStones() {
		game.getBoard().getHouses()[play.getCurrentMove()].clearStones();
		KalahaValidationException ex=
				assertThrows(KalahaValidationException.class, ()->
				validationService.validateGamePlay(game, play));
		game.getBoard().getHouses()[play.getCurrentMove()].addStones(6);
		assertThat(ex.getMessage()).isEqualTo("No Stones found in the pit!! Please try another move!");
	}
	
	@Test
	@Order(5)
	void testInvalidPlayMove() {
		play.setCurrentMove(6);
		KalahaValidationException ex=
				assertThrows(KalahaValidationException.class, ()->
				validationService.validateGamePlay(game, play));
		game.getBoard().getHouses()[play.getCurrentMove()].addStones(6);
		assertThat(ex.getMessage()).isEqualTo("Invalid Move!! You cannot pick stones from your Store!!");
	}
	
	@Test
	@Order(6)
	void testInvalidPlayer() {
		play.getPlayer().setId(1);
		game.setPlayers(players);
		KalahaValidationException ex=
				assertThrows(KalahaValidationException.class, ()->
				validationService.validateGamePlay(game, play));
		assertThat(ex.getMessage()).startsWith("Sorry!! Its");
	}
	
	@Test
	@Order(7)
	void testInvalidGame() {
		game.getGameState().setGameStatus(GameStatus.GAME_OVER);
		KalahaValidationException ex=
				assertThrows(KalahaValidationException.class, ()->
				validationService.validateGamePlay(game, play));
		game.getBoard().getHouses()[play.getCurrentMove()].addStones(6);
		assertThat(ex.getMessage()).isEqualTo("Game has already ended!!");
	}

}

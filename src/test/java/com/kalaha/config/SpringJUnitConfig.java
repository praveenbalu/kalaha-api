package com.kalaha.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import com.kalaha.engine.RuleEngine;
import com.kalaha.model.Board;
import com.kalaha.model.Game;
import com.kalaha.model.House;
import com.kalaha.model.Player;
import com.kalaha.model.request.Play;
import com.kalaha.rule.GameOverRule;
import com.kalaha.rule.GameRule;
import com.kalaha.service.BoardService;
import com.kalaha.service.GameService;
import com.kalaha.service.ValidationService;

@SpringJUnitWebConfig(SpringJUnitConfig.Config.class)
public class SpringJUnitConfig {

	@Configuration
	public static class Config {
		
		@Bean
		@Scope("prototype")
		public BoardService boardService() {
			return new BoardService();
		}
		
		@Bean
		@Scope("prototype")
		public GameService gameService() {
			return new GameService();
		}
		
		@Bean
		@Scope("prototype")
		public Game game() {
			Board board=boardService().createBoard(RuleEngine.BOARD_SIZE, RuleEngine.INITIAL_STONES);
			Game game = new Game(null, board, 0);
			return game;
		}
		
		@Bean
		@Scope("prototype")
		public List<Player> players(){
			return Arrays.asList(new Player(0,"Player 1"),new Player(1,"Player 2"));
		}
		
		@Bean
		public GameRule gameOverRule() {
			return new GameOverRule(game());
		}
		
		@Bean
		public GameRule gameOverRule1() {
			return new GameOverRule(game1());
		}
		
		@Bean
		public ValidationService validationService() {
			return new ValidationService();
		}
		
		@Bean
		@Scope("prototype")
		public Game game1() {
			Board board=boardService().createBoard(RuleEngine.BOARD_SIZE, RuleEngine.INITIAL_STONES);
			Game game = new Game(null, board, 1);
			House[] houses=game.getBoard().getHouses();
			IntStream.range(7,14).forEach(i-> {
				houses[i].clearStones();
			});
			houses[0].addStones(5);
			return game;
		}
		
		@Bean
		@Scope("prototype")
		public Play play() {
			Play play=new Play();
			play.setPlayer(new Player(0,"Player 1"));
			play.setCurrentMove(4);
			return play;
		}
		
		@Bean
		public RuleEngine ruleEngine() {
			return new RuleEngine();
		}
		
		@Bean
		public GameRule dummyGameRule() {
			return new GameRule(null) {
				
				@Override
				public void apply() {
					
				}
			};
		}
		
	}
}

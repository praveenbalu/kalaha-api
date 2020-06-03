package com.kalaha.rule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.enums.GameStatus;
import com.kalaha.model.Game;
import com.kalaha.rule.GameOverRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
public class GameOverRuleTest {
	
	
	@Autowired Game game;
	@Autowired Game game1;
	
	@Test
	public void testGameNotOver() {
		new GameOverRule(game).apply();
		assertThat(game.getGameState().getGameStatus()).isEqualTo(GameStatus.YET_TO_START);
	}
	
	@Test
	public void testGameOver() {
		new GameOverRule(game1).apply();
		assertThat(game1.getGameState().getGameStatus()).isEqualTo(GameStatus.GAME_OVER);
	}
}



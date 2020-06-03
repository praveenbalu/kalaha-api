package com.kalaha.rule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.model.Game;
import com.kalaha.rule.PlayerTurnChangedRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
class PlayerTurnChangedRuleTest {

	@Autowired
	Game game;
	
	@Test
	void testPlayerTurnChanged() {
		new PlayerTurnChangedRule(game, 4).apply();
		assertThat(game.getCurrentPlayerId()).isEqualTo(1);
	}

	@Test
	void testPlayerTurnNotChanged() {
		new PlayerTurnChangedRule(game, 6).apply();
		assertThat(game.getCurrentPlayerId()).isEqualTo(0);
	}
}

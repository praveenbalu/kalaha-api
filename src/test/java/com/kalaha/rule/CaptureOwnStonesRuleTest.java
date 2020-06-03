package com.kalaha.rule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.model.Game;
import com.kalaha.model.House;
import com.kalaha.rule.CaptureOwnStonesRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
class CaptureOwnStonesRuleTest {

	@Autowired
	Game game;
	
	@Test
	void testStonesCaptured() {
		setGame(game);
		new CaptureOwnStonesRule(game, 3).apply();
		assertThat(game.getBoard().getHouses()[6].getStones()).isEqualTo(1);
	}
	
	@Test
	void testStonesNotCaptured() {
		new CaptureOwnStonesRule(game, 5).apply();
		assertThat(game.getBoard().getHouses()[6].getStones()).isEqualTo(0);
	}
	
	private void setGame(Game game) {
		game.setCurrentPlayerId(0);
		House house=game.getBoard().getHouses()[3];
		house.clearStones();
		house.addOneStone();
	}
}

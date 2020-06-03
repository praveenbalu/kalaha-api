package com.kalaha.rule;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.model.Game;
import com.kalaha.model.House;
import com.kalaha.rule.CaptureStonesRule;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
@TestMethodOrder(OrderAnnotation.class)
class CaptureStonesRuleTest {
	
	@Autowired
	Game game;
	
	@Test
	void testStonesCaptured() {
		setGame(game);
		new CaptureStonesRule(game, 3).apply();
		assertThat(game.getBoard().getHouses()[6].getStones()).isEqualTo(7);
	}
	
	@Test
	void testStonesNotCaptured() {
		new CaptureStonesRule(game, 5).apply();
		assertThat(game.getBoard().getHouses()[6].getStones()).isEqualTo(0);
	}
	
	private void setGame(Game game) {
		game.setCurrentPlayerId(0);
		House house=game.getBoard().getHouses()[3];
		house.clearStones();
		house.addOneStone();
	}

}

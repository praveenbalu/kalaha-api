package com.kalaha.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.engine.RuleEngine;
import com.kalaha.enums.Rule;
import com.kalaha.exception.KalahaInvalidRuleException;
import com.kalaha.model.Game;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
class RuleEngineTest {
	
	@Autowired
	RuleEngine ruleEngine;
	
	@Autowired
	Game game;
	
	@Test
	public void testCreateValidRule() throws KalahaInvalidRuleException {
		assertThat(ruleEngine.createRule(Rule.GAME_OVER, game)).isNotNull();
	}

	@Test
	public void testCreateInValidRule() {
		KalahaInvalidRuleException ex=
				assertThrows(KalahaInvalidRuleException.class, ()->ruleEngine.createRule(Rule.CAPTURE_OWN_STONES, game)
						,"Expected exception while creating rule");
		assertThat(ex.getMessage()).contains("needs parameter as position");
	}
	
	@Test
	public void testCreateValidRuleWithPosition() throws KalahaInvalidRuleException {
		assertThat(ruleEngine.createRule(Rule.CAPTURE_OWN_STONES, 0, game)).isNotNull();
	}
	
	@Test
	public void testCreateInValidRuleWithPosition() {
		KalahaInvalidRuleException ex=
				assertThrows(KalahaInvalidRuleException.class, ()->ruleEngine.createRule(Rule.GAME_OVER, 0, game)
						,"Expected exception while creating rule");
		assertThat(ex.getMessage()).contains("does not need a position parameter");
	}
	
	@Test
	public void testAddNewRule() {
		assertThat(ruleEngine.addRule(Rule.CAPTURE_OPPONENT_STONES_ONEMPTY)).isTrue();
	}
	
	@Test
	public void testRemoveNewRule() {
		ruleEngine.addRule(Rule.CAPTURE_OPPONENT_STONES_ONEMPTY);
		assertThat(ruleEngine.removeRule(Rule.CAPTURE_OPPONENT_STONES_ONEMPTY)).isTrue();
	}
	
	@Test
	public void testAddDefaultRule() {
		assertThat(ruleEngine.addRule(Rule.GAME_OVER)).isFalse();
	}
	
	@Test
	public void testRemoveDefaultRule() {
		assertThat(ruleEngine.removeRule(Rule.GAME_OVER)).isFalse();	
	}
}

package com.kalaha.engine;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.kalaha.enums.Rule;
import com.kalaha.exception.KalahaInvalidRuleException;
import com.kalaha.model.Game;
import com.kalaha.rule.CaptureOwnStonesRule;
import com.kalaha.rule.CaptureStonesRule;
import com.kalaha.rule.GameOverRule;
import com.kalaha.rule.GameRule;

import lombok.NonNull;

/**
 * <p>
 * RuleEngine controls the rules of the game
 * Its helps to control, manage and also customize the rules
 * available in the game
 * 
 * </p>
 * @author Praveen
 * 
 */
@Service
public class RuleEngine {

	/** Customized rules for the specific game. */
	private Set<Rule> allowedRules;

	/** Default rules for all the games. */
	private static final Set<Rule> defaultRules;

	/** Initial number of stones per house. */
	public static final Integer INITIAL_STONES = 6;

	/** Initial board size */
	public static final Integer BOARD_SIZE = 14;

	static {
		defaultRules = new HashSet<>();
		defaultRules.add(Rule.GAME_OVER);
		defaultRules.add(Rule.PLAYER_TURN_CHANGED);
		defaultRules.add(Rule.CAPTURE_STONES);
	}

	/**
	 * Instantiates a new rule engine with default rules.
	 */
	public RuleEngine() {
		allowedRules = new HashSet<>(defaultRules);
	}

	/**
	 * Creates the rule object.
	 *
	 * @param the rule to be created
	 * @param game to execute the rule
	 * @return the game rule object of type {@link GameRule}
	 * @throws KalahaInvalidRuleException on invalid rules creation
	 */
	public GameRule createRule(@NonNull Rule rule, Game game) throws KalahaInvalidRuleException {
		if (rule == Rule.GAME_OVER)
			return new GameOverRule(game);
		throw new KalahaInvalidRuleException(String.format("Rule %s needs parameter as position", rule.toString()));
	}

	/**
	 * Creates the rule object with position param (if required).
	 * NOTE: Some rules might require position to execute
	 *
	 * @param the rule to be created
	 * @param position to execute the rule
	 * @param game to execute the rule
	 * @return the game rule object of type {@link GameRule}
	 * @throws KalahaInvalidRuleException on invalid rules creation
	 */
	public GameRule createRule(@NonNull Rule rule, int position, Game game) throws KalahaInvalidRuleException {
		switch (rule) {
		case CAPTURE_STONES:
			return new CaptureStonesRule(game, position);
		case CAPTURE_OWN_STONES:
			return new CaptureOwnStonesRule(game, position);
		default:
			throw new KalahaInvalidRuleException(
					String.format("Rule %s does not need a position parameter", rule.toString()));
		}
	}

	/**
	 * Method for the purpose of rules customization
	 * Adds the rule.
	 * @param rule to add
	 * @return true if added
	 */
	public Boolean addRule(Rule rule) {
		return this.allowedRules.add(rule);
	}

	/**
	 * Method for the purpose of rules customization
	 * Adds the rule.
	 * NOTE: Cannot remove default rule
	 * @param rule the rule
	 * @return true if added
	 */
	public Boolean removeRule(Rule rule) {
		return !defaultRules.contains(rule) ? this.allowedRules.remove(rule) : Boolean.FALSE;
	}

}

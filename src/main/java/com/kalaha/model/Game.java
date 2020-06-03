package com.kalaha.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString

/**
 * Gets the game state.
 *
 * @return the game state
 */
@Getter

/**
 * Sets the game state.
 *
 * @param gameState
 *            the new game state
 */
@Setter
@Document(collection = "Games")
@JsonIgnoreProperties(value = "players")
@Component
public class Game implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7028548628587546069L;

	/** The game id. */
	@Id
	private String gameId;

	/** The players. */
	private List<Player> players;

	/** The board. */
	private Board board;

	/** The current player id. */
	private int currentPlayerId;

	/** The player. */
	@Transient
	private Player player;

	/** The game state. */
	@DBRef
	private GameState gameState;

	/**
	 * Instantiates a new game.
	 */
	public Game() {
		gameState = new GameState();
	}

	/**
	 * Instantiates a new game.
	 *
	 * @param players
	 *            the players
	 * @param board
	 *            the board
	 * @param currentPlayerId
	 *            the current player id
	 */
	public Game(List<Player> players, Board board, Integer currentPlayerId) {
		this();
		this.players = players;
		this.board = board;
		this.currentPlayerId = currentPlayerId;
	}
}

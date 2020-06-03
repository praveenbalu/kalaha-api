package com.kalaha.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Instantiates a new house.
 *
 * @param the player id
 * @param stones
 * @param store
 * 
 */
@AllArgsConstructor

@Getter
public class House implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5075266180558865102L;

	/** The player id. */
	private Integer playerId;

	/** The stones. */
	private Integer stones;

	/**
	 * Clear stones.
	 */
	public void clearStones() {
		this.stones = 0;
	}

	/**
	 * Adds the one stone.
	 */
	public void addOneStone() {
		this.stones++;
	}

	/**
	 * Adds the stones.
	 *
	 * @param stones
	 *            the stones
	 */
	public void addStones(Integer stones) {
		this.stones += stones;
	}
}

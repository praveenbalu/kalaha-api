package com.kalaha.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Board contains the house components of the 
 * particular game
 * 
 * Example Board instance:
 * 
 *    6 6 6 6 6 6 -> House objects 
 * 0              0 -> House object 
 *    6 6 6 6 6 6 -> House objects  
 * 
 */

/**
 * Instantiates a new board.
 *
 * @param houses which contains stones
 */
@AllArgsConstructor

/**
 * Gets the houses.
 *
 * @return the houses
 */
@Getter
public class Board implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5210251012674730338L;

	/** The houses. */
	private House[] houses;
}

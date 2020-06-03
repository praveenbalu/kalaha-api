package com.kalaha.util;

/**
 * The Class KalahaUtils.
 * @author Praveen
 * 
 */
public class KalahaUtils {

	/**
	 * Instantiates a new kalaha utils.
	 */
	private KalahaUtils() {
	}

	/**
	 * Find opponent index.
	 *
	 * @param index
	 *            the index
	 * @param boardSize
	 *            the board size
	 * @return the int
	 */
	public static int findOpponentIndex(int index, int boardSize) {
		return boardSize - index - 1;
	}

	/**
	 * Find store index by id.
	 *
	 * @param playerId
	 *            the player id
	 * @param boardSize
	 *            the board size
	 * @return the int
	 */
	public static int findStoreIndexById(int playerId, int boardSize) {
		return (playerId == 0 ? boardSize / 2 : boardSize) - 1;
	}
	
	
	/**
	 * Returns if the given house index is store
	 * @param index
	 * @param boardSize
	 * @return true, if it is store
	 */
	public static boolean isStore(int index, int boardSize) {
		return index == boardSize-1 
				|| index == (boardSize/2)-1;
	}

	/**
	 * Switch player.
	 *
	 * @param playerId
	 *            the player id
	 * @return the int
	 */
	public static int switchPlayer(int playerId) {
		return 1 - playerId;
	}

}

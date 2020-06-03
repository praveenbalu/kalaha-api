package com.kalaha.service;

import static com.kalaha.util.KalahaUtils.findStoreIndexById;
import static com.kalaha.util.KalahaUtils.isStore;

import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.kalaha.engine.RuleEngine;
import com.kalaha.model.Board;
import com.kalaha.model.House;

/**
 * Class to manage the services of the board
 * @author Praveen
 */
@Service("boardService")
public class BoardService {

	/**
	 * Creates a new Board object with
	 * {@Link House} objects inside it
	 *
	 * Example:
	 * For a 14 size board with 6 stones
	 *    6 6 6 6 6 6 --> Player 2 side with id/index : 1
	 * 0              0 --> Store of player id : 0
	 *    6 6 6 6 6 6 --> Player 1 side with id/index : 0 
	 * Returns houses[14]
	 * index:0 1 2 3 4 5 6   7 8 9 10 11 12 13
	 *		 6 6 6 6 6 6 0   6 6 6  6  6  6  0
	 * 		 | | | | | | |   | | |  |  |  |  |
	 * 		 ---Player 1--   ---Player 2------
	 * 
	 * @param size of the board
	 * @param number of stones inside each house
	 * @return the board
	 */
	public Board createBoard(int boardSize, int stonesCount) {
		House[] houses = new House[boardSize];
		IntStream.range(0, houses.length).boxed().forEach(i -> {
			int playerId = i <= houses.length / 2 - 1 ? 0 : 1;
			boolean isStore = i == (boardSize / 2 - 1) || i == boardSize - 1;
			int initialStones = isStore ? 0 : stonesCount;
			houses[i] = new House(playerId, initialStones);
		});
		return new Board(houses);
	}

	/**
	 * Capture stones from the pits and puts in store house
	 * If the game needs to finalized
	 *
	 * Example:
	 * Consider 6 x 6 Board
	 *    3 4 5 1 2 3
	 * 22             33
	 *    0 0 0 0 0 0 
	 * After finalizing:
	 *    0 0 0 0 0 0
	 * 40             33
	 *    0 0 0 0 0 0  
	 *    
	 * @param Houses in the board including store
	 * @param size of the board
	 */
	public void finalizeStoreStones(House[] houses, int boardSize) {
		House player1Store = houses[boardSize / 2 - 1];
		House player2Store = houses[boardSize - 1];

		IntStream.range(0, houses.length - 1).boxed().forEach(i -> {
			House house = houses[i];
			if (isStore(i,boardSize))
				return;

			if (house.getPlayerId() == 0)
				player1Store.addStones(house.getStones());
			else
				player2Store.addStones(house.getStones());

			house.clearStones();
		});
	}

	/**
	 * Gets the stones from the stores
	 * 
	 * Example:
	 * Consider the houses:
	 *    0 4 3 0 2 0
	 * 22             33
	 *    0 0 3 0 1 0  
	 * returns {33,22}
	 *    
	 * @param houses
	 * @param board size
	 * @return the stones in the player stores
	 */
	public Integer[] getStoreStones(House[] houses, int boardSize) {
		return new Integer[] { houses[findStoreIndexById(0, boardSize)].getStones(),
				houses[findStoreIndexById(1, boardSize)].getStones() };
	}

	/**
	 * Checks if is player side houses empty excluding store.
	 *
	 * @param houses
	 * @param playerId
	 * @return true, if is player houses empty
	 */
	public boolean isPlayerHousesEmpty(House[] houses, Integer playerId) {

		int startRange = playerId == 0 ? 0 : (houses.length / 2);
		int endRange = playerId == 0 ? (houses.length / 2) - 1 : houses.length;

		return IntStream.range(startRange, endRange).boxed().noneMatch(i -> {
			House house = houses[i];
			return house.getPlayerId().intValue() == playerId && !isStore(i,RuleEngine.BOARD_SIZE) && house.getStones() != 0;
		});
	}

}

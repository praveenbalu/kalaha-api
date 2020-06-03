package com.kalaha.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.engine.RuleEngine;
import com.kalaha.model.Board;
import com.kalaha.model.House;
import com.kalaha.service.BoardService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Test cases for Board Service methods")
public class BoardServiceTest {
	
	@Autowired
	private BoardService boardService;
	
	private Board board;
	private House[] houses;
	
	@BeforeAll
	public void setUp() {
		board=boardService.createBoard(RuleEngine.BOARD_SIZE, RuleEngine.INITIAL_STONES);
		houses=board.getHouses(); 	
	}
	
	@Test
	@Order(1)
	public void testCreateGame() {
		assertThat(board).isNotNull().withFailMessage("Board object created cannot be null");
		assertThat(houses).hasSize(RuleEngine.BOARD_SIZE).withFailMessage("Invalid House size");
		assertThat(houses[0].getStones()).isEqualTo(RuleEngine.INITIAL_STONES)
					.withFailMessage("Initial stones in the house are supposed to be %s",RuleEngine.INITIAL_STONES);
	}
	
	@Test
	@Order(2)
	public void testFinalizeStoreStones() {
		boardService.finalizeStoreStones(houses, RuleEngine.BOARD_SIZE);
		assertThat(houses[6].getStones()).isEqualTo(36).withFailMessage("Store stones not matching for player 1");
		assertThat(houses[13].getStones()).isEqualTo(36).withFailMessage("Store stones not matching for player 2");
	}
	
	@Test
	@Order(3)
	public void testgetStoreStones() {
		Integer[] stones=boardService.getStoreStones(houses, RuleEngine.BOARD_SIZE);
		assertThat(stones[0]).isEqualTo(36).withFailMessage("Store stones not matching for player 1");
		assertThat(stones[1]).isEqualTo(36).withFailMessage("Store stones not matching for player 2");
	}
	
	@Test
	@Order(4)
	public void testIsPlayerHousesEmpty() {
		assertThat(boardService.isPlayerHousesEmpty(houses, 0)).isTrue().withFailMessage("Player 1 house should be empty");
		assertThat(boardService.isPlayerHousesEmpty(houses, 1)).isTrue().withFailMessage("Player 2 house should be empty");
	}
	
	@Test
	@Order(5)
	public void testIsPlayerHousesNotEmpty() {
		houses[0].addOneStone();
		houses[9].addOneStone();
		assertThat(boardService.isPlayerHousesEmpty(houses, 0)).isFalse().withFailMessage("Player 1 house should not be empty");
		assertThat(boardService.isPlayerHousesEmpty(houses, 1)).isFalse().withFailMessage("Player 2 house should not be empty");
	}
}

package com.kalaha.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalaha.config.SpringJUnitConfig;
import com.kalaha.controller.GameController;
import com.kalaha.model.Game;
import com.kalaha.model.Player;
import com.kalaha.model.response.GameStats;
import com.kalaha.service.GameService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
@ComponentScan(basePackageClasses=com.kalaha.controller.GameController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = SpringJUnitConfig.Config.class)
class GameControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private GameService gameService;
	
	@Autowired
	Game game;
		
	@Test
	public void testNewGame() throws Exception {
		ObjectMapper mapper=new ObjectMapper();
		when(gameService.createGame(any(Player.class))).thenReturn(game);
		mockMvc.perform(post("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Player(null,"Player 2"))))
				.andExpect(status().isOk());
		
		verify(gameService,times(1)).createGame(any(Player.class));
		verifyNoMoreInteractions(gameService);
				
	}
	
	@Test
	public void testNewGameBadRequest() throws Exception {
		mockMvc.perform(post("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest());	
		verifyNoMoreInteractions(gameService);
	}
	
	@Test
	public void testJoinGame() throws Exception {
		ObjectMapper mapper=new ObjectMapper();
		when(gameService.joinGame(anyString(),any(Player.class))).thenReturn(game);
		mockMvc.perform(put("/game/101dummy")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new Player(1,"Player 2"))))
				.andExpect(status().isOk());
		verify(gameService,times(1)).joinGame(anyString(),any(Player.class));
		verifyNoMoreInteractions(gameService);
	}
	
	@Test
	public void testGameInfo() throws Exception {
		when(gameService.getGameStats(anyString())).thenReturn(new GameStats());
		mockMvc.perform(get("/game/101dummy")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		verify(gameService,times(1)).getGameStats(anyString());
		verifyNoMoreInteractions(gameService);
	}
	

}

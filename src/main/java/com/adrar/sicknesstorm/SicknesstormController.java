package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.text.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SicknesstormController {
	@Autowired
	SicknesstormService sicknesstormService;
	
	Game game = new Game();
	
  @MessageMapping("/message")
  @SendTo("/topic/message")
  public Game getGame() {
      return game;
  }	
  
  @MessageMapping("/getPlayerJoined")
  @SendTo("/topic/message")
  public List<Player> getPlayerJoined(String namePlayer){
	  Player player = new Player();
	  player.setId(this.game.getPlayerList().size()+1);
	  player.setName(namePlayer);
	  return sicknesstormService.addPlayer(player, this.game);  
  }

//	@GetMapping("/players")
//	public List<Player> getAllPlayers(){
//		return sicknesstormService.getAllPlayers();
//	}
	
	@GetMapping("/territory/{id}")
	public Territory getTerritory(@PathVariable Integer id) {
		return sicknesstormService.getAterritory(id);
	}
	
	@MessageMapping("/territories")
	@SendTo("/topic/territories")
	public List<Territory> getTerritoriesSocket(){
		return this.game.getTerritories();
	}

	@MessageMapping("/movefortify")
	@SendTo("/topic/message")
	public Game moveFortify(List<Integer> message) {
		return sicknesstormService.movePawns(message.get(0), message.get(1), message.get(2), this.game);
	}
	
	@GetMapping("/isadjacent/{territoryA}/{territoryB}")
	public boolean isAdjacent(@PathVariable int territoryA, @PathVariable int territoryB) {
		return sicknesstormService.isAdjacent(territoryA, territoryB);
	}
	
	@MessageMapping("/addpawn")
	@SendTo("/topic/message")
	public Game addPawn(List<Integer> message) {
		return sicknesstormService.addPawn(message.get(0), message.get(1), this.game );
	}
	
	@MessageMapping("/fight")
	@SendTo("/topic/message")
	public Game startFight(List<Integer> message) {
		return sicknesstormService.buildFight(message, this.game);
	}
	
	@MessageMapping("/answerfight")
	@SendTo("/topic/message")
	public Game startFight(int message) {
		return sicknesstormService.answerFight(message, this.game);
	}
	
	@MessageMapping("/initializegame")
	@SendTo("/topic/message")
	public Game initializeGame() {
		return sicknesstormService.initializeGame(this.game);
	}
	
	@MessageMapping("/closefightstep")
	@SendTo("/topic/message")
	public Game closeFightStep() {
		return sicknesstormService.closeFightStep(this.game);
	}
	
	@MessageMapping("/closemovefortifystep")
	@SendTo("/topic/message")
	public Game closeMoveFortifyStep() {
		return sicknesstormService.closeMoveFortifyStep(this.game);
	}
	
	@GetMapping("/roundphase")
	public GamePhase gamePhase() {
		return sicknesstormService.gamePhase(this.game);
	}
	
	@GetMapping("/playerturn")
	public Player playerTurn() {
		return sicknesstormService.playerTurn(game);
	}
}



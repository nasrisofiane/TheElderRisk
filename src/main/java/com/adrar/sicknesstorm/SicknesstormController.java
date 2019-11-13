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
	  game.setTerritories(sicknesstormService.getTerritories());
      return game;
  }	
  
  @MessageMapping("/getPlayerJoined")
  @SendTo("/topic/message")
  public List<Player> getPlayerJoined(String namePlayer){
	  Player player = new Player();
	  player.setName(namePlayer);
	  if(sicknesstormService.getAllPlayers().size() <= 10) {
		  sicknesstormService.addPlayer(player); 
		  return sicknesstormService.getAllPlayers();
	  }else {
		  return null;
	  }
  }
	
	@GetMapping("/player/{id}")
	public Player getPlayer(@PathVariable Integer id) {
		return sicknesstormService.getAplayer(id);																																																																																																																																																																												
	}
	
	@GetMapping("/players")
	public List<Player> getAllPlayers(){
		return sicknesstormService.getAllPlayers();
	}
	
	@GetMapping("/territory/{id}")
	public Territory getTerritory(@PathVariable Integer id) {
		return sicknesstormService.getAterritory(id);
	}
	
	@MessageMapping("/territories")
	@SendTo("/topic/territories")
	public List<Territory> getTerritoriesSocket(){
		return sicknesstormService.getTerritories();
	}

	@GetMapping("/movefortify/{idTerritoryA}/{idTerritoryB}/{nbPawns}")
	public String moveFortify(@PathVariable int idTerritoryA, @PathVariable int idTerritoryB, @PathVariable int nbPawns ) {
		return sicknesstormService.movePawns(idTerritoryA, idTerritoryB, nbPawns, this.game);
	}
	
	@GetMapping("/isadjacent/{territoryA}/{territoryB}")
	public boolean isAdjacent(@PathVariable int territoryA, @PathVariable int territoryB) {
		return sicknesstormService.isAdjacent(territoryA, territoryB);
	}
	
	@MessageMapping("/addpawn")
	@SendTo("/topic/message")
	public Game addPawn(List<Integer> message) {
		sicknesstormService.addPawn(message.get(0), message.get(1), this.game );
		this.game.setTerritories(sicknesstormService.getTerritories());
		return this.game;
	}
	
	@MessageMapping("/fight")
	@SendTo("/topic/message")
	public Game startFight(List<Integer> message) {
		sicknesstormService.buildFight(message, this.game);
		this.game.setTerritories(sicknesstormService.getTerritories());
		return this.game;
	}
	
	@MessageMapping("/answerfight")
	@SendTo("/topic/message")
	public Game startFight(int message) {
		sicknesstormService.answerFight(message, this.game);
		this.game.setTerritories(sicknesstormService.getTerritories());
		return this.game;
	}
	
	@GetMapping(value = "/attp/{idplayer}/{idTerritory}")  /*"attp" signifie addTerritoryToPlayer*/
	public void addTerritoryToPlayer(@PathVariable int idplayer ,@PathVariable int idTerritory) {
		sicknesstormService.addTerritoryToPlayer(idplayer, idTerritory);
	}
	
	@MessageMapping("/initializegame")
	@SendTo("/topic/message")
	public Game initializeGame() {
		sicknesstormService.initializeGame(this.game);
		game.setTerritories(sicknesstormService.getTerritories());
		return game;
	}
	
	@GetMapping("/closefightstep")
	public String closeFightStep() {
		return sicknesstormService.closeFightStep(this.game);
	}
	
	@MessageMapping("/closemovefortifystep")
	@SendTo("/topic/message")
	public Game closeMoveFortifyStep() {
		sicknesstormService.closeMoveFortifyStep(this.game);
		return this.game;
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



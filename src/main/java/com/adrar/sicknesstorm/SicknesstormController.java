package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.text.View;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping("/territories")
	public List<Territory> getTerritories(){
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

	@PostMapping(value = "/addplayer")
	public void addPlayer(@RequestBody Player player) {
		sicknesstormService.addPlayer(player);
	}
	
	@GetMapping(value = "/addpawn/{idTerritory}/{pawn}")
	public void addPawn(@PathVariable int idTerritory, @PathVariable int pawn) {
		sicknesstormService.addPawn(idTerritory, pawn, this.game );
	}

	@GetMapping(value = "/fight/{idTerritoryAtk}/{idTerritoryDef}/{nbAttack}/{nbDefense}")
	public String startFight(@PathVariable int idTerritoryAtk ,@PathVariable int idTerritoryDef, @PathVariable int nbAttack , @PathVariable int nbDefense) {
		return sicknesstormService.startFight(idTerritoryAtk, idTerritoryDef, nbAttack, nbDefense, this.game);
	}
	
	@GetMapping(value = "/attp/{idplayer}/{idTerritory}")  /*"attp" signifie addTerritoryToPlayer*/
	public void addTerritoryToPlayer(@PathVariable int idplayer ,@PathVariable int idTerritory) {
		sicknesstormService.addTerritoryToPlayer(idplayer, idTerritory);
	}
	
	@GetMapping("/initializegame")
	public Game initializeGame() {
		sicknesstormService.initializeGame(this.game);
		return game;
	}
	
	@GetMapping("/closefightstep")
	public String closeFightStep() {
		return sicknesstormService.closeFightStep(this.game);
	}
	
	@GetMapping("/closemovefortifystep")
	public String closeMoveFortifyStep() {
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



package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.text.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class SicknesstormController {
	@Autowired
	SicknesstormService sicknesstormService;
	
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
	public void moveFortify(@PathVariable int idTerritoryA, @PathVariable int idTerritoryB, @PathVariable int nbPawns ) {
		sicknesstormService.movePawns(idTerritoryA, idTerritoryB, nbPawns);
	}
	
	@GetMapping("/isadjacent/{territoryA}/{territoryB}")
	public boolean isAdjacent(@PathVariable int territoryA, @PathVariable int territoryB) {
		return sicknesstormService.isAdjacent(territoryA, territoryB);
	}

	@PostMapping(value = "/addplayer")
	public void addPlayer(@RequestBody Player player) {
		sicknesstormService.addPlayer(player);
	}

	
	@GetMapping(value = "/addpawn/{idPlayer}/{idTerritory}/{pawn}")
	public void addPawn(@PathVariable int idPlayer ,@PathVariable int idTerritory, @PathVariable int pawn) {
		sicknesstormService.addPawn(idPlayer ,idTerritory, pawn );
	}
	
	
	@GetMapping(value = "/fight/{idTerritoryAtk}/{idTerritoryDef}/{nbAttack}/{nbDefense}")
	public void startFight(@PathVariable int idTerritoryAtk ,@PathVariable int idTerritoryDef, @PathVariable int nbAttack , @PathVariable int nbDefense) {
		sicknesstormService.startFight(idTerritoryAtk, idTerritoryDef, nbAttack, nbDefense);
	}
	
	@GetMapping(value = "/attp/{idplayer}/{idTerritory}")
	public void addTerritoryToPlayer(@PathVariable int idplayer ,@PathVariable int idTerritory) {
		sicknesstormService.addTerritoryToPlayer(idplayer, idTerritory);
	}
	
	
	/*@GetMapping("/save") Methode de test pour moveFortify, à ne pas toucher !!
	public void savePlayerIntoATerritory() {
		sicknesstormService.savePlayerIntoATerritory();
	}*/
}



package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.text.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		Territory changeFormatTerritory = sicknesstormService.getAterritory(id);
		for(Territory territory : changeFormatTerritory.getTerritoryAdjacent()) {
			territory.setTerritoryAdjacent(null);
			territory.setPlayer(null);
		}
		return changeFormatTerritory;
	}
	
	@GetMapping("/territories")
	public List<Territory> getTerritories(){
		return sicknesstormService.getTerritories();
	}
	
	@GetMapping("/movefortify/{idTerritoryA}/{idTerritoryB}/{nbPawns}")
	public boolean moveFortify(@PathVariable int idTerritoryA, @PathVariable int idTerritoryB, @PathVariable int nbPawns ) {
		return sicknesstormService.movePawns(idTerritoryA, idTerritoryB, nbPawns);
	}
	
	@GetMapping("/isadjacent/{territoryA}/{territoryB}")
	public boolean isAdjacent(@PathVariable int territoryA, @PathVariable int territoryB) {
		return sicknesstormService.isAdjacent(territoryA, territoryB);
	}
}

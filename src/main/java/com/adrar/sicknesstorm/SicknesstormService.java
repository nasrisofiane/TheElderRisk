package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SicknesstormService{
	@Autowired
	PlayerRepository playerRepo;
	
	@Autowired
	TerritoryRepository territoryRepo;
	
	public Player getAplayer(Integer id){
		return playerRepo.getOne(id);
	}
	
	public List<Player> getAllPlayers(){
		return playerRepo.findAll();
	}
	
	public Territory getAterritory(Integer id) {
		//territoryRepo.getOne(id).testTer(territoryRepo.getOne(id));
		return territoryRepo.findById(id).get();
	}
	
	public List<Territory> getTerritories(){
		return territoryRepo.findAll();
	}

	public boolean isAdjacent(int territoryA, int territoryB) {
		return this.getAterritory(territoryA).isAdjacent(this.getAterritory(territoryB));
	}
	
	public void addPlayer(Player player) {
		 playerRepo.save(player);
	}
	
	//Initialize the game with all the players, shuffle the territories and start Round method.
	public void initializeGame(Game game) {
		if(game.getPhase() == GamePhase.INITIALIZE) {
			game.initialize(this.getAllPlayers());
			List<Territory> territories = this.getTerritories();
			Collections.shuffle(territories);
			int j = 0;
			
			for(Player player : game.getPlayerList()) { //affect all territories randomly to all players.
				for(int i = j; i < territories.size(); i++) {
					territories.get(i).setPlayer(player);
					territories.get(i).setPawn(5);
					territoryRepo.save(territories.get(i));
					if(i == j + Math.round(territories.size() / game.getPlayerList().size())) {
						j = i;
						break;
					}
				}
			}
			game.round();
		}
		else {
			System.out.println("GAME ALREADY INITIALIZED");
		}
	}
	
	public String addPawn(int idTerritory , int pawn, Game game ) {
			Territory territory = this.getAterritory(idTerritory);
			
			if(game.getPlayerTurn().getId() == territory.getPlayer().getId()) {
				if(game.getPawnsToPlace() > 0 && pawn <= game.getPawnsToPlace()) {
					
					game.setPawnsToPlace(game.getPawnsToPlace() - pawn);
					territory.setPawn(territory.getPawn() + pawn);
				   	territoryRepo.save(territory);
				   	if(game.getPawnsToPlace() == 0) {
				   		game.setPhase(GamePhase.ATTACK);
				   	}
				   	else {
				   		System.out.println("YOU HAVE TO PLACE ALL YOUR AVAILABLE PAWNS");
				   		return "YOU HAVE TO PLACE ALL YOUR AVAILABLE PAWNS";
				   	}
				   	return "Lasts "+game.getPawnsToPlace()+" to place";
				}
				else {
					System.out.println("NOT ENOUGH PAWNS");
					return "NOT ENOUGH PAWNS";
				}
			   	
			}
			else {
				System.out.println("NOT YOUR TERRITORY => " + game.getPlayerTurn().getName()+ " => TERRITORY OWNER" + territory.getPlayer().getName());
				return "NOT YOUR TERRITORY => " + game.getPlayerTurn().getName()+ " => TERRITORY OWNER " + territory.getPlayer().getName();
			}
		
	}
	
	public String movePawns (int idTerritorya , int idTerritoryb , int pawn, Game game) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY) {
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritorya).getPlayer().getId()) {
					this.getAterritory(idTerritorya).moveFortify(this.getAterritory(idTerritoryb), pawn);
				   	territoryRepo.save(this.getAterritory(idTerritorya));
					territoryRepo.save(this.getAterritory(idTerritoryb));  
					return "YOUR PAWNS HAVE BEEN WELL MOVED FROM "+this.getAterritory(idTerritorya).getName()+" TO "+this.getAterritory(idTerritoryb).getName()+"";
			}
		}
		else {
			return "MOVE PAWNS NOT AVAILABLE";
		}
		return "";
	}
	
	public String startFight (int idTerritoryAtk , int idTerritoryDef , int nbAttack , int nbDefense, Game game) {
		if(game.getPhase() == GamePhase.ATTACK) {
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritoryAtk).getPlayer().getId()) {
				if(this.getAterritory(idTerritoryDef).getPlayer().getId() == game.getPlayerTurn().getId()) {
					return "CANNOT ATTACK YOUR OWN TERRITORIES";
				}
				else {
					ArrayList<ArrayList<Integer>> result = this.getAterritory(idTerritoryAtk).attack(this.getAterritory(idTerritoryDef), nbAttack , nbDefense);
					territoryRepo.save(this.getAterritory(idTerritoryAtk));
					territoryRepo.save(this.getAterritory(idTerritoryDef));
					return result.toString();
				}
			}
			else {
				return "NOT YOUR TERRITORY";
			}
		}
		else {
			return "ATTACK IS NOT AVAILABLE";
		}
	}
	
	public void addTerritoryToPlayer ( int idplayer , int idTerritory) {
		this.getAterritory(idTerritory).setPlayer(this.getAplayer(idplayer));
		playerRepo.save(this.getAplayer(idplayer));
		territoryRepo.save(this.getAterritory(idTerritory));
	}
	
	public String closeFightStep(Game game) {
		if(game.getPhase() == GamePhase.ATTACK) {
			game.setPhase(GamePhase.MOVEFORTIFY);
			return "PHASE CHANGED TO => "+ game.getPhase();
		}
		else {
			return "CANNOT CLOSE THE PHASE, PHASE =>"+game.getPhase().toString();
		}
	}
	
	public String closeMoveFortifyStep(Game game) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY) {
			game.round();
			return "NEXT PLAYER";
		}
		else {
			return "CANNOT CLOSE THE PHASE, PHASE =>"+game.getPhase().toString();
		}
	}
	
	public GamePhase gamePhase(Game game) {
		return game.getPhase();
	}
	
	public Player playerTurn(Game game) {
		return game.getPlayerTurn();
	}
}
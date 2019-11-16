package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SicknesstormService{
	
	@Autowired
	TerritoryRepository territoryRepo;
	
	private List<Integer> lastFightTerritories = new ArrayList<>();
	
	public List<Integer> getLastFightTerritories() {
		return lastFightTerritories;
	}

	public void setLastFightTerritories(List<Integer> lastFightTerritories) {
		this.lastFightTerritories = lastFightTerritories;
	}
	
	public List<Territory> getTerritories(){
		return territoryRepo.findAll();
	}

	public boolean isAdjacent(int territoryA, int territoryB, Game game) {
		Territory territoryAdjacentA = null;
		Territory territoryAdjacentB = null;
		for(Territory territory : game.getTerritories()) {
			if(territory.getId() == territoryA) {
				territoryAdjacentA = territory;
			}
			if(territory.getId() == territoryB) {
				territoryAdjacentB = territory;
			}
		}
		return territoryAdjacentA.isAdjacent(territoryAdjacentB);
	}
	
	public List<Player> addPlayer(Player player, Game game) {
		
		for(Player playerInList : game.getPlayerList()) {
			if(playerInList.getSessionId() == SimpAttributesContextHolder.currentAttributes().getSessionId()) {
				System.out.println("ALREADY ADDED");
				return game.getPlayerList();
			}
		}
		
		player.setSessionId(SimpAttributesContextHolder.currentAttributes().getSessionId());
		game.addPlayer(player);
		System.out.println("GOT IT => " +player.getSessionId());
		return game.getPlayerList();
	}

	/**
	 * Initilize the game with a suffle of all territories and attributes territories to each player.
	 * @param game
	 */
	public Game initializeGame(Game game) {
		if(game.getPhase() == GamePhase.INITIALIZE) {
			List<Territory> retrieveTerritories = this.getTerritories();
			Collections.shuffle(retrieveTerritories);
			game.setTerritories(retrieveTerritories);
			
			//Algorithm that gives all territory randomly to each player.
			int playerIndex = 0;

			for(Territory territory : game.getTerritories()) {
				game.getPlayerList().get(playerIndex).addTerritory(territory);
				territory.setPlayer(game.getPlayerList().get(playerIndex));
				territory.setPawn(3);
				playerIndex += 1;
				if(playerIndex >= game.getPlayerList().size()) {
					playerIndex = 0;
				}
				System.out.println("TERRITORY NAME => "+ territory.getName() + " | OWNER => "+ territory.getPlayer().getName());
			}
			
			//End of the algorithm
			game.round();
			return game;
		}
		else {
			System.out.println("GAME ALREADY INITIALIZED");
		}
		return null;
	}
	
	public Game addPawn(int idTerritory , int pawn, Game game ) {
			Territory territoryAddPawn = null;
			
			for(Territory territory : game.getTerritories()) {
				if(territory.getId() == idTerritory) {
					territoryAddPawn = territory;
					break;
				}
			}
			
			/* condition that check if the id of the playerTurn is equal to the territory
			that he want to add pawn*/
			if(game.getPlayerTurn().getId() == territoryAddPawn.getPlayer().getId()) {
				if(game.getPawnsToPlace() > 0 && pawn <= game.getPawnsToPlace()) {
					game.setPawnsToPlace(game.getPawnsToPlace() - pawn);
					territoryAddPawn.setPawn(territoryAddPawn.getPawn() + pawn);
				   	if(game.getPawnsToPlace() == 0) {
				   		game.setPhase(GamePhase.ATTACK);
				   	}
				   	else {
				   		System.out.println("YOU HAVE TO PLACE ALL YOUR AVAILABLE PAWNS");
				   	}
				}
				else {
					System.out.println("NOT ENOUGH PAWNS");
				}
			   	
			}
			else {
				System.out.println("NOT YOUR TERRITORY => " + game.getPlayerTurn().getName()+ " => TERRITORY OWNER" + territoryAddPawn.getPlayer().getName());
			}
		return game;
	}
	
	/**
	 * Move the pawns from the territoryA to the territoryB if it respect the conditions.
	 * @param idTerritorya
	 * @param idTerritoryb
	 * @param pawn
	 * @param game
	 * @return
	 */
	public Game movePawns (int idTerritoryA , int idTerritoryB , int pawn, Game game) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY) {

			Territory territoryA = null;
			Territory territoryB = null;
			
				for(Territory territory : game.getTerritories()) {
					if(territory.getId() == idTerritoryA) {
						territoryA = territory;
					}
					if(territory.getId() == idTerritoryB) {
						territoryB = territory;
					}
				}
				
			// Condition that check if the territoryA AND territoryB owner is equal to the player's turn
			if(game.getPlayerTurn().getId() == territoryA.getPlayer().getId() && game.getPlayerTurn().getId() == territoryB.getPlayer().getId()) {
					if(territoryA.moveFortify(territoryB, pawn) == true) { 
						System.out.println( "YOUR PAWNS HAVE BEEN WELL MOVED FROM "+territoryA.getName()+" TO "+territoryB.getName()+"");
					}
					else {
						System.out.println( "TERRITORY NOT NEIGHBOR ");
					}
			}
			else {
				System.out.println( "NOT YOUR TERRITORY");
			}
		}
		else {
			System.out.println( "MOVE PAWNS NOT AVAILABLE");
		}
		return game;
	}
	
	/**
	 * Method that take the attacks informations and store it in an array called "lastFightTerritories"
	 * also set the GetAttacked from the Class Game to the name of the owner of the territory that get attacked.
	 * @param fightRequestInfos
	 * @param game
	 * @return game
	 */
	public Game buildFight (List<Integer> fightRequestInfos, Game game) {
		
		Territory territoryAtk = null;
		Territory territoryDef = null;
		
		for(Territory territory : game.getTerritories()) {
			if(territory.getId() == fightRequestInfos.get(0)) {
				territoryAtk = territory;
			}
			if(territory.getId() == fightRequestInfos.get(1)) {
				territoryDef = territory;
			}
		}
		
		if(territoryAtk.getPlayer().getId() != territoryDef.getPlayer().getId()) {
			if(game.getPhase() == GamePhase.ATTACK && this.isAdjacent(fightRequestInfos.get(0), fightRequestInfos.get(1), game) == true) {
				game.setGetAttacked(territoryDef.getPlayer().getName());
				this.lastFightTerritories = fightRequestInfos;
			}
		}
		else {
			System.out.println("Cannot attack your own territories");
		}
		return game;
	}
	
	/**
	 * Method that will call Start fight with the array's values and the parameter nbDefense sent from the controller
	 * @param nbDefense
	 * @param game
	 * @return game
	 */
	public Game answerFight (int nbDefense, Game game) {
		if(game.getPhase() == GamePhase.ATTACK) {
			this.startFight(this.lastFightTerritories.get(0), this.lastFightTerritories.get(1), this.lastFightTerritories.get(2), nbDefense, game);
		}
		return game;
	}
	
	/**
	 * Method that start a fight with the parameters passed, once the fight is done, the GetAttaked
	 * from Game class is set to null same for the array "lastFightTerritories"
	 * @param idTerritoryAtk
	 * @param idTerritoryDef
	 * @param nbAttack
	 * @param nbDefense
	 * @param game
	 * @return
	 */
	public String startFight (int idTerritoryAtk , int idTerritoryDef , int nbAttack , int nbDefense, Game game) {
		Territory territoryAtk = null;
		Territory territoryDef = null;
		
		if(game.getPhase() == GamePhase.ATTACK) {
			
			for(Territory territory : game.getTerritories()) {
				if(territory.getId() == idTerritoryAtk) {
					territoryAtk = territory;
				}
				if(territory.getId() == idTerritoryDef) {
					territoryDef = territory;
				}
			}
			
			if(game.getPlayerTurn().getId() == territoryAtk.getPlayer().getId()) {
				if(territoryDef.getPlayer().getId() == game.getPlayerTurn().getId()) {
					return "CANNOT ATTACK YOUR OWN TERRITORIES";
				}
				else {
					ArrayList<Territory> result = territoryAtk.attack(territoryDef, nbAttack , nbDefense);
					if(result != null) {
					}
					else {
						return "Territory not adjacent";
					}
					this.lastFightTerritories = null;
					game.setGetAttacked(null);
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
	
	public Game closeFightStep(Game game) {
		if(game.getPhase() == GamePhase.ATTACK) {
			game.setPhase(GamePhase.MOVEFORTIFY);
			System.out.println( "PHASE CHANGED TO => "+ game.getPhase());
		}
		else {
			System.out.println( "CANNOT CLOSE THE PHASE, PHASE =>"+game.getPhase().toString());
		}
		return game;
	}
	
	public Game closeMoveFortifyStep(Game game) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY) {
			game.round();
			System.out.println( "NEXT PLAYER");
		}
		else {
			System.out.println( "CANNOT CLOSE THE PHASE, PHASE =>"+game.getPhase().toString());
		}
		return game;
	}
	
	public GamePhase gamePhase(Game game) {
		return game.getPhase();
	}
	
	public Player playerTurn(Game game) {
		return game.getPlayerTurn();
	}
	
	public String restoreGame(String name, Game game) {
		for(Player player : game.getPlayerList()) {
			if(player.getName().equals(name)) {
				return player.getSessionId();
			}
		}
		return null;
	}
}
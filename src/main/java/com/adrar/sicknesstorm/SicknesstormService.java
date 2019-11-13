package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SicknesstormService{
	@Autowired
	PlayerRepository playerRepo;
	
	@Autowired
	TerritoryRepository territoryRepo;
	
	private List<Integer> lastFightTerritories = new ArrayList<>();
	
	
	
	public List<Integer> getLastFightTerritories() {
		return lastFightTerritories;
	}

	public void setLastFightTerritories(List<Integer> lastFightTerritories) {
		this.lastFightTerritories = lastFightTerritories;
	}

	public Player getAplayer(Integer id){
		return playerRepo.findById(id).get();
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
	
	public Set<Territory> getTerritoryByPlayerId(Integer playerId){
		return territoryRepo.findTerritoryByPlayerId(playerId);
	}
	
	/**
	 * Initilize the game with a suffle of all territories and attributes territories to each player.
	 * @param game
	 */
	public void initializeGame(Game game) {
		if(game.getPhase() == GamePhase.INITIALIZE) {
			game.initialize(this.getAllPlayers());
			List<Territory> territories = this.getTerritories();
			Collections.shuffle(territories);
			//Algorithm that gives all territory randomly to each player.
			int j = 0;
			int playerIndex = 0;
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
				game.getPlayerList().get(playerIndex).setPlayerTerritories(this.getTerritoryByPlayerId(player.getId()));
				playerIndex += 1;
			}
			//End of the algorithm
			game.round();
		}
		else {
			System.out.println("GAME ALREADY INITIALIZED");
		}
	}
	
	public void addPawn(int idTerritory , int pawn, Game game ) {
			Territory territory = this.getAterritory(idTerritory);
			
			/* condition that check if the id of the playerTurn is equal to the territory
			that he want to add pawn*/
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
				   	}
				}
				else {
					System.out.println("NOT ENOUGH PAWNS");
				}
			   	
			}
			else {
				System.out.println("NOT YOUR TERRITORY => " + game.getPlayerTurn().getName()+ " => TERRITORY OWNER" + territory.getPlayer().getName());
			}
		
	}
	
	/**
	 * Move the pawns from the territoryA to the territoryB if it respect the conditions.
	 * @param idTerritorya
	 * @param idTerritoryb
	 * @param pawn
	 * @param game
	 * @return
	 */
	public String movePawns (int idTerritorya , int idTerritoryb , int pawn, Game game) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY) {
			// Condition that check if the territoryA AND territoryB owner is equal to the player's turn
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritorya).getPlayer().getId() && game.getPlayerTurn().getId() == this.getAterritory(idTerritoryb).getPlayer().getId()) {
					if(this.getAterritory(idTerritorya).moveFortify(this.getAterritory(idTerritoryb), pawn) == true) {
					   	territoryRepo.save(this.getAterritory(idTerritorya));
						territoryRepo.save(this.getAterritory(idTerritoryb));  
						return "YOUR PAWNS HAVE BEEN WELL MOVED FROM "+this.getAterritory(idTerritorya).getName()+" TO "+this.getAterritory(idTerritoryb).getName()+"";
					}
					else {
						return "TERRITORY NOT NEIGHBOR ";
					}
			}
			else {
				return "NOT YOUR TERRITORY";
			}
		}
		else {
			return "MOVE PAWNS NOT AVAILABLE";
		}
	}
	
	/**
	 * Method that take the attacks informations and store it in an array called "lastFightTerritories"
	 * also set the GetAttacked from the Class Game to the name of the owner of the territory that get attacked.
	 * @param fightRequestInfos
	 * @param game
	 * @return game
	 */
	public Game buildFight (List<Integer> fightRequestInfos, Game game) {
		if(game.getPhase() == GamePhase.ATTACK && this.isAdjacent(fightRequestInfos.get(0), fightRequestInfos.get(1)) == true) {
			game.setGetAttacked(this.getAterritory(fightRequestInfos.get(1)).getPlayer().getName());
			this.lastFightTerritories = fightRequestInfos;
		}
		else {
			
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
		else {
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
		if(game.getPhase() == GamePhase.ATTACK) {
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritoryAtk).getPlayer().getId()) {
				if(this.getAterritory(idTerritoryDef).getPlayer().getId() == game.getPlayerTurn().getId()) {
					return "CANNOT ATTACK YOUR OWN TERRITORIES";
				}
				else {
					ArrayList<Territory> result = this.getAterritory(idTerritoryAtk).attack(this.getAterritory(idTerritoryDef), nbAttack , nbDefense);
					if(result != null) {
						territoryRepo.save(result.get(0));
						territoryRepo.save(result.get(1));
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
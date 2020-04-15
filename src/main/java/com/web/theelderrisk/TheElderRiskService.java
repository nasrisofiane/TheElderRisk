package com.web.theelderrisk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TheElderRiskService{
	
	@Autowired
	TerritoryRepository territoryRepo;

	@Autowired
	PlayerRepository playerRepo;

	public Game createTimerTask(Game game, TimerTask newTask){
		game.createTimer(newTask);
		return game;
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
	
	public List<Player> addPlayer(Player player, Game game, String sessionId) {
		
		for(Player playerInList : game.getPlayerList()) {
			if(playerInList.getSessionId() == this.getPlayerSession()) {
				return game.getPlayerList();
			}
		}

		if(game.getPlayerList().size() <= 3){
			player.setSessionId(sessionId);
			game.addPlayer(player);
			this.savePlayer(player);
		}

		return game.getPlayerList();
	}
	
	public void savePlayer(Player player){
		this.playerRepo.save(player);
	}

	public boolean deleteDisconnectedPlayer(Game game, String sessionId){
		
		for(Player player : game.getPlayerList()){
			if(player.getSessionId() == sessionId){
				game.deletePlayer(player);
				game.initializePlayersTerritories();
				return game.endGame();
			}
		}
			
		return false;
	}

	/**
	 * Initilize the game with a suffle of all territories and attributes territories to each player.
	 * @param game
	 */
	public Game initializeGame(Game game) {
		if(!game.isGameHasStarted() && game.getPhase() == GamePhase.INITIALIZE) {

			List<Territory> territories = this.getTerritories();
			this.initializeAdjacentTerritories(territories);

			Collections.shuffle(territories);

			game.setTerritories(territories);
			game.initializePlayersTerritories();
			game.preparationPhase();
			game.setPhase(GamePhase.PREPARE);
			game.round();
			game.setGameHasStarted(true);
			return game;
		}
		return null;
	}

	public void initializeAdjacentTerritories(List<Territory> territories){
		for(Territory territory : territories){
			for(ArrayList<Integer> TerritoriesId : this.getTerritoriesAdjacents(territory.getId())){
				for(Integer TerritoryId : TerritoriesId){
					for(Territory territoryR : territories){
						if(territoryR != territory){
							if (TerritoryId.equals(((Integer)territoryR.getId()))){
								territory.addTerritoryAdjacent(territoryR);
							}
						}
					}
				}
			}
		}
	}

	private String getPlayerSession(){
		return SimpAttributesContextHolder.currentAttributes().getSessionId();
	}
	
	public Game addPawn(int idTerritory , int pawn, Game game, TimerTask newTask) {
			Territory territoryAddPawn = null;
			String playerSession = this.getPlayerSession();

			for(Territory territory : game.getTerritories()) {
				if(territory.getId() == idTerritory) {
					if(territory.getPlayer().getSessionId().equals(playerSession)){
						territoryAddPawn = territory;
					}
					break;
				}
			}

			if(territoryAddPawn == null){
				return game;
			}

			if(game.getPhase() == GamePhase.PREPARE){
				if(territoryAddPawn.getPlayer().getPawnsToPlace() > 0 && pawn <= territoryAddPawn.getPlayer().getPawnsToPlace()) {
					territoryAddPawn.getPlayer().setPawnsToPlace(territoryAddPawn.getPlayer().getPawnsToPlace() - pawn);
					territoryAddPawn.setPawn(territoryAddPawn.getPawn() + pawn);
				}
				game.round();
				this.createTimerTask(game, newTask);
			}
			
			/* condition that check if the id of the playerTurn is equal to the territory
			that he want to add pawn*/
			if(game.getPhase() == GamePhase.PLACEPAWN && game.getPlayerTurn().getId() == territoryAddPawn.getPlayer().getId()) {
				if(game.getPlayerTurn().getPawnsToPlace() > 0 && pawn <= game.getPlayerTurn().getPawnsToPlace()) {
					game.getPlayerTurn().setPawnsToPlace(game.getPlayerTurn().getPawnsToPlace() - pawn);
					territoryAddPawn.setPawn(territoryAddPawn.getPawn() + pawn);
				   	if(game.getPlayerTurn().getPawnsToPlace() == 0) {
						   game.setPhase(GamePhase.ATTACK);
				   	}
				}
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
		if(game.getPhase() == GamePhase.MOVEFORTIFY && game.getPlayerTurn().getSessionId().equals(this.getPlayerSession())) {

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
			if(game.getPlayerTurn().getId().equals(territoryA.getPlayer().getId()) && game.getPlayerTurn().getId().equals(territoryB.getPlayer().getId())) {
				territoryA.moveFortify(territoryB, pawn);
			}
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
		
		if(game.getPlayerTurn().getSessionId().equals(this.getPlayerSession())){
			Territory territoryAtk = null;
			Territory territoryDef = null;
			
			for(Territory territory : game.getTerritories()) {
				if(territory.getId() == fightRequestInfos.get(0)) {
					if(territory.getPlayer() == game.getPlayerTurn()){
						territoryAtk = territory;
					}
				}
				if(territory.getId() == fightRequestInfos.get(1)) {
					if(territory.getPlayer() != game.getPlayerTurn()){
						territoryDef = territory;
					}
				}
			}
			
			if(territoryAtk.getPlayer().getId() != territoryDef.getPlayer().getId() && territoryAtk != null && territoryDef != null) {
				if(game.getPhase() == GamePhase.ATTACK && this.isAdjacent(fightRequestInfos.get(0), fightRequestInfos.get(1), game) == true) {
					game.setGetAttacked(territoryDef.getPlayer().getName());
					game.setLastFightTerritories(fightRequestInfos);
				}
			}
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
		Player getAttackedPlayer = null;

		for(Player player : game.getPlayerList()){
			if(game.getGetAttacked().equals(player.getName())){
				getAttackedPlayer = player;
				break;
			}
		}

		if(game.getPhase() == GamePhase.ATTACK && getAttackedPlayer.getSessionId().equals(this.getPlayerSession())) {
			this.startFight(game.getLastFightTerritories().get(0), game.getLastFightTerritories().get(1), game.getLastFightTerritories().get(2), nbDefense, game);
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
					game.setLastFightTerritories(null);
					game.setGetAttacked(null);
					game.endGame();
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
		if(game.getPhase() == GamePhase.ATTACK && game.getPlayerTurn().getSessionId().equals(this.getPlayerSession())) {
			game.setPhase(GamePhase.MOVEFORTIFY);
		}
		return game;
	}
	
	public Game closeMoveFortifyStep(Game game, TimerTask newTask) {
		if(game.getPhase() == GamePhase.MOVEFORTIFY && game.getPlayerTurn().getSessionId().equals(this.getPlayerSession())) {
			game.round();
			this.createTimerTask(game, newTask);
		}
		return game;
	}

	public void playerEliminator(Game game){
		for(Player player : game.getPlayerList()){
			int numberOfOwnedTerritories = 0;

			for(Territory territory : game.getTerritories()){
				if(territory.getId() == player.getId()){ 
					numberOfOwnedTerritories += 1;
				}
			}

			if(numberOfOwnedTerritories == 0){
				game.deletePlayer(player);
			}
			
		}
	}
	
	public GamePhase gamePhase(Game game) {
		return game.getPhase();
	}
	
	public Player playerTurn(Game game) {
		return game.getPlayerTurn();
	}

	public ArrayList<ArrayList<Integer>> getTerritoriesAdjacents(int id){
		return this.territoryRepo.getAdjacentsTerritories(id);
	}
}
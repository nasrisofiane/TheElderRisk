package com.web.theelderrisk;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Game {
	
	private int id; 

	private List<Player> playerList = new ArrayList();
	
	private int turnPlayerNumber = 0;
	
	@JsonIgnoreProperties("playerTerritories")
	private Player playerTurn;
	
	private GamePhase phase = GamePhase.INITIALIZE;
	
	private List<Territory> territories = new ArrayList<>();
	
	private String getAttacked;
	
	private boolean gameHasStarted = false;
	
	private List<Integer> lastFightTerritories = new ArrayList<>();
	
	private long endingTurnTime = 0;

	@JsonIgnore
	private Date endingTurnDate;

	@JsonIgnore
	private LocalDateTime endingLocalTime;

	@JsonIgnore
	private Timer timer;

	@JsonIgnore
	private boolean firstRound = true;

	@JsonIgnore
	private ZoneId zoneId = ZoneId.systemDefault();

	public int getTurnPlayerNumber() {
		return turnPlayerNumber;
	}

	public void setTurnPlayerNumber(int turnPlayerNumber) {
		this.turnPlayerNumber = turnPlayerNumber;
	}

	public void preparationPhase(){
		for (Player player : this.playerList) {
			player.setPawnsToPlace(150);
		}
	}
	
	/**
	 * set all boolean all phases of a round to false and return the player that have to play
	 * @return the player that have to play
	 */
	public Player round() {
		if(this.phase == GamePhase.PREPARE){
			this.playerTurn = this.playerList.get(this.turnPlayerNumber);
			boolean playerAreReady = true;

			for(Player player : this.playerList){
				if(player.getPawnsToPlace() > 0){
					playerAreReady = false;
				}
			}

			if(playerAreReady == true){
				this.phase = GamePhase.ATTACK;
				this.turnPlayerNumber += 1;
				this.cleanTimer();
			}
			
		}
		else{
			this.playerTurn = this.playerList.get(this.turnPlayerNumber);

			if(this.firstRound == false){
				this.phase = GamePhase.PLACEPAWN;
				this.playerTurn.setPawnsToPlace((int) Math.ceil(playerTurn.getPlayerTerritories().size() / 3));
			}
			else{
				this.phase = GamePhase.ATTACK;
			}

			this.turnPlayerNumber += 1;
	
			if(turnPlayerNumber == this.playerList.size()) {
				this.turnPlayerNumber = 0;
				this.firstRound = false;
			}
			this.cleanTimer();
		}
		return playerTurn;
	}

	public void executeOnTask(){
		this.dispatchPlayersPawns();
		this.round();
		this.cleanTimer();
	}

	public void createTimer(TimerTask newTask){
		
		if(this.endingTurnTime == 0){
			
			Timer timer = new Timer();
			LocalDateTime endingDate = LocalDateTime.now().plusMinutes(2);
			Date endingDuration = Date.from(endingDate.atZone(this.zoneId).toInstant());

			this.endingLocalTime = endingDate;
			this.endingTurnDate = endingDuration;
			timer.schedule(newTask, this.getEndingTurnDate());
			this.timer = timer;
		}

		this.getEndingTurnTime();

	}

	public void dispatchPlayersPawns(){
		for(Player player : this.playerList){
			while(player.getPawnsToPlace() > 0){
				for(Territory territory : player.getPlayerTerritories()){
					if(player.getPawnsToPlace() > 0){
						territory.setPawn(territory.getPawn()+1);
						player.setPawnsToPlace(player.getPawnsToPlace()-1);
					}
					else{
						break;
					}
				}
			}
		}
	}
	
	public String getGetAttacked() {
		return getAttacked;
	}

	public void setGetAttacked(String getAttacked) {
		this.getAttacked = getAttacked;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}

	public Player getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(Player playerTurn) {
		this.playerTurn = playerTurn;
	}

	public int getId() {
		return id;
	}

	public GamePhase getPhase() {
		return phase;
	}

	public void setPhase(GamePhase phase) {
		this.phase = phase;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}

	public void addPlayer(Player player) {
		playerList.add(player);
	}

	public  void deletePlayer(Player player) {
		for(Territory territory : player.getPlayerTerritories()){
			territory.setPlayer(null);
		}
		playerList.remove(player);	
		
	}
	
	public boolean endGame() {
		if(this.playerList.size() == 1){
			this.cleanTimer();
			this.phase = GamePhase.END;
			return true;
		}
		
		return false;
	}

	private void cleanTimer(){
		this.timer.cancel();
		this.timer.purge();
		this.endingTurnTime = 0;
	}

	public void initializePlayersTerritories(){

		int playerIndex = 0;

		for(Territory territory : this.getTerritories()) {

			if(territory.getPlayer() == null){
				this.getPlayerList().get(playerIndex).addTerritory(territory);
				territory.setPlayer(this.getPlayerList().get(playerIndex));
				territory.setPawn(1);
	
				playerIndex += 1;
				
				if(playerIndex >= this.getPlayerList().size()) {
					playerIndex = 0;
				}
			}

		}
			
	}

	public boolean isGameHasStarted() {
		return gameHasStarted;
	}

	public void setGameHasStarted(boolean gameHasStarted) {
		this.gameHasStarted = gameHasStarted;
	}

	public List<Integer> getLastFightTerritories() {
		return lastFightTerritories;
	}

	public void setLastFightTerritories(List<Integer> lastFightTerritories) {
		this.lastFightTerritories = lastFightTerritories;
	}

	public long getEndingTurnTime() {
		if(this.endingTurnTime >= 0 && this.endingLocalTime != null){
			LocalDateTime now = LocalDateTime.now();
			this.endingTurnTime = now.until(this.endingLocalTime, SECONDS);
		}
		return endingTurnTime;
	}

	public void setEndingTurnTime(long endingTurnTime) {
		this.endingTurnTime = endingTurnTime;
	}

	public Date getEndingTurnDate() {
		return endingTurnDate;
	}

	public void setEndingTurnDate(Date endingTurnDate) {
		this.endingTurnDate = endingTurnDate;
	}

	public LocalDateTime getEndingLocalTime() {
		return endingLocalTime;
	}

	public void setEndingLocalTime(LocalDateTime endingLocalTime) {
		this.endingLocalTime = endingLocalTime;
	}

	public boolean isFirstRound() {
		return firstRound;
	}

	public void setFirstRound(boolean firstRound) {
		this.firstRound = firstRound;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}



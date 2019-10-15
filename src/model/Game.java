package model;

import java.util.ArrayList;

public class Game {
	private int id ; 
	private ArrayList<Integer> playerList = new ArrayList();	
	
	public  void round() {
		
	}
	public  void addPlayer(Player player) {
		playerList.add(player.getId());
	}
	public  void deletePlayer(Player player) {
		playerList.remove(player.getId());	
	}
	public  void startGame() {
		
	}
	public  void endGame() {
		
	}
}



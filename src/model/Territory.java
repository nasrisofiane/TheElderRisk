package model;

import java.util.ArrayList;

public class Territory {
	private int id;
	private String name;
	private int continentId;
	private ArrayList<Territory> territoryAdjacent;
	private int pawn;
	private int playerId;
	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getContinentId() {
		return continentId;
	}
	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}
	public ArrayList<Territory> getTerritoryAdjacent() {
		return territoryAdjacent;
	}
	public void setTerritoryAdjacent(ArrayList<Territory> territoryAdjacent) {
		this.territoryAdjacent = territoryAdjacent;
	}
	public int getPawn() {
		return pawn;
	}
	public void setPawn(int pawn) {
		this.pawn = pawn;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	/**
	 * 
	 * @param territory
	 * @param nbAttack
	 * @param nbDefense
	 * perform an attack to the Territory passed in parameters,
	 * check if the Territory is a neighbor before the attack.
	 * if there's only 1 pawn on the Territory attacked, perform a shifumi.
	 * if the attacker get all pawn from the attacked territory, call conquerTerritory(Territory, Nbpawns)
	 */
	public void attack(Territory territory, int nbAttack, int nbDefense) {
		if(territoryAdjacent.contains(territory.id)) {
			Fight fight = new Fight();
			ArrayList<Integer> resultats = fight.startFight(nbAttack, nbDefense);
			System.out.println("Territoire voisin");
			if(territory.getPawn() == 1) {
				if(this.shifumi() == true) {
					territory.setPawn(territory.getPawn() - resultats.get(0));
					this.pawn = this.pawn - resultats.get(1);
				}
			}
			else {
				territory.setPawn(territory.getPawn() - resultats.get(0));
				this.pawn = this.pawn - resultats.get(1);
			}
			if(territory.getPawn() <= 0) {
				this.conquerTerritory(territory, resultats.get(0));
			}
		}
		else {
			System.out.println("Territoire non voisin");		
			}
	}
	
	public void conquerTerritory(){ // take the control of a territory.
		
	}
	
	public void moveFortify(Territory territory, Territory targetTerritory, int nbPawnDeplace ) { //move pawn from a territory to another.
		
		 
		if(territoryAdjacent.contains(targetTerritory.id) && (territory.pawn > 1)) {
	
	
		territory.setPawn(territory.getPawn()-nbPawnDeplace);
		targetTerritory.setPawn(targetTerritory.getPawn()+ nbPawnDeplace);
			
			
		
		}
	}
	public boolean shifumi() {
		int min=1;
		int max=3;
			
		
			
			int attacker = min + (int)(Math.random() * ((max - min) + 1));
			
			int defender = min + (int)(Math.random() * ((max - min) + 1));
			
		
			
		if( attacker == 1) { 	System.out.println("pierre");}
		
		if( attacker == 2) { 	System.out.println("feuille");}
		
		if( attacker == 3) { 	System.out.println("ciseaux");}
		
		
			
		if( defender == 1) { 	System.out.println("pierre");}
		
		if( defender == 2) { 	System.out.println("feuille");}
		
		if( defender == 3) { 	System.out.println("ciseaux");}
		
			
		if( attacker==1 &&defender==1) {System.out.println("Recommencer");
		this.shifumi();
		}
		
		if( attacker==1 &&defender==2) {System.out.println("la feuille gagne");
		return false;
		}
		
		if( attacker==1 &&defender==3) {System.out.println("la pierre gagne");
		return true;
		}
		
		if( attacker==2 &&defender==1) {System.out.println("la feuille gagne");
		return true;
		}
		
		if( attacker==2 &&defender==2) {System.out.println("Recommencer");
		this.shifumi();
		}
		
		if( attacker==2 &&defender==3) {System.out.println("le ciseaux gagne");
		return false;
		}
		if( attacker==3 &&defender==1) {System.out.println("la pierre gagne");
		return false;
		}
		if(attacker==3 &&defender==2) {System.out.println("le ciseaux gagne");
		return true;
		}
		if( attacker==3 &&defender==3) {System.out.println("Recommencer");
		this.shifumi();
		}
		
		return false;	
			
			
			
		}

	
}

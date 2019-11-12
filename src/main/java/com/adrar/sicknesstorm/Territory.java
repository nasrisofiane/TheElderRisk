package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name ="territories")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Territory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private int continentId;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "territories_adjacent", 
			joinColumns = @JoinColumn(name = "territory_a"), 
			inverseJoinColumns = @JoinColumn(name = "territory_b"))
	private Set<Territory> territoriesA;
	
	public Set<Territory> getTerritoriesA() {
		return territoriesA;
	}

	public void setTerritoriesA(Set<Territory> territoriesA) {
		this.territoriesA = territoriesA;
	}

	@JsonIgnoreProperties("territoryAdjacent")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "territories_adjacent", 
			joinColumns = @JoinColumn(name = "territory_b"), 
			inverseJoinColumns = @JoinColumn(name = "territory_a"))
	private Set<Territory> territoryAdjacent;
	
	private int pawn;

	@JsonIgnoreProperties("playerTerritories")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "player_id")
	private Player player;

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
	
	public Set<Territory> getTerritoryAdjacent() {
		for(Territory territory : this.territoriesA) {
			this.territoryAdjacent.add(territory);
		}
		return territoryAdjacent;
	}
	
	public void setTerritoryAdjacent(Set<Territory> territoryAdjacent) {
		this.territoryAdjacent = territoryAdjacent;
	}
	
	public int getPawn() {
		return pawn;
	}
	public void setPawn(int pawn) {
		this.pawn = pawn;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void addTerritoryAdjacent(Territory territory) {
		this.territoryAdjacent.add(territory);
	}

	public void deleteTerritoryAdjacent(Territory territory) {
		this.territoryAdjacent.remove(territory);
	}
	
	public boolean isAdjacent(Territory territory) {
		getTerritoryAdjacent();
		if(this.territoryAdjacent.contains(territory)) {
			return true;
		}
		else {
			return false;
		}
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
	public ArrayList<Territory> attack(Territory territory, int nbAttack, int nbDefense) {
		ArrayList<ArrayList<Integer>> resultats = new ArrayList<>();
		if(nbAttack > territory.getPawn() || nbAttack > 3) {//secure the number of dice thrown for the attacker
			nbAttack = 1;
		}
		if(nbDefense > this.pawn || nbDefense > 2) { //secure the number of dice thrown for the defender
			nbDefense = 1;
		}
		if(isAdjacent(territory)) { // if the territory attacked is a neighbor of this territory so the fight can be executed
			Fight fight = new Fight();
			
			System.out.println("Territoire voisin");
			if(territory.getPawn() == 1) {
				if(this.shifumi() == true) {
					resultats = fight.startFight(nbAttack, nbDefense);
					territory.setPawn(territory.getPawn() - resultats.get(0).get(0));
					this.pawn = this.pawn - resultats.get(0).get(1);
				}
			}
			else {
				resultats = fight.startFight(nbAttack, nbDefense);
				territory.setPawn(territory.getPawn() - resultats.get(0).get(0));
				this.pawn = this.pawn - resultats.get(0).get(1);
				System.out.println(territory.getId()+" -> NB PIONS -> " + territory.getPawn());
			}
			if(territory.getPawn() <= 0) {
				this.conquerTerritory(territory, resultats.get(0).get(0));
			}
			if(this.pawn <= 0) {
				territory.conquerTerritory(this, resultats.get(0).get(1));
			}
			ArrayList<Territory> territoriesChanged = new ArrayList<Territory>();
			territoriesChanged.add(this);
			territoriesChanged.add(territory);
			return territoriesChanged;
		}//END OF : if the territory attacked is a neighbor of this territory so the fight can be executed
		else {
			System.out.println("Territoire non voisin");
			return null;
			}
	}
	
	public void conquerTerritory(Territory territory, int nbPawns){ // take the control of a territory.
		territory.setPlayer(this.player);
		territory.setPawn(nbPawns);
	}
	
	public boolean checkBeforeMoveFortify(Player player, Territory territoryA, Set<Territory> chemins){ 
		System.out.println(this.id);
		this.getTerritoryAdjacent();
		if(territoryA.getPlayer() != null) {
			if(territoryA.getPlayer().getId() == player.getId()) {
				for(Territory territory : this.territoryAdjacent) {
					if(territory.getPlayer() != null) {
						if(!chemins.contains(territory) && territory.getPlayer().getId() == player.getId()) {
							chemins.add(this);
							if(territory.getId() == territoryA.getId()) {
								return true;
							}
							else {
								return territory.checkBeforeMoveFortify(player, territoryA, chemins);
							}
						}
					}
					
					
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		return false;
	} 
	
	public boolean moveFortify(Territory targetTerritory, int nbPawnDeplace) { //move pawn from a territory to another.
		 Set<Territory> chemins = new HashSet<Territory>();
		if(this.checkBeforeMoveFortify(this.player, targetTerritory, chemins) == true) {
			if(this.pawn - nbPawnDeplace >= 1) {
				targetTerritory.setPawn(targetTerritory.getPawn() + nbPawnDeplace);
				this.pawn -= nbPawnDeplace;
			}
			else {
				System.out.println("Not enough pawns");
			}
		}
		return this.checkBeforeMoveFortify(this.player, targetTerritory, chemins);
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
			return this.shifumi();
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
			return this.shifumi();
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
			return this.shifumi();
		}
		
			return false;
		}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		//System.out.println("equals "+this.id+" "+this.name+" "+((Territory)obj).getId()+" "+((Territory)obj).getName());
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Territory other = (Territory) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Territory [id=" + id + ", name=" + name + ", continentId=" + continentId + ", territoryAdjacent="
				 + ", pawn=" + pawn + ", player=" + player + "]";
	}
	
	
	
	
}

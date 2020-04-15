package com.web.theelderrisk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name ="territories")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Territory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private int continentId;
	
	@JsonIgnoreProperties("territoryAdjacent")
	@Transient
	private Set<Territory> territoryAdjacent = new HashSet<Territory>();
	
	@Transient
	private int pawn;

	@JsonIgnoreProperties("playerTerritories")
	@Transient
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
	
	public boolean isAdjacent(Territory territory) {
		
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

		Territory territoire = territory;
		ArrayList<ArrayList<Integer>> resultats = new ArrayList<>();

		//Check the dices input, if it do not respect the number it set the dice to 1.
		if(nbAttack >= this.pawn || nbAttack > 3) {//secure the number of dice thrown from the attacker
			nbAttack = 1;
		}
		if(nbDefense > territory.getPawn()  || nbDefense > 2) { //secure the number of dice thrown from the defender
			nbDefense = 1;
		}
		if(isAdjacent(territory)) { // if the territory attacked is a neighbor of this territory so the fight can be executed
			Fight fight = new Fight();
			resultats = fight.startFight(nbAttack, nbDefense);

			territory.setPawn(territory.getPawn() - resultats.get(0).get(0));
			this.pawn = this.pawn - resultats.get(0).get(1);

			this.player.setLastFightResults(resultats);
			territory.getPlayer().setLastFightResults(resultats);

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
			return null;
		}
	}
	
	public void conquerTerritory(Territory territory, int nbPawns){ // take the control of a territory.
		territory.getPlayer().deleteTerritory(territory);
		this.player.addTerritory(territory);
		territory.setPlayer(this.player);
		territory.setPawn(nbPawns);
	}
	
	/**
	 * Method that check if the territory that you want to move your pawn to is adjcent and your territory.
	 * @param player
	 * @param territoryA
	 * @param chemins
	 * @return
	 */
	public boolean checkBeforeMoveFortify(Player player, Territory territoryA){ 

		if(territoryA.getPlayer().getId().equals(player.getId())) {
			for(Territory territory : this.territoryAdjacent) {
				if(territory.getPlayer().getId().equals(player.getId())) {
					if(territory.getId() == territoryA.getId()){
						return true;
					}
				}
			}
		}
		return false;
	} 
	
	public boolean moveFortify(Territory targetTerritory, int nbPawnDeplace) { //move pawn from a territory to another.
		
		//Condition that check the pawn can be move.
		if(this.checkBeforeMoveFortify(this.player, targetTerritory) == true) {
			if(this.pawn - nbPawnDeplace >= 1) {
				targetTerritory.setPawn(targetTerritory.getPawn() + nbPawnDeplace);
				this.pawn -= nbPawnDeplace;
				return true;
			}
			else {
			}
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

package model;

import java.util.ArrayList;

public class Fight {
	private int id;
	private int nbPawnMaxAtk;
	private int nbPawnMaxDef;
	
	public int getNbPawnMaxAtk() {
		return nbPawnMaxAtk;
	}

	public void setNbPawnMaxAtk(int nbPawnMaxAtk) {
		this.nbPawnMaxAtk = nbPawnMaxAtk;
	}

	public int getNbPawnMaxDef() {
		return nbPawnMaxDef;
	}

	public void setNbPawnMaxDef(int nbPawnMaxDef) {
		this.nbPawnMaxDef = nbPawnMaxDef;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @param nbAtk
	 * @param nbDef
	 * @return an ArrayList with the results of the dices, the first index is the number of dices won
	 * by the attacker and the second index is for the defender.
	 */
	public ArrayList<Integer> startFight(int nbAtk , int nbDef) {
		if(nbAtk < this.nbPawnMaxAtk && nbDef <= this.nbPawnMaxDef) {
			return this.checkWinner(this.dice(nbAtk), this.dice(nbDef))
		}
		else {
			return null;
		}
	}
	
	public void checkWinner(int atk , int defd) {
		
	}
	
	public void dice(int nbDices) {
		
	}

}

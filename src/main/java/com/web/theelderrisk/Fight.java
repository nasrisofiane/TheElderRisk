package com.web.theelderrisk;

import java.util.ArrayList;
import java.util.Collections;

public class Fight {
	private int id;
	private int nbPawnMaxAtk = 3;
	private int nbPawnMaxDef = 2;
	
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
	public ArrayList<ArrayList<Integer>> startFight(int nbAtk , int nbDef) {
		
		if(nbAtk <= this.nbPawnMaxAtk && nbDef <= this.nbPawnMaxDef) {
			
		}
		ArrayList<ArrayList<Integer>> resultat = this.checkWinner(this.dice(nbAtk), this.dice(nbDef));
		return resultat;
	}
	
	/**
	 * 
	 * @param atk
	 * @param defd
	 * @return a two dimensional array, at first index there's an array with the results of dices wons the second array
	 * the dices numbers for the attackers and the last index is for the defender dices numbers
	 */
	
	public ArrayList<ArrayList<Integer>> checkWinner(ArrayList<Integer>atk , ArrayList<Integer> defd) {

		ArrayList<ArrayList<Integer>> resultat = new ArrayList(3);

		resultat.add(new ArrayList());
		resultat.get(0).add(0);
		resultat.get(0).add(0);
		resultat.add(new ArrayList());
		resultat.add(new ArrayList());

		int shorterArray;

		if ( atk.size()>defd.size()) {
			shorterArray=defd.size();
		}
		else {
			shorterArray=atk.size();
		}
		
		for(int i = 0; i < atk.size(); i++) {
			resultat.get(1).add(atk.get(i));
		}
		for(int i = 0; i < defd.size(); i++) {
			resultat.get(2).add(defd.get(i));
		}
		
		for (int i= 0 ; i<shorterArray;i++) {
			if  (Collections.max(atk) > Collections.max(defd)) {
				resultat.get(0).set(0, resultat.get(0).get(0)+1);	
			}
			else {
				resultat.get(0).set(1, resultat.get(0).get(1)+1);	
			}
			atk.remove(atk.indexOf(Collections.max(atk)));
			defd.remove(defd.indexOf(Collections.max(defd)));
		}
		return resultat;
	}


	/**
	 * @param nb = nombredeDesLancé
	 * @return
	 */
	public  ArrayList<Integer> dice(int nb) {
		 int Min = 1;
		 int Max = 6;

		ArrayList<Integer> dice = new ArrayList();

		for(int i=0;i<nb;i++) {

			dice.add(Min + (int)(Math.random() * ((Max - Min) + 1)));	
			Collections.sort(dice, Collections.reverseOrder());
		}
	return dice;
}

}
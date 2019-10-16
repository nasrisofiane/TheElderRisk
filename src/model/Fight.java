package model;

import java.util.ArrayList;
import java.util.Collection;
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
	public ArrayList<Integer> startFight(int nbAtk , int nbDef) {
		if(nbAtk <= this.nbPawnMaxAtk && nbDef <= this.nbPawnMaxDef) {
			ArrayList<Integer> resultat = this.checkWinner(this.dice(nbAtk), this.dice(nbDef));
			System.out.println(resultat);
			return resultat;
		}
		else {
			System.out.println("ici");
			return null;
		}
	}
	
	public ArrayList<Integer> checkWinner(ArrayList<Integer>atk , ArrayList<Integer> defd) {
		ArrayList<Integer> resultat = new ArrayList(2);
		resultat.add(0);
		resultat.add(0);
		
		int shorterArray;
		if ( atk.size()>defd.size()) {
			shorterArray=defd.size();
		}
		else {
			shorterArray=atk.size();
		}
		
		for (int i= 0 ; i<shorterArray;i++) {
			if  (Collections.max(atk) > Collections.max(defd)) {
				resultat.set(0, resultat.get(0)+1);	
			}
			else {
				resultat.set(1, resultat.get(1)+1);	
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
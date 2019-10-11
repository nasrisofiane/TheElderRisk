package risque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Joueur {
	private int Min = 1;
	private int Max = 6;
	private int choixd;
	public String name;
	ArrayList<Integer> list = new ArrayList();
	
public ArrayList<Integer> lancer(int nb) {

	for(int i=0;i<nb;i++) {
	list.add(Min + (int)(Math.random() * ((Max - Min) + 1)));	
	Collections.sort(list, Collections.reverseOrder());
	}
	System.out.println(list);
return list;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

}
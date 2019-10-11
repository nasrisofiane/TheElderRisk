package risque;
import java.util.Collections;
import java.util.Scanner;






public class Risque {



public static void main(String[] args) {
/*Lancer lancer = new Lancer();*/

Joueur j1 = new Joueur();

j1.lancer(3);
j1.setName("abdel");


Joueur j2 = new Joueur();
j2.lancer(2);
j2.setName("clément");	 
	 
if ( j1.list.get(0).intValue()>j2.list.get(0).intValue()&& j1.list.get(1).intValue()>j2.list.get(1).intValue()) {
	System.out.println("dès 1 : "+" "+j1.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j1.name + (" ") + ("win!"));


}
else if (j1.list.get(0).intValue()>j2.list.get(0).intValue() && j1.list.get(1).intValue()<j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j1.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j2.name + (" ") + ("win!"));
}

else if (j1.list.get(0).intValue()==j2.list.get(0).intValue() && j1.list.get(1).intValue()<j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j2.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j1.name + (" ") + ("win!"));
}
else if (j1.list.get(0).intValue()<j2.list.get(0).intValue() && j1.list.get(1).intValue()<j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j2.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j2.name + (" ") + ("win!"));
}
else if (j1.list.get(0).intValue()<j2.list.get(0).intValue() && j1.list.get(1).intValue()>j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j2.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j1.name + (" ") + ("win!"));
}
else if (j1.list.get(0).intValue()==j2.list.get(0).intValue() && j1.list.get(1).intValue()>j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j2.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j1.name + (" ") + ("win!"));
}
else if (j1.list.get(0).intValue()>j2.list.get(0).intValue() && j1.list.get(1).intValue()==j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j1.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j2.name + (" ") + ("win!"));
}
else if (j1.list.get(0).intValue()<j2.list.get(0).intValue() && j1.list.get(1).intValue()==j2.list.get(1).intValue()) {
	
	System.out.println("dès 1 : "+" "+j2.name + (" ") + ("win!"));
	System.out.println("dès 2 : "+" "+j2.name + (" ") + ("win!"));
}
   /*f (army==3) {
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
	
}
else if (army==2) {
	
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
	
}
else if (army==1) {
	lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
	System.out.println(lancer);
}
	}
	  
	static class J2{
		
		
		public static void main(String[] args) {

		    if (army==3) {
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
			
		}
		else if (army==2) {
			
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
			
		}
		else if (army==1) {
			lancer =  Min + (int)(Math.random() * ((Max - Min) + 1));
			System.out.println(lancer);
		}
			}*/
		
		
	}
	    
}
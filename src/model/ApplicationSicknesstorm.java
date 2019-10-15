package model;

public class ApplicationSicknesstorm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player player = new Player();
		
		Territory territory1 = new Territory();
		Territory territory2 = new Territory(); 
		Territory territory3 = new Territory(); 

		territory2.setPawn(1);
		territory1.setPawn(15);
		territory3.setPawn(15);
		territory3.setId(9);
		territory2.setId(4);
		territory1.setId(2);
		territory1.setPlayer(player);
		System.out.println("Player 1 Territories -> "+player.getPlayerTerritories());
		System.out.println("NOMBRE DE PIONS -> "+territory2.getPawn());
		territory1.addTerritoryAdjacent(territory2);
		territory1.attack(territory2, 12, 2);
		System.out.println("Player 1 Territories -> "+player.getPlayerTerritories());
		System.out.println("NOMBRE DE PIONS -> "+territory2.getPawn());
	}

}

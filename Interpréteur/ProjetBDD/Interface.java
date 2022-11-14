import java.util.Scanner;


public class Interface {
	Scanner interacteur;
	DriverJDBC jdbc;
	
	public Interface() {
		this.interacteur = new Scanner(System.in);
		this.jdbc = new DriverJDBC();
		this.jdbc.connexion();
	}
	
	public void connexion() {
		
		System.out.println("Bienvenu à GrenobleEAT ! \n");
		System.out.println("As-tu un compte ? \n -- -- -- \n");
		
		System.out.println("1) J'ai un compte utilisateur");
		System.out.println("2) Créer un compte");
		System.out.println("3) Quitter l'application");
		
	}
	
	public void quit() {
		jdbc.fermeture();
	}
	
	public void accueil() {
		
		System.out.println("Bienvenu à nouveau ! \n");
		System.out.println("Que souhaites-tu faire ? \n -- -- -- \n");
		
		System.out.println("1) Restaurants disponibles");
		System.out.println("2) Réaliser une commande");
		System.out.println("3) Éliminer données personnelles d'une personne (Droit à l'oubli)");
		
		
	}
	
	public void creerCompte() { // TODO 
		System.out.println("Tu vas te regaler ! \n");
		System.out.println("C'est quoi ton nom ! \n");
	}
}

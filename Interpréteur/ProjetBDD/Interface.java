import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Interface {
	Scanner interacteur;
	DriverJDBC jdbc;
	
	public Interface() {
		this.interacteur = new Scanner(System.in);
		this.jdbc = new DriverJDBC();
		this.jdbc.connexion();
	}
	
	public int numberOfAnswers(ResultSet rs) {
		int out = 0;
		try {
			while (rs.next()) out++;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
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
	
	public void identification() {
		try {
			int nombreReponses = 0;
			System.out.println("\n -- -- -- \n Quel est ton adresse mail ? \n");
			String mail = interacteur.nextLine();
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UId FROM UTILISATEURS WHERE UMail = \'" + mail + "\'");
			int userId;
			while (rs.next()) {
				nombreReponses++;
				if (nombreReponses > 1) {
					identificationAvecId();
					return;
				}
				userId = rs.getInt("UId");
			if (nombreReponses == 0) {
				System.out.println("\n Aïe... Nous n'avons aucun utilisateur avec cet adresse mail... Veillez indiquer une adresse mail existance ? \n");
				identification();
				return;
			}
			System.out.println("\n Quel est ton mot de passe ? \n");
			String MdP = interacteur.nextLine();
			stmt = jdbc.connection.createStatement();
			rs = stmt.executeQuery("SELECT UId FROM UTILISATEURS WHERE UId = \'" + String.valueOf(userId) + "\' AND Mdp = \'" + MdP + "\'");
				
				
			}
		} catch (SQLException e) { 
			
			e.printStackTrace();
			
		}
	}
	
	public void identificationAvecId() {
		try {
			System.out.println("\n Aïe... Ce mail est utilisé par plus d'une personne... Quel est ton numéro d'utilisateur ? \n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void creerCompte() { // TODO 
		System.out.println("Tu vas te regaler ! \n");
		System.out.println("C'est quoi ton nom ! \n");
	}

	public void connexion() {
		
		System.out.println("Bienvenu à GrenobleEAT ! \n");
		System.out.println("As-tu un compte ? \n -- -- -- \n");
		
		System.out.println("1) J'ai un compte utilisateur");
		System.out.println("2) Créer un compte");
		System.out.println("3) Quitter l'application \n");
		
		System.out.println("Tapez le numéro de la réponse que vous souhaitez : \n");
		
		String reponse = interacteur.nextLine();
		
		switch (reponse) {
			case "1":
				this.accueil();
				break;
			case "2":
				this.creerCompte();
				break;
			case "3":
				this.quit();
				break;
			default:
				System.out.println("Vous n'avez pas indiqué une réponse valide. \n -- -- -- \n");
				connexion();
				break;
		}
		
	}
}
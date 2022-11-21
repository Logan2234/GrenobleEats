import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Interface {
	Scanner interacteur;
	DriverJDBC jdbc;
	Utilisateur user;
	
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
	
	public void droitOublie() {
		System.out.println("Est-tu sûr(e) de vouloir effacer tes données, " + user.getPrenom() + "?");
		System.out.println("Les données ne pourront pas être récupérées. \n");
		
		System.out.println("1) Oui, je souhaite tout effacer");
		System.out.println("2) Quitter l'application \n");

		System.out.println("Tapez le numéro de la réponse que vous souhaitez : \n");
		
		String reponse = interacteur.nextLine();
		
		switch (reponse) {
			case "1":
				this.accueil(); // TODO
				break;
			case "2":
				this.quit();
				break;
			default:
				System.out.println("Vous n'avez pas indiqué une réponse valide. \n -- -- -- \n");
				accueil();
				break;
		}
	}
	
	public void effacementDonnees() {
		try {
			Statement stmt = jdbc.connection.createStatement();
			stmt.executeUpdate("UPDATE UTILISATEURS SET UMail = \'\', UNom = \\'\\', Prenom = \\'\\', "
					+ "UAdresse = \\'\\', Mdp =  = \\'\\' WHERE UId = " + String.valueOf(user.getIdentifiant()));
			System.out.println("Les données personnelles ont été effacées. Nous procédons à fermer la session. \n -- -- -- \n");
			connexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void accueil() {
		
		System.out.println("Bienvenu à nouveau, " + user.getPrenom() + "! \n");
		System.out.println("Que souhaites-tu faire ? \n -- -- -- \n");
		
		System.out.println("1) Restaurants disponibles");
		System.out.println("2) Réaliser une commande");
		System.out.println("3) Éliminer mes données personnelles (Droit à l'oubli)");
		System.out.println("4) Changer d'utilisateur");
		System.out.println("5) Quitter l'application \n");

		System.out.println("Tapez le numéro de la réponse que vous souhaitez : \n");
		
		String reponse = interacteur.nextLine();
		
		switch (reponse) {
			case "1":
				this.accueil(); // TODO
				break;
			case "2":
				this.creerCompte(); // TODO
				break;
			case "3":
				this.quit(); // TODO
				break;
			case "4":
				identification();
				break;
			case "5":
				quit();
				break;
			default:
				System.out.println("Vous n'avez pas indiqué une réponse valide. \n -- -- -- \n");
				accueil();
				break;
		}
		
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
					System.out.println("\n Aïe... Il y a plus d'un utilisateur avec le même courriel... Pas normal... Indique une autre adresse. \n");
					identification();
					return;
				}
				userId = rs.getInt("UId");
			if (nombreReponses == 0) {
				System.out.println("\n Aïe... Nous n'avons aucun utilisateur avec cet adresse mail... Veillez indiquer une adresse mail existance ? \n");
				identification();
				return;
			}
			verifierMDP(userId);
			}
		} catch (SQLException e) { 
			
			e.printStackTrace();
			
		}
	}
	
	public void verifierMDP(int userId) {
		try {
			System.out.println("\n Quel est ton mot de passe ? \n");
			String MdP = interacteur.nextLine();
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UId FROM UTILISATEURS WHERE UId = " + String.valueOf(userId) + " AND Mdp = \'" + MdP + "\'");
			int nombreReponses = 0;
			while (rs.next()) {
				nombreReponses++;
			}
			if (nombreReponses == 0) {
				System.out.println("Mot de passe incorrect. Veillez introduire le bon mot de passe. \n");
				verifierMDP(userId);
				return;
			} else {
				stmt = jdbc.connection.createStatement();
				rs = stmt.executeQuery("SELECT UMail, UNom, Prenom, UAdresse FROM UTILISATEURS WHERE UId = \'" + String.valueOf(userId) + "\'");
				rs.next();
				this.user = new Utilisateur(userId, rs.getString("UMail"), rs.getString("UNom"), rs.getString("Prenom"), MdP, rs.getString("Adresse"));
				accueil();
				return;
			}
			
		} catch (SQLException e) { 
			e.printStackTrace();	
		}
	}

	public void laisserEvaluation(){
		try {
			System.out.println("\n Entrez le numéro de la commande à sélectionner \n");
			System.out.println("Numéro, IdCommande, Date, Heure, Prix");

			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Cid, CDate, CHeure, CPrix FROM COMMANDES WHERE UId = \'" + user.getIdentifiant() + "\'"); // requête à completer maybe
			int cId;
			while (rs.next()){
				cId = Integer.valueOf(rs.getString("Cid"));
				System.out.println(cId + " " + rs.getString("CDate") + " " + rs.getString("CHeure") + " " + rs.getString("CPrix"));

			}
			System.out.println();
			String userInput = interacteur.nextLine();
			stmt = jdbc.connection.createStatement();
			rs = stmt.executeQuery("SELECT Cid, CDate, CHeure, CPrix FROM COMMANDES WHERE CId = \'" + userInput + "\' ");
			int nbLoop = 0;
			while (rs.next()){
				nbLoop++;
				if(nbLoop>1){
					System.out.println("PROBLEME ! Il y a plusieurs commandes de même identifiant");
					return;
				}
			}
			if (nbLoop==0){
				System.out.println("Le numéro que vous avez rentré ne correspond à aucune commande, veuillez réessayer.");
				laisserEvaluation();
			}
			else{
				int idCommande = Integer.valueOf(userInput);
				System.out.println("\n Entrez une note (entier entre 1 et 5 compris)\n");
				userInput = interacteur.nextLine();
			}

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
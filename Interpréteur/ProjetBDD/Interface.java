import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


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
				this.effacementDonnees();
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
	
	public void categoriesMeres(){
		try {
			Set<String> catMeres = new HashSet<String>();
			
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CatNom FROM UTILISATEURS WHERE CatNomMere = \'_\'");
			while (rs.next()) {
				catMeres.add(rs.getString("CatNom"));
			}
			System.out.println("\n -- -- -- \n");
			
			while (true) {
				System.out.println(" Quelle catégorie te donne faim ? \n");
				for (String cat : catMeres) {
					System.out.println("- " + cat);
				}
				System.out.println("\n Écris la catégorie que tu préféres : \n");
				String reponse = interacteur.nextLine();
				
				if (catMeres.contains(reponse)) {
					selectSousCat(reponse);
					break;
				} 
				System.out.println("\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves \n");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void selectSousCat(String catMere) { 
		try {
			Set<String> sousCats = new HashSet<String>();
		
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CatNom FROM UTILISATEURS WHERE CatNomMere = \'" + catMere + "\'");
			while (rs.next()) {
				sousCats.add(rs.getString("CatNom"));
			}
			System.out.println("\n -- -- -- \n");
			System.out.println(" T'as fait un trop bon choix ! Miam ! \n"); 
			
			if (sousCats.size() == 0) {
				restoParCat(catMere);
			} else {
				while (true) {
					System.out.println(" Quelle sous-catégorie t'aimerais gouter ? \n"); 
					System.out.println("- " + catMere + " en général (tapez 1)"); 
					for (String cat : sousCats) {
						System.out.println("- " + cat);
					}
					System.out.println("\n Écris la catégorie que tu préféres : \n");
					String reponse = interacteur.nextLine();
					if (reponse == "1") {
						restoParCat(catMere);
						break;
					}
					if (sousCats.contains(reponse)) {
						selectSousCat(reponse);
						break;
					} 
					System.out.println("\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves \n");
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void restoParCat(String categorie) {
		try {
			System.out.println("\n -- -- -- \n");
			System.out.println(" Tu vas te régaler ! \n");
			
			ArrayList<String> restos = new ArrayList<String>();
			
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT RNom FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CATEGORIESRESTAURANT = \'" + categorie + "\'");
			while (rs.next()) {
				restos.add(rs.getString("RNom"));
			}
			if (restos.size() == 0) {
				System.out.println(" Oups... Il y a aucun restorant avec cette catégorie... Veillez sélectionner une autre catégorie. \n");
				categoriesMeres();
				return;
			}
			System.out.println("\n -- -- -- \n");   
			System.out.println(" Dans la catégorie de " + categorie + ", voici les restaurants disponibles : \n"); 
			
			for (int i = 0; i < restos.size(); i += 10) {
				for (int j = i; j < i + 10 && j < restos.size(); j++) {
					System.out.println(String.valueOf((i%10) + 1) + ") " + restos.get(j)); 
				}
				if (i + 10 <= restos.size()) {
					System.out.println("11) Voir plus de restaurants");
				}
				while (true) {
					System.out.println("\n Écris le numéro de ta réponse souhaitée : \n");
					String reponse = interacteur.nextLine();
					if (reponse == "11" && i + 10 <= restos.size()) break;
					if (reponse == "1" && i <= restos.size()) {
						commanderResto(restos.get(i));
						return;
					}
					if (reponse == "2" && i + 1 <= restos.size()) {
						commanderResto(restos.get(i + 1));
						return;
					}
					if (reponse == "3" && i + 2 <= restos.size()) {
						commanderResto(restos.get(i + 2));
						return;
					}
					if (reponse == "4" && i + 3 <= restos.size()) {
						commanderResto(restos.get(i + 3));
						return;
					}
					if (reponse == "5" && i + 4 <= restos.size()) {
						commanderResto(restos.get(i + 4));
						return;
					}
					if (reponse == "6" && i + 5 <= restos.size()) {
						commanderResto(restos.get(i + 5));
						return;
					}
					if (reponse == "7" && i + 6 <= restos.size()) {
						commanderResto(restos.get(i + 6));
						return;
					}
					if (reponse == "8" && i + 7 <= restos.size()) {
						commanderResto(restos.get(i + 7));
						return;
					}
					if (reponse == "9" && i + 8 <= restos.size()) {
						commanderResto(restos.get(i + 8));
						return;
					}
					if (reponse == "10" && i + 9 <= restos.size()) {
						commanderResto(restos.get(i + 9));
						return;
					}
					
					System.out.println("\n Aïe, votre réponse n'est pas valide ou il n'y a plus de restorants. Choisis une réponse valide. \n");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void commanderResto(String resto) {
		//TODO
	}
	
	public void effacementDonnees() {
		try {
			Statement stmt = jdbc.connection.createStatement();
			stmt.executeUpdate("UPDATE UTILISATEURS SET UMail = \'\', UNom = \'\', Prenom = \'\', "
					+ "UAdresse = \'\', Mdp = \'\' WHERE U_Id = " + String.valueOf(user.getIdentifiant()));
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
				this.droitOublie(); 
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
			ResultSet rs = stmt.executeQuery("SELECT U_Id FROM UTILISATEURS WHERE UMail = \'" + mail + "\'");
			int userId = 0;
			while (rs.next()) {
				nombreReponses++;
				if (nombreReponses > 1) {
					System.out.println("\n Aïe... Il y a plus d'un utilisateur avec le même courriel... Pas normal... Indique une autre adresse. \n");
					identification();
					return;
				}
				userId = rs.getInt("U_Id");
			}
			if (nombreReponses == 0) {
				System.out.println("\n Aïe... Nous n'avons aucun utilisateur avec cet adresse mail... Nous revenons vers l'accueil. \n");
				connexion();
				return;
			}
			verifierMDP(userId);
			
		} catch (SQLException e) { 
			
			e.printStackTrace();
			
		}
	}
	
	public void verifierMDP(int userId) {
		try {
			System.out.println("\n Quel est ton mot de passe ? \n");
			String MdP = interacteur.nextLine();
			if (MdP.equals("quit")) {
				connexion();
				return;
			}
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT U_Id FROM UTILISATEURS WHERE U_Id = " + String.valueOf(userId) + " AND Mdp = \'" + MdP + "\'");
			int nombreReponses = 0;
			while (rs.next()) {
				nombreReponses++;
			}
			if (nombreReponses == 0) {
				System.out.println("Mot de passe incorrect. Veillez introduire le bon mot de passe. Tappez quit pour revenir au début.\n");
				verifierMDP(userId);
				return;
			} else {
				stmt = jdbc.connection.createStatement();
				rs = stmt.executeQuery("SELECT UMail, UNom, Prenom, UAdresse FROM UTILISATEURS WHERE U_Id = " + String.valueOf(userId));
				rs.next();
				this.user = new Utilisateur(userId, rs.getString("UMail"), rs.getString("UNom"), rs.getString("Prenom"), MdP, rs.getString("UAdresse"));
				System.out.println("\n -- -- -- \n");
				accueil();
				return;
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
		
		System.out.println("\n -- -- -- \nBienvenu à GrenobleEAT ! \n");
		System.out.println("As-tu un compte ? \n -- -- -- \n");
		
		System.out.println("1) J'ai un compte utilisateur");
		System.out.println("2) Créer un compte");
		System.out.println("3) Quitter l'application \n");
		
		System.out.println("Tapez le numéro de la réponse que vous souhaitez : \n");
		
		String reponse = interacteur.nextLine();
		
		switch (reponse) {
			case "1":
				this.identification();
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Interface {
	Scanner interacteur;
	DriverJDBC jdbc;
	Utilisateur user;
	Commande commande;
	static final DateFormat heure = new SimpleDateFormat("hh:mm:ss a");
	static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

	public Interface() {
		this.interacteur = new Scanner(System.in);
		this.jdbc = new DriverJDBC();
		this.jdbc.connexion();
		this.commande = new Commande();
	}

	private void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		System.out.println();
	}

	public void quit() {
		jdbc.fermeture();
	}

	public int numberOfAnswers(ResultSet rs) {
		int out = 0;
		try {
			while (rs.next())
				out++;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public void accueil() {
		clearConsole();

		commande.uid = user.getIdentifiant();

		System.out.println("Bienvenue " + user.getPrenom() + "! \n");
		System.out.println("Que souhaites-tu faire ? \n -- -- -- \n");

		System.out.println("1) Parcourir les restaurants");
		System.out.println("2) Passer une commande");
		System.out.println("3) Supprimer mes données personnelles (Droit à l'oubli)");
		System.out.println("4) Changer d'utilisateur");
		System.out.println("5) Quitter l'application \n");

		System.out.print("Tapez le numéro de la réponse que vous souhaitez : ");

		switch (interacteur.nextLine()) {
			case "1":
				// TODO
				break;
			case "2":
				this.categoriesRecommandes();
				break;
			case "3":
				droitOubli();
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

	public void categoriesRecommandes() {
		clearConsole();

		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("BLABLABLABLA"); // TODO COMMANDE QUI DONNE 3 DERNIÈRES CAT COMMANDÉES
			Set<String> recommandes = new HashSet<String>();
			while (rs.next()) {
				recommandes.add(rs.getString("CatNom")); // TODO vérifier que c'est bien CatNom que tu recup
			}
			System.out.println("\n -- -- -- \n");

			while (true) {
				System.out.println(" Quelle catégorie tu veux commander ? \n");
				for (String cat : recommandes) {
					System.out.println("- " + cat);
				}
				System.out.println("- Autre");
				System.out.println("\n Écris la catégorie que tu préféres : \n");
				String reponse = interacteur.nextLine();

				if (reponse == "Autre") {
					categoriesMeres();
					return;
				}

				if (recommandes.contains(reponse)) {
					selectSousCat(reponse);
					break;
				}
				System.out.println(
						"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie qui te donne la dalle. \n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void categoriesMeres() {
		clearConsole();
		
		try {
			Set<String> catMeres = new HashSet<String>();

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle
			ResultSet rs = stmt.executeQuery("SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = \'_\'");
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
				System.out.println(
						"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves \n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void selectSousCat(String catMere) {
		clearConsole();

		try {
			Set<String> sousCats = new HashSet<String>();

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle
			ResultSet rs = stmt
					.executeQuery("SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = \'" + catMere + "\'");
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
					System.out.println(
							"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void validationCommande() {
		clearConsole();

		while (true) {
			System.out.println(" \nTu veux manger sur place ou on te livre ?\n");
			System.out.println("1) Livraison");
			System.out.println("2) Sur place");
			System.out.println("3) Annuler commande\n");
			String reponse = interacteur.nextLine();
			switch (reponse) {
			case "1":
				commande.type = "L"; // En livraison
				commandeLivraison();
				return;
			case "2":
				commande.type = "P"; // Sur place
				commandeSurPlace();
				return;
			case "3":
				accueil();
			default:
				System.out.println("\nAïe... La valeur introduite est incorrecte... \n");
				break;
			}
		}
	}
	
	public void commandeLivraison() {
		clearConsole();

		String reponse;
		while (true) {
			System.out.println(" \nIndiques l'adresse de livraison : \n");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				commande.adresse = reponse;
				break;
			}
		}
		
		System.out.println(" \nTu veux laisser un message pour le livreur ? : \n");
		reponse = interacteur.nextLine();
		if (reponse != "") commande.texte = reponse;
		
		while (true) {
			System.out.println(" \nÀ quelle heure tu veux commander ? (heure entre 0 et 23) : \n");
			reponse = interacteur.nextLine();
			try {
				int heure = Integer.valueOf(reponse);
				if (0 <= heure && heure <= 23) {
					commande.heure = heure;
					break;
				}
				else System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 23\n");
			} catch (Exception e) {
				System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 23\n");
			}
		}
		
		while (true) {
			System.out.println(" \nÀ quelle minute tu veux commander ? (minute entre 0 et 59) : \n");
			reponse = interacteur.nextLine();
			try {
				int minute = Integer.valueOf(reponse);
				if (0 <= minute && minute <= 59) {
					commande.minute = minute;
					break;
				}
				else System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 59\n");
			} catch (Exception e) {
				System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 59\n");
			}
		}
		
		envoyerCommande();
		
	}
	
	public void commandeSurPlace() {
		clearConsole();

		String reponse;
		int capaciteMax = 0; // TODO requete SQL pour avoir la capacite max
		while (true) {
			System.out.println(" \nIndiques le nombre de personnes dans la réservation : \n");
			reponse = interacteur.nextLine();
			try {
				int nbPlaces = Integer.valueOf(reponse);
				if (0 < nbPlaces && nbPlaces <= capaciteMax) {
					commande.nbPlaces = nbPlaces;
					break;
				}
				else System.out.println("\nOups... T'as pas indiqué un chiffre entre 1 et la capacité maximale du restaurant...\n");
			} catch (Exception e) {
				System.out.println("\nOups... T'as pas indiqué un chiffre entre 1 et la capacité maximale du restaurant...\n");
			}
		}
		
		
		while (true) {
			System.out.println(" \nÀ quelle heure tu veux réserver ? (heure entre 0 et 23) : \n");
			reponse = interacteur.nextLine();
			try {
				int heure = Integer.valueOf(reponse);
				if (0 <= heure && heure <= 23) {
					commande.heure = heure;
					break;
				}
				else System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 23\n");
			} catch (Exception e) {
				System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 23\n");
			}
		}
		
		while (true) {
			System.out.println(" \nÀ quelle minute tu veux réserver ? (minute entre 0 et 59) : \n");
			reponse = interacteur.nextLine();
			try {
				int minute = Integer.valueOf(reponse);
				if (0 <= minute && minute <= 59) {
					commande.minute = minute;
					break;
				}
				else System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 59\n");
			} catch (Exception e) {
				System.out.println("\nOups... T'as pas indiqué un chiffre entre 0 et 59\n");
			}
		}
		
		envoyerCommande();
	}
	
	public void envoyerCommande() {
		// TODO mettre dans la BDD
	}
	
	public void listeRestos() {
		try {
			System.out.println("\n -- -- -- \n");
			System.out.println(" Tu vas te régaler ! \n");
			
			ArrayList<String> restos = new ArrayList<String>();
			
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery(""); // TODO requete pour sortir dans l'ordre décroissant des éval et ordre alpha 
			
			while (rs.next()) {
				restos.add(rs.getString("RNom"));
			}
			
			System.out.println("Voici la liste des restaurants dans l'ordre décroissant de notes : \n"); 
			
			for (int i = 0; i < restos.size(); i += 10) {
				for (int j = i; j < i + 10 && j < restos.size(); j++) {
					System.out.println(String.valueOf((i%10) + 1) + ") " + restos.get(j)); 
				}
				if (i + 10 <= restos.size()) {
					System.out.println("11) Voir plus de restaurants");
				}
				System.out.println("\n0) Retour à l'accueil \n");
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
					if (reponse == "0") {
						accueil();
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
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT PNom, PDescription, PPrix FROM PLATS WHERE PRestaurant = \'" + resto + "\'"); 
			
			commande.rNom = resto;
			
			ArrayList<Map<String,String>> plats = new ArrayList<Map<String,String>>();
			
			while (rs.next()) {
				
				Map<String,String> plat = new HashMap<String,String>();
				plat.put("Nom", rs.getString("PNom"));
				plat.put("Des", rs.getString("PDescription"));
				plat.put("Prix", String.valueOf(rs.getFloat("PPrix"))); 
				plats.add(plat);
			}
			if (plats.size() == 0) {
				System.out.println(" Oups... Il y a aucun plat disponible... Veillez sélectionner un autre restorant. \n");
				commande.commencerCommande();
				listeRestos();
				return;
			}
			System.out.println("\n -- -- -- \n");   
			System.out.println(" Voici les plats disponibles au " + resto + " : \n"); 
			
			for (int i = 0; i < plats.size(); i += 10) {
				for (int j = i; j < i + 10 && j < plats.size(); j++) { 
					System.out.println(String.valueOf((i%10) + 1) + ") " + plats.get(j).get("Nom") + " (" + plats.get(j).get("Prix") + " €) - " + plats.get(j).get("Des")); 
				}
				if (i + 10 <= plats.size()) {
					System.out.println("11) Voir plus de plats");
				}
				System.out.println("\n0) Retour aux restaurants et annuler commande en cours \n");
				while (true) {
					System.out.println("\n Écris le numéro de ta réponse souhaitée : \n");
					String reponse = interacteur.nextLine();
					if (reponse == "11" && i + 10 <= plats.size()) break;
					if (reponse == "1" && i <= plats.size()) {
						ajouterACommande(plats.get(i), resto);
						return;
					}
					if (reponse == "2" && i + 1 <= plats.size()) {
						ajouterACommande(plats.get(i + 1), resto);
						return;
					}
					if (reponse == "3" && i + 2 <= plats.size()) {
						ajouterACommande(plats.get(i + 2), resto);
						return;
					}
					if (reponse == "4" && i + 3 <= plats.size()) {
						ajouterACommande(plats.get(i + 3), resto);
						return;
					}
					if (reponse == "5" && i + 4 <= plats.size()) {
						ajouterACommande(plats.get(i + 4), resto);
						return;
					}
					if (reponse == "6" && i + 5 <= plats.size()) {
						ajouterACommande(plats.get(i + 5), resto);
						return;
					}
					if (reponse == "7" && i + 6 <= plats.size()) {
						ajouterACommande(plats.get(i + 6), resto);
						return;
					}
					if (reponse == "8" && i + 7 <= plats.size()) {
						ajouterACommande(plats.get(i + 7), resto);
						return;
					}
					if (reponse == "9" && i + 8 <= plats.size()) {
						ajouterACommande(plats.get(i + 8), resto);
						return;
					}
					if (reponse == "10" && i + 9 <= plats.size()) {
						ajouterACommande(plats.get(i + 9), resto);
						return;
					}
					if (reponse == "0") {
						commande.commencerCommande();
						listeRestos();
						return;
					}
					
					System.out.println("\n Aïe, votre réponse n'est pas valide ou il n'y a plus de restorants. Choisis une réponse valide. \n");
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void ajouterACommande(Map<String,String> plat, String resto) {
		int quantite = 1;
		String reponse;
		while (true) {
			System.out.println(" \nT'aimerais commander combien de plats comme celui-là ? Si tu n'indiques pas de quantité, on ajoutera que 1 à la commande.\n");
			reponse = interacteur.nextLine();
			try {
				if (reponse == "") break;
				quantite = Integer.valueOf(reponse);
				if (quantite > 10) System.out.println("\n Ha ouais... " + reponse + "? Ça fait beaucoup, mais on sait que t'es un petit gourmand ;P \n");
				if (quantite > 0) break;
			} catch (Exception e){
				System.out.println(" \nIntroduis une vraie valeur numérique entière positive strictement, stp. \n");
			}
			System.out.println(" \nIntroduis une valeur numérique entière positive strictement, stp. \n");
		}
		if (commande.plats.containsKey(plat.get("Nom"))) {
			int nbPlats = commande.plats.get(plat.get("Nom")) + quantite;
			commande.plats.put(plat.get("Nom"), nbPlats);
		} else commande.plats.put(plat.get("Nom"), quantite);
		
		commande.prix += Float.valueOf(plat.get("Prix")) * quantite;
		
		while (true) {
			System.out.println("\n Tu veux continuer ta commande ou ça sera assez ? \n");
			System.out.println("1) Je souhaite continuer ma commande");
			System.out.println("2) Ça sera bon, je veux passer à la suite");
			System.out.println("\nIndiques le chiffre correspondant à ton choix désiré : \n");
			reponse = interacteur.nextLine();
			switch (reponse) {
			case "1":
				commanderResto(resto);
				return;
			case "2":
				validationCommande();
				return;
			default:
				System.out.println("\nAïe... La valeur introduite est incorrecte... \n");
				break;
			}
		}
	}

	public void restoParCat(String categorie) {
		try {
			System.out.println("\n -- -- -- \n");
			System.out.println(" Tu vas te régaler ! \n");

			ArrayList<String> restos = new ArrayList<String>();

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle
			ResultSet rs = stmt.executeQuery(
					"SELECT DISTINCT RNom FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CATEGORIESRESTAURANT.CatNom = \'"
							+ categorie + "\'");
			while (rs.next()) {
				restos.add(rs.getString("RNom"));
			}
			if (restos.size() == 0) {
				System.out.println(
						" Oups... Il y a aucun restorant avec cette catégorie... Veillez sélectionner une autre catégorie. \n");
				categoriesMeres();
				return;
			}
			System.out.println("\n -- -- -- \n");
			System.out.println(" Dans la catégorie de " + categorie + ", voici les restaurants disponibles : \n");

			for (int i = 0; i < restos.size(); i += 10) {
				for (int j = i; j < i + 10 && j < restos.size(); j++) {
					System.out.println(String.valueOf((i % 10) + 1) + ") " + restos.get(j));
				}
				if (i + 10 <= restos.size()) {
					System.out.println("11) Voir plus de restaurants");
				}
				System.out.println("\n0) Retour aux catégories \n");
				while (true) {
					System.out.println("\n Écris le numéro de ta réponse souhaitée : \n");
					String reponse = interacteur.nextLine();
					if (reponse == "11" && i + 10 <= restos.size())
						break;
					if (reponse == "1" && i <= restos.size()) {
						commanderResto(restos.get(i), categorie);
						return;
					}
					if (reponse == "2" && i + 1 <= restos.size()) {
						commanderResto(restos.get(i + 1), categorie);
						return;
					}
					if (reponse == "3" && i + 2 <= restos.size()) {
						commanderResto(restos.get(i + 2), categorie);
						return;
					}
					if (reponse == "4" && i + 3 <= restos.size()) {
						commanderResto(restos.get(i + 3), categorie);
						return;
					}
					if (reponse == "5" && i + 4 <= restos.size()) {
						commanderResto(restos.get(i + 4), categorie);
						return;
					}
					if (reponse == "6" && i + 5 <= restos.size()) {
						commanderResto(restos.get(i + 5), categorie);
						return;
					}
					if (reponse == "7" && i + 6 <= restos.size()) {
						commanderResto(restos.get(i + 6), categorie);
						return;
					}
					if (reponse == "8" && i + 7 <= restos.size()) {
						commanderResto(restos.get(i + 7), categorie);
						return;
					}
					if (reponse == "9" && i + 8 <= restos.size()) {
						commanderResto(restos.get(i + 8), categorie);
						return;
					}
					if (reponse == "10" && i + 9 <= restos.size()) {
						commanderResto(restos.get(i + 9), categorie);
						return;
					}
					if (reponse == "0") {
						categoriesRecommandes();
						return;
					}

					System.out.println(
							"\n Aïe, votre réponse n'est pas valide ou il n'y a plus de restorants. Choisis une réponse valide. \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void commanderResto(String resto, String categorie) {
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT PNom, PDescription, PPrix FROM PLATS WHERE PRestaurant = \'" + resto + "\'");

			ArrayList<Map<String, String>> plats = new ArrayList<Map<String, String>>();

			while (rs.next()) {

				Map<String, String> plat = new HashMap<String, String>();
				plat.put("Nom", rs.getString("PNom"));
				plat.put("Des", rs.getString("PDescription"));
				plat.put("Prix", "PPrix");
				plats.add(plat);
			}
			if (plats.size() == 0) {
				System.out.println(
						" Oups... Il y a aucun plat disponible... Veillez sélectionner un autre restaurant. \n");
				restoParCat(categorie);
				return;
			}
			System.out.println("\n -- -- -- \n");
			System.out.println(" Voici les plats disponibles au " + resto + " : \n");

			for (int i = 0; i < plats.size(); i += 10) {
				for (int j = i; j < i + 10 && j < plats.size(); j++) {
					System.out.println(String.valueOf((i % 10) + 1) + ") " + plats.get(j).get("Nom") + " ("
							+ plats.get(j).get("Prix") + " €) - " + plats.get(j).get("Des"));
				}
				if (i + 10 <= plats.size()) {
					System.out.println("11) Voir plus de restaurants");
				}
				System.out.println("\n0) Retour aux restorants \n");
				while (true) {
					System.out.println("\n Écris le numéro de ta réponse souhaitée : \n");
					String reponse = interacteur.nextLine();
					if (reponse == "11" && i + 10 <= plats.size())
						break;
					if (reponse == "1" && i <= plats.size()) {
						ajouterACommande(plats.get(i), resto, categorie);
						return;
					}
					if (reponse == "2" && i + 1 <= plats.size()) {
						ajouterACommande(plats.get(i + 1), resto, categorie);
						return;
					}
					if (reponse == "3" && i + 2 <= plats.size()) {
						ajouterACommande(plats.get(i + 2), resto, categorie);
						return;
					}
					if (reponse == "4" && i + 3 <= plats.size()) {
						ajouterACommande(plats.get(i + 3), resto, categorie);
						return;
					}
					if (reponse == "5" && i + 4 <= plats.size()) {
						ajouterACommande(plats.get(i + 4), resto, categorie);
						return;
					}
					if (reponse == "6" && i + 5 <= plats.size()) {
						ajouterACommande(plats.get(i + 5), resto, categorie);
						return;
					}
					if (reponse == "7" && i + 6 <= plats.size()) {
						ajouterACommande(plats.get(i + 6), resto, categorie);
						return;
					}
					if (reponse == "8" && i + 7 <= plats.size()) {
						ajouterACommande(plats.get(i + 7), resto, categorie);
						return;
					}
					if (reponse == "9" && i + 8 <= plats.size()) {
						ajouterACommande(plats.get(i + 8), resto, categorie);
						return;
					}
					if (reponse == "10" && i + 9 <= plats.size()) {
						ajouterACommande(plats.get(i + 9), resto, categorie);
						return;
					}
					if (reponse == "0") {
						restoParCat(categorie);
						;
						return;
					}
					System.out.println(
							"\n Aïe, votre réponse n'est pas valide ou il n'y a plus de restorants. Choisis une réponse valide. \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouterACommande(Map<String,String> plat, String resto, String categorie) {
		int quantite = 1;
		String reponse;
		while (true) {
			System.out.println(" \nT'aimerais commander combien de plats comme celui-là ? Si tu n'indiques pas de quantité, on ajoutera que 1 à la commande.\n");
			reponse = interacteur.nextLine();
			try {
				if (reponse == "") break;
				quantite = Integer.valueOf(reponse);
				if (quantite > 10) System.out.println("\n Ha ouais... " + reponse + "? Ça fait beaucoup, mais on sait que t'es un petit gourmand ;P \n");
				if (quantite > 0) break;
			} catch (Exception e){
				System.out.println(" \nIntroduis une vraie valeur numérique entière positive strictement, stp. \n");
			}
			System.out.println(" \nIntroduis une valeur numérique entière positive strictement, stp. \n");
		}
		if (commande.plats.containsKey(plat.get("Nom"))) {
			int nbPlats = commande.plats.get(plat.get("Nom")) + quantite;
			commande.plats.put(plat.get("Nom"), nbPlats);
		} else commande.plats.put(plat.get("Nom"), quantite);
		
		commande.prix += Float.valueOf(plat.get("Prix")) * quantite;
		
		while (true) {
			System.out.println("\n Tu veux continuer ta commande ou ça sera assez ? \n");
			System.out.println("1) Je souhaite continuer ma commande");
			System.out.println("2) Ça sera bon, je veux passer à la suite");
			System.out.println("\nIndiques le chiffre correspondant à ton choix désiré : \n");
			reponse = interacteur.nextLine();
			switch (reponse) {
			case "1":
				commanderResto(resto, categorie);
				return;
			case "2":
				validationCommande();
				return;
			default:
				System.out.println("\nAïe... La valeur introduite est incorrecte... \n");
				break;
			}
		}
	}

	public void laisserEvaluation() { // TODO à debugger avec la base de donnée en partie complète
		try {
			System.out.println("\n Entrez le numéro de la commande à sélectionner \n");
			System.out.println("Numéro, IdCommande, Date, Heure, Prix");

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle -> prévenir si besoin de la modifier, à retester avec
			// des commandes dans la BD
			ResultSet rs = stmt.executeQuery(
					"SELECT Cid, CDate, CPrix FROM COMMANDES WHERE U_Id = \'" + user.getIdentifiant() + "\'");
			int cId;
			while (rs.next()) {
				System.out.println(rs.getString("Cid") + " " + rs.getString("CDate") + " " + rs.getString("CHeure")
						+ " " + rs.getString("CPrix"));
			}
			System.out.println();
			String userInput = interacteur.nextLine();
			stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle -> prévenir si besoin de la modifier, à retester avec
			// des commandes dans la BD
			rs = stmt.executeQuery("SELECT Cid, CDate, CPrix FROM COMMANDES WHERE Cid = \'" + userInput + "\' ");
			int nbLoop = 0;
			while (rs.next()) {
				nbLoop++;
				if (nbLoop > 1) {
					System.out.println("PROBLEME ! Il y a plusieurs commandes de même identifiant");
					return;
				}
			}
			if (nbLoop == 0) {
				System.out
						.println("Le numéro que vous avez rentré ne correspond à aucune commande, veuillez réessayer.");
				laisserEvaluation();
			} else {
				int idCommande = Integer.valueOf(userInput);
				System.out.println("\n Entrez une note (entier entre 1 et 5 compris)\n");
				int userInputInt = Integer.valueOf(interacteur.nextLine());
				while (!(userInputInt == 1 || userInputInt == 2 || userInputInt == 3 || userInputInt == 4
						|| userInputInt == 5)) {
					System.out.println("Note incorrecte");
					System.out.println("\n Entrez une note (entier entre 1 et 5 compris)\n");
					userInputInt = Integer.valueOf(interacteur.nextLine());
				}
				String note = String.valueOf(userInputInt);
				System.out.println("\n Entrez une un avis (Facultatif)\n");
				userInput = interacteur.nextLine();
				Date newDate = new Date();
				String currDate = date.format(newDate);
				String currHeure = heure.format(newDate);

				stmt = jdbc.connection.createStatement();
				rs = stmt.executeQuery("SELECT Eid FROM EVALUATIONS");
				nbLoop = 0;
				while (rs.next()) {
					nbLoop++;
				}
				String id = String.valueOf(nbLoop);
				// TODO : mettre au format TIMESTAMP
				jdbc.insertValeur("EVALUATIONS", "(" + id + ", " + currDate + ", " + currHeure
						+ "' \'" + userInput + "\', " + note + ", " + idCommande + ")"); // TODO requête à vérifier
			}
			System.out.println("\n Merci d'avoir laissé un avis \n");
			accueil();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void commande(int idRestaurant) {
		try {
			System.out.println("\n Prenons ta commande\n");

			while (true) {
				System.out.println("Entre l'identifiant d'un plat : ");
				Statement stmt = jdbc.connection.createStatement();
				ResultSet rs = stmt.executeQuery(""); // TODO idPlat et nomPlat (avec idRestaurant)
				while (rs.next())
					System.out.println(rs.getString("Pid") + " " + rs.getString("PNom"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void connexion() {
		clearConsole();
		
		System.out.println("\n -- -- -- \nBienvenu à GrenobleEAT ! \n");
		System.out.println("As-tu un compte ? \n -- -- -- \n");
		System.out.println("1) J'ai un compte utilisateur");
		System.out.println("2) Créer un compte");
		System.out.println("3) Quitter l'application \n");
		System.out.print("Tapez le numéro de la réponse que vous souhaitez : ");

		switch (interacteur.nextLine()) {
			case "1":
				identification();
				break;
			case "2":
				creerCompte();
				break;
			case "3":
				quit();
				break;
			default:
				System.out.println("Vous n'avez pas indiqué une réponse valide. \n -- -- -- \n");
				connexion();
				break;
		}
	}

	public void identification() {
		clearConsole();

		System.out.print("Quel est ton adresse mail ? \n ");
		String mail = interacteur.nextLine();

		if (mail.equals("quit")) {
			connexion();
			return;
		}

		System.out.print("\nQuel est ton mot de passe ? \n");
		String mdp = interacteur.nextLine();

		if (mdp.equals("quit")) {
			connexion();
			return;
		}

		try {
			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle (retourne l'UId si un mdp existe)
			ResultSet rs = stmt.executeQuery("SELECT U_id, UNom, UPrenom, UAdresse FROM UTILISATEURS WHERE UMail = \'"
					+ mail + "\' AND UMdp = \'" + mdp + "\'");

			int nombreReponses = 0;
			while (rs.next())
				nombreReponses++;

			if (nombreReponses == 0) {
				System.out.println(
						"\nIdentifiant ou mot de passe incorrect. Veillez vérifier vos entrées. Tappez quit pour revenir au début.\n");
				identification();
				return;
			} else {
				this.user = new Utilisateur(rs.getInt("U_id"), mail, rs.getString("UNom"), rs.getString("UPrenom"), mdp,
						rs.getString("UAdresse"));

				System.out.println("\n -- -- -- \n");
				accueil();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void creerCompte() {
		clearConsole();

		System.out.println("L'aventure commence ! \n");
		
		String reponse;
		while (true) {
			System.out.println("C'est quoi ton nom ? \n");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.nom = reponse;
				break;
			}
		}
		while (true) {
			System.out.println("C'est quoi ton prénom ? \n");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.prenom = reponse;
				break;
			}
		}
		while (true) {
			System.out.println("C'est quoi ton adresse ? \n");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.adresse = reponse;
				break;
			}
		}
		while (true) {
			System.out.println("C'est quoi ton email ? \n");
			reponse = interacteur.nextLine();
			if (reponse != "" && reponse.contains("@")) {
				user.eMail = reponse;
				break;
			}
		}
		while (true) {
			System.out.println("Tu veux quoi comme Mot de Passe ? \n");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.mdp = reponse;
				break;
			}
		}

		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(U_ID) FROM UTILISATEURS");
			rs.next();
			int new_uid = rs.getInt("MAX(U_ID)") + 1;

			jdbc.insertValeur("UTILISATEURS", "(\'" + new_uid + "\', \'" + user.eMail + "\', \'" + user.mdp + "\', \'" + user.nom
					+ "\', \'" + user.prenom + "\', \'" + user.adresse + "\')");

			System.out.println("===== Compte créé! Bon appétit! =====\n");

			accueil();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void droitOubli() {
		clearConsole();

		System.out.print("Es-tu sûr(e) de vouloir effacer tes données, " + user.getPrenom() + "?");
		System.out.println(" /!\\ LES DONNÉES NE POURRONT PAS ÊTRE RÉCUPÉRÉES /!\\ \n");

		System.out.println("1) Oui, je souhaite tout effacer");
		System.out.println("2) Retour\n");

		System.out.print("Tapez le numéro de la réponse que vous souhaitez : ");

		switch (interacteur.nextLine()) {
			case "1":
				try {
					Statement stmt = jdbc.connection.createStatement();
					stmt.executeUpdate(
							"UPDATE UTILISATEURS SET UMail = NULL, UMdp = NULL, UNom = NULL, UPrenom = NULL, UAddresse = NULL WHERE U_Id = "
									+ String.valueOf(user.getIdentifiant()));
					System.out.println("Les données personnelles ont été effacées. Fermeture de la session...\n");
					connexion();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "2":
				accueil();
				break;
			default:
				System.out.println("Vous n'avez pas indiqué une réponse valide. \n -- -- -- \n");
				droitOubli();
				break;
		}
	}
}
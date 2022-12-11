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
		clearConsole();
		this.jdbc.connexion();
		this.commande = new Commande();
		this.user = new Utilisateur();
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

		System.out.println(" -- -- --");
		System.out.println(" Bienvenue " + user.getPrenom() + "! \n");
		System.out.println(" Que souhaites-tu faire ? \n -- -- -- \n");

		System.out.println("1) Parcourir les restaurants");
		System.out.println("2) Passer une commande");
		System.out.println("3) Supprimer mes données personnelles (Droit à l'oubli)");
		System.out.println("4) Laisser évaluation");
		System.out.println("5) Changer d'utilisateur");
		System.out.println("6) Quitter l'application \n");

		System.out.print(" Tape le numéro de la réponse que tu souhaites: ");

		switch (interacteur.nextLine()) {
			case "1":
				clearConsole();
				listeRestos();
				break;
			case "2":
				clearConsole();
				categoriesRecommandes();
				break;
			case "3":
				clearConsole();
				droitOubli();
				break;
			case "4":
				clearConsole();
				laisserEvaluation();
				break;
			case "5":
				clearConsole();
				identification();
				break;
			case "6":
				clearConsole();
				quit();
				break;
			default:
				clearConsole();
				System.out.println(" Tu n'as pas indiqué une réponse valide.\n");
				accueil();
				break;
		}
	}

	public void categoriesRecommandes() {
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT CatNom FROM CATEGORIESRESTAURANT AS cat, RESTAURANTS as res, PLATSCOMMANDE as plcm, COMMANDES as com WHERE cat.RMail = res.RMail AND plcm.PRestaurant = res.RMail AND com.Cid = plcm.Cid AND com.U_id = "
							+ String.valueOf(user.identifiant) + " ORDER by com.CDate DESC LIMIT 5;");
			Set<String> recommandes = new HashSet<String>();
			int nbCommandes = -1;

			while (rs.next()) {
				recommandes.add(rs.getString("CatNom"));
				nbCommandes++;
			}

			if (nbCommandes <= 0) {
				categoriesMeres();
				return;
			}

			while (true) {
				System.out.println(" Quelle catégorie tu veux commander ? \n");
				for (String cat : recommandes) {
					System.out.println("- " + cat);
				}
				System.out.println("- Autre");
				System.out.print("\n Écris la catégorie que tu préfères: ");
				String reponse = interacteur.nextLine();

				switch (reponse) {
					case "Autre":
						clearConsole();
						categoriesMeres();
						break;
					default:
						clearConsole();
						if (recommandes.contains(reponse))
							selectSousCat(reponse);
						else
							System.out.println(
									"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie qui te donne la dalle.");
						break;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void categoriesMeres() {
		try {
			Set<String> catMeres = new HashSet<String>();

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle
			ResultSet rs = stmt.executeQuery("SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = \'_\'");
			while (rs.next())
				catMeres.add(rs.getString("CatNom"));

			while (true) {
				System.out.println(" Quelle catégorie te donne faim ? \n");
				for (String cat : catMeres) {
					System.out.println("- " + cat);
				}
				System.out.print("\n Écris la catégorie que tu préfères: ");
				String reponse = interacteur.nextLine();

				if (catMeres.contains(reponse)) {
					clearConsole();
					selectSousCat(reponse);
					break;
				}

				clearConsole();
				System.out.println(
						"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void selectSousCat(String catMere) {
		try {
			Set<String> sousCats = new HashSet<String>();

			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle
			ResultSet rs = stmt
					.executeQuery("SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = \'" + catMere + "\'");
			while (rs.next())
				sousCats.add(rs.getString("CatNom"));

			if (sousCats.size() == 0) {
				restoParCat(catMere);
			} else {
				while (true) {
					System.out.println(" Quelle sous-catégorie aimerais-tu goûter ? \n");
					if (!catMere.equals("Par pays") && !catMere.equals("Par catégorie"))
						System.out.println("- " + catMere + " en général (tape 1)");

					for (String cat : sousCats)
						System.out.println("- " + cat);

					System.out.print("\n Écris la catégorie que tu préfères: ");

					String reponse = interacteur.nextLine();

					switch (reponse) {
						case "1":
							clearConsole();
							restoParCat(catMere);
							break;
						default:
							clearConsole();
							if (sousCats.contains(reponse))
								selectSousCat(reponse);
							else
								System.out.println(
										"\n Oups... Cette réponse n'est pas valide. Écris (exactement) la catégorie de tes rêves.");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void validationCommande() {
		while (true) {
			System.out.println(" Tu veux manger sur place, emporter ton plat, ou bien te faire livrer ?\n");
			System.out.println("1) Livraison");
			System.out.println("2) Sur place");
			System.out.println("3) À emporter");
			System.out.println("4) Annuler la commande\n");
			System.out.print(" Indique le chiffre correspondant à ton choix désiré: ");
			String reponse = interacteur.nextLine();
			switch (reponse) {
				case "1":
					commande.type = "livraison";
					commandeLivraison();
					return;
				case "2":
					commande.type = "place";
					commandeSurPlace();
					return;
				case "3":
					commande.type = "emporter";
					commandeAEmporter();
					return;
				case "4":
					clearConsole();
					accueil();
				default:
					clearConsole();
					System.out.println(" Aïe... La valeur introduite est incorrecte... \n");
					break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void commandeLivraison() {
		String reponse;
		while (true) {
			System.out.print("\n Indique l'adresse de livraison: ");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				commande.adresse = reponse;
				break;
			}
		}

		System.out.print("\n Tu veux laisser un message pour le livreur (facultatif) ? : ");
		reponse = interacteur.nextLine();
		if (reponse != "")
			commande.texte = reponse;

		while (true) {
			System.out.print("\n À quelle heure tu veux commander ? (heure entre 0 et 23): ");
			reponse = interacteur.nextLine();
			try {
				int heure = Integer.valueOf(reponse);
				if (0 <= heure && heure <= 23) {
					commande.heure = heure;
					break;
				} else
					System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			} catch (Exception e) {
				System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			}
		}

		while (true) {
			System.out.print("\n À quelle minute tu veux commander ? (minute entre 0 et 59): ");
			reponse = interacteur.nextLine();
			try {
				int minute = Integer.valueOf(reponse);
				if (0 <= minute && minute <= 59) {
					commande.minute = minute;
					break;
				} else
					System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			} catch (Exception e) {
				System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			}
		}

		Timestamp tempsActuel = new Timestamp(System.currentTimeMillis());
		Timestamp tempsCommande = new Timestamp(System.currentTimeMillis());
		tempsCommande.setHours(commande.heure);
		tempsCommande.setMinutes(commande.minute);
		commande.timestamp = tempsCommande;

		if (tempsCommande.before(tempsActuel)) {
			commandeLivraison();
			return;
		}

		envoyerCommande();
	}

	@SuppressWarnings("deprecation")
	public void commandeSurPlace() {
		String reponse;

		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Places FROM RESTAURANTS WHERE RMail = \'" + commande.rMail + "\'");
			rs.next();
			int capaciteMax = rs.getInt("Places");

			while (true) {
				System.out.print("\n Indique le nombre de personnes pour la réservation: ");
				reponse = interacteur.nextLine();
				try {
					int nbPlaces = Integer.valueOf(reponse);
					if (0 < nbPlaces && nbPlaces <= capaciteMax) {
						commande.nbPlaces = nbPlaces;
						break;
					} else
						System.out.println(
								"\nOups... Tu n'as pas indiqué un chiffre entre 1 et la capacité maximale du restaurant...\n");
				} catch (Exception e) {
					System.out.println(
							"\nOups... Tu n'as pas indiqué un chiffre entre 1 et la capacité maximale du restaurant...\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		while (true) {
			System.out.print(" \n À quelle heure tu veux réserver ? (heure entre 0 et 23): ");
			reponse = interacteur.nextLine();
			try {
				int heure = Integer.valueOf(reponse);
				if (0 <= heure && heure <= 23) {
					commande.heure = heure;
					break;
				} else
					System.out.println("\nOups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			} catch (Exception e) {
				System.out.println("\nOups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			}
		}

		while (true) {
			System.out.print(" \n À quelle minute tu veux réserver ? (minute entre 0 et 59): ");
			reponse = interacteur.nextLine();
			try {
				int minute = Integer.valueOf(reponse);
				if (0 <= minute && minute <= 59) {
					commande.minute = minute;
					break;
				} else
					System.out.println("\nOups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			} catch (Exception e) {
				System.out.println("\nOups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			}
		}
		Timestamp tempsActuel = new Timestamp(System.currentTimeMillis());
		Timestamp tempsCommande = new Timestamp(System.currentTimeMillis());
		tempsCommande.setHours(commande.heure);
		tempsCommande.setMinutes(commande.minute);
		commande.timestamp = tempsCommande;

		if (tempsCommande.before(tempsActuel)) {
			commandeSurPlace();
			return;
		}

		envoyerCommande();
	}

	@SuppressWarnings("deprecation")
	public void commandeAEmporter() {
		String reponse;

		while (true) {
			System.out.print("\n À quelle heure tu veux réserver ? (heure entre 0 et 23): ");
			reponse = interacteur.nextLine();
			try {
				int heure = Integer.valueOf(reponse);
				if (0 <= heure && heure <= 23) {
					commande.heure = heure;
					break;
				} else
					System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			} catch (Exception e) {
				System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 23");
			}
		}

		while (true) {
			System.out.print("\n À quelle minute tu veux réserver ? (minute entre 0 et 59): ");
			reponse = interacteur.nextLine();
			try {
				int minute = Integer.valueOf(reponse);
				if (0 <= minute && minute <= 59) {
					commande.minute = minute;
					break;
				} else
					System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			} catch (Exception e) {
				System.out.println("\n Oups... Tu n'as pas indiqué un chiffre entre 0 et 59");
			}
		}
		Timestamp tempsActuel = new Timestamp(System.currentTimeMillis());
		Timestamp tempsCommande = new Timestamp(System.currentTimeMillis());
		tempsCommande.setHours(commande.heure);
		tempsCommande.setMinutes(commande.minute);
		commande.timestamp = tempsCommande;

		if (tempsCommande.before(tempsActuel)) {
			commandeAEmporter();
			return;
		}
		envoyerCommande();
	}

	public void envoyerCommande() {
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(Cid) FROM COMMANDES");
			int new_uid = rs.getInt("MAX(Cid)") + 1;
			stmt.executeUpdate(
					"INSERT INTO COMMANDES VALUES (" + new_uid + ",CURRENT_TIMESTAMP," + String.valueOf(commande.prix)
							+ "," + String.valueOf(user.identifiant) + ",\'" + commande.type + "\')");

			for (String plat : commande.plats.keySet()) {
				stmt = jdbc.connection.createStatement();
				rs = stmt.executeQuery("SELECT Pid FROM PLATS WHERE PNom = \'" + plat + "\' AND PRestaurant = \'"
						+ commande.rMail + "\'");
				stmt.executeUpdate(
						"INSERT INTO PLATSCOMMANDE VALUES (" + new_uid + "," + String.valueOf(rs.getString("Pid"))
								+ ",\'" + commande.rMail + "\'," + String.valueOf(commande.plats.get(plat)) + ")");
			}

			switch (commande.type) {
				case "livraison":
					stmt.executeUpdate("INSERT INTO COMMANDESLIVREES VALUES (" + new_uid + ",\'" + commande.adresse
							+ "\',\'" + commande.texte + "\',\'" + commande.timestamp.toString() + "\',\'Attente\')");
					break;
				case "emporter":
					stmt.executeUpdate("INSERT INTO COMMANDESEMPORTEES VALUES (" + new_uid + ",\'Attente\')");
					break;
				case "place":
					stmt.executeUpdate("INSERT INTO COMMANDESSURPLACE VALUES (" + new_uid + "," + commande.nbPlaces
							+ ",\'" + commande.timestamp.toString() + "\',\'Attente\')");
					break;
			}

			clearConsole();
			System.out.println(
					" -- -- -- \n Merci pour ta commande ! Ça fera " + commande.prix
							+ "€ !\n Ta commande est en attente de validation par le restaurant ! \n -- -- -- \n");
			accueil();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void listeRestos() {
		try {
			ArrayList<String> restos = new ArrayList<String>();

			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT RNom FROM RESTAURANTS ORDER BY RNote DESC, RNom ASC;");

			while (rs.next())
				restos.add(rs.getString("RNom"));

			System.out.println(" Voici la liste des restaurants dans l'ordre décroissant des notes:");

			for (int i = 0; i < restos.size(); i += 10) {
				for (int j = i; j < i + 10 && j < restos.size(); j++)
					System.out.print("\n" + String.valueOf((j % 10) + 1) + ") " + restos.get(j));

				if (i + 10 < restos.size())
					System.out.print("\n\n11) Voir plus de restaurants");

				System.out.println("\n\n0) Retour à l'accueil\n");

				while (true) {
					System.out.print("\n Écris le numéro de ta réponse souhaitée: ");
					String reponse = interacteur.nextLine();
					if (reponse.equals("11") && i + 10 < restos.size())
						break;
					if (reponse.equals("1") && i < restos.size()) {
						commanderResto(restos.get(i));
						return;
					}
					if (reponse.equals("2") && i + 1 < restos.size()) {
						commanderResto(restos.get(i + 1));
						return;
					}
					if (reponse.equals("3") && i + 2 < restos.size()) {
						commanderResto(restos.get(i + 2));
						return;
					}
					if (reponse.equals("4") && i + 3 < restos.size()) {
						commanderResto(restos.get(i + 3));
						return;
					}
					if (reponse.equals("5") && i + 4 < restos.size()) {
						commanderResto(restos.get(i + 4));
						return;
					}
					if (reponse.equals("6") && i + 5 < restos.size()) {
						commanderResto(restos.get(i + 5));
						return;
					}
					if (reponse.equals("7") && i + 6 < restos.size()) {
						commanderResto(restos.get(i + 6));
						return;
					}
					if (reponse.equals("8") && i + 7 < restos.size()) {
						commanderResto(restos.get(i + 7));
						return;
					}
					if (reponse.equals("9") && i + 8 < restos.size()) {
						commanderResto(restos.get(i + 8));
						return;
					}
					if (reponse.equals("10") && i + 9 < restos.size()) {
						commanderResto(restos.get(i + 9));
						return;
					}
					if (reponse.equals("0")) {
						clearConsole();
						accueil();
						return;
					}
					System.out.println(
							"\n Aïe.. Ta réponse n'est pas valide ou bien il n'y a plus de restaurants. Choisis une réponse valide.\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void commanderResto(String resto) {
		clearConsole();
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT RMail FROM RESTAURANTS WHERE RNOM = \'" + resto + "\'");
			commande.rMail = rs.getString("RMail");
			rs = stmt.executeQuery("SELECT PNom, PDescription, PPrix FROM PLATS WHERE PRestaurant = \'"
					+ rs.getString("RMail") + "\'");

			commande.rNom = resto;

			ArrayList<Map<String, String>> plats = new ArrayList<Map<String, String>>();

			while (rs.next()) {

				Map<String, String> plat = new HashMap<String, String>();
				plat.put("Nom", rs.getString("PNom"));
				plat.put("Des", rs.getString("PDescription"));
				plat.put("Prix", String.valueOf(rs.getFloat("PPrix")));
				plats.add(plat);
			}

			if (plats.size() == 0) {
				clearConsole();
				System.out.println(
						" Oups... Il y a aucun plat disponible... Sélectionne un autre restaurant. \n");
				commande.commencerCommande();
				listeRestos();
				return;
			}

			System.out.println(" Voici les plats disponibles au restaurant \"" + resto + "\": \n");

			for (int i = 0; i < plats.size(); i += 10) {
				for (int j = i; j < i + 10 && j < plats.size(); j++) {
					System.out.println(String.valueOf((i % 10) + j + 1) + ") " + plats.get(j).get("Nom") + " ("
							+ plats.get(j).get("Prix") + " €) - " + plats.get(j).get("Des"));
				}
				if (i + 10 < plats.size()) {
					System.out.println("\n11) Voir plus de plats");
				}
				System.out.println("\n0) Retour aux restaurants et annuler la commande en cours\n");
				while (true) {
					System.out.print("\n Écris le numéro de ta réponse souhaitée: ");
					String reponse = interacteur.nextLine();
					if (reponse.equals("11") && i + 10 < plats.size())
						break;
					if (reponse.equals("1") && i < plats.size()) {
						ajouterACommande(plats.get(i), resto);
						return;
					}
					if (reponse.equals("2") && i + 1 < plats.size()) {
						ajouterACommande(plats.get(i + 1), resto);
						return;
					}
					if (reponse.equals("3") && i + 2 < plats.size()) {
						ajouterACommande(plats.get(i + 2), resto);
						return;
					}
					if (reponse.equals("4") && i + 3 < plats.size()) {
						ajouterACommande(plats.get(i + 3), resto);
						return;
					}
					if (reponse.equals("5") && i + 4 < plats.size()) {
						ajouterACommande(plats.get(i + 4), resto);
						return;
					}
					if (reponse.equals("6") && i + 5 < plats.size()) {
						ajouterACommande(plats.get(i + 5), resto);
						return;
					}
					if (reponse.equals("7") && i + 6 < plats.size()) {
						ajouterACommande(plats.get(i + 6), resto);
						return;
					}
					if (reponse.equals("8") && i + 7 < plats.size()) {
						ajouterACommande(plats.get(i + 7), resto);
						return;
					}
					if (reponse.equals("9") && i + 8 < plats.size()) {
						ajouterACommande(plats.get(i + 8), resto);
						return;
					}
					if (reponse.equals("10") && i + 9 < plats.size()) {
						ajouterACommande(plats.get(i + 9), resto);
						return;
					}
					if (reponse.equals("0")) {
						commande.commencerCommande();
						listeRestos();
						return;
					}
					System.out.println(
							"\n Aïe.. ta réponse n'est pas valide. Choisis une réponse valide. \n");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouterACommande(Map<String, String> plat, String resto) {
		int quantite = 1;
		String reponse;
		while (true) {
			System.out.print(
					" \n T'aimerais commander combien de plats comme celui-là ? Si tu n'indiques pas de quantité, on ajoutera que 1 à la commande: ");
			reponse = interacteur.nextLine();
			try {
				if (reponse == "")
					break;
				quantite = Integer.valueOf(reponse);
				if (quantite > 10)
					System.out.println("\n Ha ouais... " + reponse
							+ "? Ça fait beaucoup, mais on sait que t'es un petit gourmand ;P \n");
				if (quantite > 0)
					break;
			} catch (Exception e) {
				System.out.println(" \nIntroduis une vraie valeur numérique entière positive strictement, stp. \n");
			}
			System.out.println(" \nIntroduis une valeur numérique entière positive strictement, stp. \n");
		}
		if (commande.plats.containsKey(plat.get("Nom"))) {
			int nbPlats = commande.plats.get(plat.get("Nom")) + quantite;
			commande.plats.put(plat.get("Nom"), nbPlats);
		} else
			commande.plats.put(plat.get("Nom"), quantite);

		commande.prix += Float.valueOf(plat.get("Prix")) * quantite;

		while (true) {
			System.out.println("\n Tu veux continuer ta commande ou tout est bon ? \n");
			System.out.println("1) Je souhaite continuer ma commande");
			System.out.println("2) Tout est bon, je veux passer à la suite");
			System.out.print("\n Indique le chiffre correspondant au choix désiré: ");
			reponse = interacteur.nextLine();
			switch (reponse) {
				case "1":
					commanderResto(resto);
					return;
				case "2":
					clearConsole();
					validationCommande();
					return;
				default:
					clearConsole();
					System.out.println(" Aïe.. La valeur introduite est incorrecte...");
					break;
			}
		}
	}

	public void restoParCat(String categorie) {
		clearConsole();

		try {
			ArrayList<String> restos = new ArrayList<String>();

			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT DISTINCT RNom FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CATEGORIESRESTAURANT.CatNom = \'"
							+ categorie + "\'");

			while (rs.next())
				restos.add(rs.getString("RNom"));

			if (restos.size() == 0) {
				clearConsole();
				System.out.println(
						" Oups... Il y a aucun restaurant avec cette catégorie... Sélectionne une autre catégorie. \n");
				categoriesRecommandes();
				return;
			}

			System.out.println(" Voici les restaurants disponibles dans la catégorie \"" + categorie + "\":\n");

			for (int i = 0; i < restos.size(); i += 10) {
				for (int j = i; j < i + 10 && j < restos.size(); j++) {
					System.out.println(String.valueOf((i % 10) + j + 1) + ") " + restos.get(j));
				}
				if (i + 10 < restos.size()) {
					System.out.println("\n11) Voir plus de restaurants");
				}
				System.out.println("\n0) Retour aux catégories \n");
				while (true) {
					System.out.print("\n Écris le numéro de ta réponse souhaitée: ");
					String reponse = interacteur.nextLine();
					if (reponse.equals("11") && i + 10 < restos.size())
						break;
					if (reponse.equals("1") && i < restos.size()) {
						commanderResto(restos.get(i), categorie);
						return;
					}
					if (reponse.equals("2") && i + 1 < restos.size()) {
						commanderResto(restos.get(i + 1), categorie);
						return;
					}
					if (reponse.equals("3") && i + 2 < restos.size()) {
						commanderResto(restos.get(i + 2), categorie);
						return;
					}
					if (reponse.equals("4") && i + 3 < restos.size()) {
						commanderResto(restos.get(i + 3), categorie);
						return;
					}
					if (reponse.equals("5") && i + 4 < restos.size()) {
						commanderResto(restos.get(i + 4), categorie);
						return;
					}
					if (reponse.equals("6") && i + 5 < restos.size()) {
						commanderResto(restos.get(i + 5), categorie);
						return;
					}
					if (reponse.equals("7") && i + 6 < restos.size()) {
						commanderResto(restos.get(i + 6), categorie);
						return;
					}
					if (reponse.equals("8") && i + 7 < restos.size()) {
						commanderResto(restos.get(i + 7), categorie);
						return;
					}
					if (reponse.equals("9") && i + 8 < restos.size()) {
						commanderResto(restos.get(i + 8), categorie);
						return;
					}
					if (reponse.equals("10") && i + 9 < restos.size()) {
						commanderResto(restos.get(i + 9), categorie);
						return;
					}
					if (reponse.equals("0")) {
						clearConsole();
						categoriesRecommandes();
						return;
					}
					System.out.println(
							"\n Aïe.. Ta réponse n'est pas valide ou bien il n'y a plus de restaurants. Choisis une réponse valide. \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void commanderResto(String resto, String categorie) {
		clearConsole();
		try {
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT RMail FROM RESTAURANTS WHERE RNOM = \'" + resto + "\'");
			commande.rMail = rs.getString("RMail");
			rs = stmt.executeQuery("SELECT PNom, PDescription, PPrix FROM PLATS WHERE PRestaurant = \'"
					+ rs.getString("RMail") + "\'");

			commande.rNom = resto;

			ArrayList<Map<String, String>> plats = new ArrayList<Map<String, String>>();

			while (rs.next()) {

				Map<String, String> plat = new HashMap<String, String>();
				plat.put("Nom", rs.getString("PNom"));
				plat.put("Des", rs.getString("PDescription"));
				plat.put("Prix", String.valueOf(rs.getFloat("PPrix")));
				plats.add(plat);
			}

			if (plats.size() == 0) {
				clearConsole();
				System.out.println(
						" Oups... Il y a aucun plat disponible... Sélectionne un autre restaurant. \n");
				restoParCat(categorie);
				return;
			}

			System.out.println(" Voici les plats disponibles au restaurant \"" + resto + "\": \n");

			for (int i = 0; i < plats.size(); i += 10) {
				for (int j = i; j < i + 10 && j < plats.size(); j++) {
					System.out.println(String.valueOf((i % 10) + j + 1) + ") " + plats.get(j).get("Nom") + " ("
							+ plats.get(j).get("Prix") + " €) - " + plats.get(j).get("Des"));
				}
				if (i + 10 < plats.size()) {
					System.out.println("\n11) Voir plus de restaurants");
				}
				System.out.println("\n0) Retour aux restaurants \n");
				while (true) {
					System.out.print("\n Écris le numéro de ta réponse souhaitée: ");
					String reponse = interacteur.nextLine();
					if (reponse.equals("11") && i + 10 < plats.size())
						break;
					if (reponse.equals("1") && i < plats.size()) {
						ajouterACommande(plats.get(i), resto, categorie);
						return;
					}
					if (reponse.equals("2") && i + 1 < plats.size()) {
						ajouterACommande(plats.get(i + 1), resto, categorie);
						return;
					}
					if (reponse.equals("3") && i + 2 < plats.size()) {
						ajouterACommande(plats.get(i + 2), resto, categorie);
						return;
					}
					if (reponse.equals("4") && i + 3 < plats.size()) {
						ajouterACommande(plats.get(i + 3), resto, categorie);
						return;
					}
					if (reponse.equals("5") && i + 4 < plats.size()) {
						ajouterACommande(plats.get(i + 4), resto, categorie);
						return;
					}
					if (reponse.equals("6") && i + 5 < plats.size()) {
						ajouterACommande(plats.get(i + 5), resto, categorie);
						return;
					}
					if (reponse.equals("7") && i + 6 < plats.size()) {
						ajouterACommande(plats.get(i + 6), resto, categorie);
						return;
					}
					if (reponse.equals("8") && i + 7 < plats.size()) {
						ajouterACommande(plats.get(i + 7), resto, categorie);
						return;
					}
					if (reponse.equals("9") && i + 8 < plats.size()) {
						ajouterACommande(plats.get(i + 8), resto, categorie);
						return;
					}
					if (reponse.equals("10") && i + 9 < plats.size()) {
						ajouterACommande(plats.get(i + 9), resto, categorie);
						return;
					}
					if (reponse.equals("0")) {
						restoParCat(categorie);
						return;
					}
					System.out.println(
							"\n Aïe.. Ta réponse n'est pas valide. Choisis une réponse valide. \n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouterACommande(Map<String, String> plat, String resto, String categorie) {
		int quantite = 1;
		String reponse;

		while (true) {
			System.out.print(
					" \n T'aimerais commander combien de plats comme celui-là ? Si tu n'indiques pas de quantité, on ajoutera que 1 à la commande: ");
			reponse = interacteur.nextLine();
			try {
				if (reponse == "")
					break;
				quantite = Integer.valueOf(reponse);
				if (quantite > 10)
					System.out.println("\n Ha ouais... " + reponse
							+ "! Ça fait beaucoup ! Mais on sait que t'es gourmand ;P \n");
				break;
			} catch (Exception e) {
				System.out.println("\n Écris une vraie valeur numérique entière positive strictement. \n");
			}
		}

		if (commande.plats.containsKey(plat.get("Nom"))) {
			int nbPlats = commande.plats.get(plat.get("Nom")) + quantite;
			commande.plats.put(plat.get("Nom"), nbPlats);
		} else
			commande.plats.put(plat.get("Nom"), quantite);

		commande.prix += Float.valueOf(plat.get("Prix")) * quantite;

		while (true) {
			System.out.println("\n Tu veux continuer ta commande ou ça sera assez ? \n");
			System.out.println("1) Je souhaite continuer ma commande");
			System.out.println("2) Ça sera bon, je veux passer à la suite");
			System.out.print("\n Indique le chiffre correspondant à ton choix désiré: ");
			reponse = interacteur.nextLine();
			switch (reponse) {
				case "1":
					commanderResto(resto, categorie);
					return;
				case "2":
					clearConsole();
					validationCommande();
					return;
				default:
					clearConsole();
					System.out.println(" Aïe... La valeur introduite est incorrecte... \n");
					break;
			}
		}
	}

	public void laisserEvaluation() {
		try {
			boolean peutEvaluer = false;
			Statement stmt = jdbc.connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT Cid, CDate, CPrix FROM COMMANDES WHERE U_Id = \'" + user.getIdentifiant()
							+ "\' AND ((SELECT COUNT(Eid) FROM EVALUATIONS WHERE EVALUATIONS.Cid = COMMANDES.Cid) < 1)");

			if (rs.next()) {
				System.out.println("Id    Date     Heure     Prix\n_______________________________\n");
				peutEvaluer = true;
			}

			if (peutEvaluer) {
				do {
					System.out.println(rs.getString("Cid") + ") " + rs.getString("CDate")
							+ "   " + rs.getString("CPrix") + "€");
				} while (rs.next());

				System.out.print(
						"\n Entre le numéro de la commande à évaluer (ou tape \"retour\" pour retourner à l'accueil): ");
				String userInput = interacteur.nextLine();
				stmt = jdbc.connection.createStatement();
				switch (userInput) {
					case "retour":
						clearConsole();
						accueil();
						break;
					default:
						rs = stmt.executeQuery(
								"SELECT Cid, CDate, CPrix FROM COMMANDES WHERE Cid = \'" + userInput + "\' ");
						int nbLoop = 0;
						while (rs.next())
							nbLoop++;
						if (nbLoop == 0) {
							clearConsole();
							System.out.println(" Le numéro que tu as rentré ne correspond à aucune commande.\n");
							laisserEvaluation();
						} else {
							int idCommande = Integer.valueOf(userInput);
							System.out.print("\n Entre une note (entier entre 1 et 5 compris): ");
							int userInputInt = Integer.valueOf(interacteur.nextLine());
							while (!(userInputInt == 1 || userInputInt == 2 || userInputInt == 3 || userInputInt == 4
									|| userInputInt == 5)) {
								System.out.println("Note incorrecte");
								System.out.print("\n Entre une note (entier entre 1 et 5 compris): ");
								userInputInt = Integer.valueOf(interacteur.nextLine());
							}
							String note = String.valueOf(userInputInt);
							System.out.print("\n Entre une un avis (facultatif): ");
							userInput = interacteur.nextLine();
							System.out.println("\n Merci d'avoir laissé un avis !\n");

							Timestamp tempsEval = new Timestamp(System.currentTimeMillis());

							stmt = jdbc.connection.createStatement();
							rs = stmt.executeQuery("SELECT MAX(Eid) FROM EVALUATIONS");
							rs.next();

							jdbc.insertValeur("EVALUATIONS",
									"(" + (rs.getInt("MAX(Eid)") + 1) + ", \'" + tempsEval + "\', "
											+ "\'" + userInput + "\', " + note + ", " + idCommande + ")");
						}
				}
			} else
				System.out.println("Aucune commande n'est disponible à l'évaluation. Retour à l'accueil.\n");
			accueil();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void connexion() {
		System.out.println(" -- -- -- \n Bienvenue à GrenobleEAT ! \n");
		System.out.println(" As-tu un compte ? \n -- -- -- \n");
		System.out.println("1) J'ai un compte utilisateur");
		System.out.println("2) Créer un compte");
		System.out.println("3) Quitter l'application \n");
		System.out.print(" Tape le numéro de la réponse souhaitée : ");

		switch (interacteur.nextLine()) {
			case "1":
				clearConsole();
				identification();
				break;
			case "2":
				clearConsole();
				creerCompte();
				break;
			case "3":
				clearConsole();
				quit();
				break;
			default:
				clearConsole();
				System.out.println("Tu n'as pas indiqué une réponse valide.\n");
				connexion();
				break;
		}
	}

	public void identification() {
		System.out.print(" Connexion (tape quit pour revenir à l'écran principal)\n\n Email: ");
		String mail = interacteur.nextLine();

		if (mail.equals("quit")) {
			clearConsole();
			connexion();
			return;
		}

		System.out.print("\n Mot de passe: ");
		String mdp = interacteur.nextLine();

		if (mdp.equals("quit")) {
			clearConsole();
			connexion();
			return;
		}

		try {
			Statement stmt = jdbc.connection.createStatement();
			// Requête fonctionnelle (retourne l'UId si un mdp existe)
			ResultSet rs = stmt
					.executeQuery("SELECT U_id, UMdp, UNom, UPrenom, UAdresse, UMail FROM UTILISATEURS WHERE UMail = \'"
							+ mail + "\'");
			String id = String.valueOf(rs.getInt("U_id"));

			if (!mail.equals(rs.getString("UMail")) || !mdp.equals(rs.getString("UMdp"))) {
				clearConsole();
				System.out.println(
						" Identifiant ou mot de passe incorrect. Vérifie tes entrées.\n");
				identification();
				return;
			} else {
				this.user = new Utilisateur(Integer.valueOf(id), mail, rs.getString("UNom"), rs.getString("UPrenom"),
						mdp,
						rs.getString("UAdresse"));
				clearConsole();
				accueil();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void creerCompte() {
		String reponse;

		System.out.println(" L'aventure commence par l'inscritpion !\n");

		while (true) {
			try {
				System.out.print(" Email: ");
				reponse = interacteur.nextLine();
				if (reponse != "" && reponse.contains("@")) {
					user.eMail = reponse;
					// Vérification que le compte n'existe pas déjà
					Statement stmt = jdbc.connection.createStatement();
					ResultSet rs = stmt
							.executeQuery("SELECT UNom FROM UTILISATEURS WHERE UMail = \'" + user.eMail + "\'");
					rs.next();
					String nom = rs.getString("UNom");
					if (nom == null)
						break;
					System.out.println("\n Cet email est déjà utilisé, merci d'en choisir un autre\n");
				} else {
					clearConsole();
					System.out.println(" Ceci n'est pas une adresse valide.\n");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		while (true) {
			System.out.print("\n Nom: ");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.nom = reponse;
				break;
			}
		}

		while (true) {
			System.out.print("\n Prénom: ");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.prenom = reponse;
				break;
			}
		}

		while (true) {
			System.out.print("\n Adresse: ");
			reponse = interacteur.nextLine();
			if (reponse != "") {
				user.adresse = reponse;
				break;
			}
		}

		while (true) {
			System.out.print("\n Mot de passe: ");
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
			
			jdbc.insertValeur("UTILISATEURS",
			"(\'" + new_uid + "\', \'" + user.eMail + "\', \'" + user.mdp + "\', \'" + user.nom
			+ "\', \'" + user.prenom + "\', \'" + user.adresse + "\')");
			
			clearConsole();
			accueil();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void droitOubli() {
		System.out.print(" Es-tu sûr(e) de vouloir effacer tes données, " + user.getPrenom() + "?");
		System.out.println(" /!\\ LES DONNÉES NE POURRONT PAS ÊTRE RÉCUPÉRÉES /!\\ \n");

		System.out.println("1) Oui, je souhaite tout effacer");
		System.out.println("2) Retour\n");

		System.out.print(" Tape le numéro de la réponse que tu souhaites: ");

		switch (interacteur.nextLine()) {
			case "1":
				try {
					Statement stmt = jdbc.connection.createStatement();
					stmt.executeUpdate(
							"UPDATE UTILISATEURS SET UMail = NULL, UMdp = NULL, UNom = NULL, UPrenom = NULL, UAdresse = NULL WHERE U_Id = "
									+ String.valueOf(user.getIdentifiant()));
					clearConsole();
					System.out.println(" Les données personnelles ont été effacées. Fermeture de la session...\n");
					connexion();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "2":
				clearConsole();
				accueil();
				break;
			default:
				clearConsole();
				System.out.println(" Tu n'as pas indiqué une réponse valide.\n");
				droitOubli();
				break;
		}
	}
}

import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	public static void main(String[] args) {
		DriverJDBC test = new DriverJDBC();
		test.connexion();
		try {
			System.out.println(" -- -- -- \n Création de table "+"\n -- -- -- \n");
			Statement stmt = test.connection.createStatement();
			stmt.executeUpdate("CREATE TABLE UTILISATEURS (U_Id int NOT NULL, UMail varchar(30), Mdp varchar(30), UNom varchar(30), Prenom varchar(30), UAdresse varchar(30))");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		test.insertValeur("UTILISATEURS", "(1, \'jorge@test.com\', \'password\', \'Luri\', \'Jorge\', \'4 Rue du Test, Grenoble 38000\')");
		test.insertValeur("UTILISATEURS", "(2, \'cedric@test.com\', \'password\', \'Pauly\', \'Cédric\', \'4 Rue du Test, Grenoble 38000\')");
		test.fermeture();
		Interface autreTest = new Interface();
		autreTest.connexion();
		test.connexion();
		test.effacerTable("UTILISATEURS");
		test.fermeture();
	}
}

import java.sql.SQLException;
import java.sql.Statement;

public class TestCed {
	public static void main(String[] args) {
		DriverJDBC test = new DriverJDBC();
		test.connexion();
		try {
			System.out.println(" -- -- -- \n Création de table "+"\n -- -- -- \n");
			Statement stmt = test.connection.createStatement();
			stmt.executeUpdate("CREATE TABLE COMMANDE (CId number(,0) NOT NULL, CDate date(7), CHeure timestamp(6)(,6), CPrix float(126,) NULL, UId number(,0) NULL, Typecommande varchar2(9) NULL)");
            stmt.executeUpdate("CREATE TABLE EVALUATION (EId number(,0) NOT NULL, CDate date(7), CHeure timestamp(6)(,6), Avis varchar2(300) NULL, ENote number(,0), CId number(,0) NULL)");

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		test.insertValeur("COMMANDE", "(1, 2022-11-21, 08:32:04 PM, 12.65, 12, EMPORTEE)");
		
			

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
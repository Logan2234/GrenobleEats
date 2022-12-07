import java.sql.*;
// import oracle.jdbc.driver.*;

public class DriverJDBC {

	// public oracle.jdbc.driver.OracleDriver driver;
	private String url;
	private String user;
	private String passwrd;
	public Connection connection;

	public DriverJDBC() {
		/* 
		this.url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
		this.user = "lurivanj";
		this.passwrd = "lurivanj";

		try {
			System.out.println(" -- -- -- \n Création du Driver Oracle \n -- -- -- \n");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
	}

	public void connexion() {
		try {
			System.out.print(" -- -- -- \n Tentative de connexion... ");
			//this.connection = DriverManager.getConnection(this.url, this.user, this.passwrd);
			this.connection = DriverManager.getConnection("jdbc:sqlite:GrenobleEAT.db");
			System.out.println("Connexion établie \n -- -- -- \n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void creationTable(String nomTable, String attributs) {
		try {
			System.out.println(" -- -- -- \n Création de table : " + nomTable + " avec les attributs " + attributs
					+ "\n -- -- -- \n");
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("CREATE TABLE " + nomTable + attributs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertValeur(String nomTable, String values) {
		try {
			System.out.println(
					" -- -- -- \n Insertion des valeurs: " + values + " dans la table " + nomTable + "\n -- -- -- \n");
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + nomTable + " VALUES " + values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Je pense qu'il faudra faire un par type
	public void selectStringValues(String nomTable, String attributs) {
		try {
			System.out.println(" -- -- -- \n Lecture des valeurs de la table " + nomTable + "\n -- -- -- \n");
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + attributs + " FROM " + nomTable);
			String[] colonnes = attributs.split(",");
			String out = " ";
			
			while (rs.next()) {
				for (String colonne : colonnes) {
					out += rs.getString(colonne) + " ";
				}
				out += " \n ";
			}

			System.out.println(out);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void effacerTable(String nomTable) {
		try {
			System.out.println(" -- -- -- \n Effaçage de table : " + nomTable + " \n -- -- -- \n");
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("DROP TABLE " + nomTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void fermeture() {
		System.out.print("\n -- -- -- \n Tentative de fermeture de connexion... ");
		try {
			this.connection.close();
			System.out.println("Connexion fermée \n -- -- -- \n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

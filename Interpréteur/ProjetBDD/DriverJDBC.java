import java.sql.*;
import oracle.jdbc.driver.*;
public class DriverJDBC {
	
	public oracle.jdbc.driver.OracleDriver driver;
	private String url;
	private String user;
	private String passwrd;
	private Connection connection;
	
	public DriverJDBC() {
		
		this.url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
		this.user = "lurivanj";
		this.passwrd =  "lurivanj";
	
		try { 
			DriverManager.registerDriver( new oracle.jdbc.driver.OracleDriver() );
			System.out.println(" -- -- -- \n Création du Driver Oracle \n -- -- -- \n"); 
		
		} catch (SQLException e) {
			
			e.printStackTrace(); 
		
		}
	}
	
	public void connexion() {
		
		try {
			System.out.println(" -- -- -- \n Tentative de connexion \n"); 
			this.connection = DriverManager.getConnection(this.url, this.user, this.passwrd);
			System.out.println(" Connexion établie \n -- -- -- \n");
			
		} catch (SQLException e) { 
				
			e.printStackTrace();
			
		}
	}
	
	public void creationTable(String nomTable) {
		try {
			System.out.println(" -- -- -- \n Création de table : " + nomTable + " \n -- -- -- \n");
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("CREATE TABLE " + nomTable + "(hey integer)");
			
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
		System.out.println(" -- -- -- \n Tentative de fermeture de connexion \n -- -- -- \n");
		try {
			this.connection.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}

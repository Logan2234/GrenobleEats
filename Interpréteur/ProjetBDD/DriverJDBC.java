import java.sql.*;
import oracle.jdbc.driver.*;
public class DriverJDBC {
	
	public oracle.jdbc.driver.OracleDriver driver;
	private String url;
	private String user;
	private String passwrd;
	private Connection connection;
	
	public DriverJDBC() {
		
		this.url = " jdbc: oracle : thin : @oracle1 . ensimag . fr : 1521:oracle1 ";
		this.user = "lurivanj";
		this.passwrd =  "lurivanj ";
	
		try { 
			DriverManager.registerDriver(
			new oracle.jdbc.driver.OracleDriver() );
			System.out.println(" -- -- -- \n Création du Driver Oracle \n -- -- -- \n"); 
		
		} catch (SQLException e) {
			
			e.printStackTrace(); 
		
		}
	}
	
	public void connexion() {
		try {
			
			this.connection = DriverManager.getConnection(this.url, this.user, this.passwrd);
			System.out.println(" -- -- -- \n Connexion établie \n -- -- -- \n");
			
		} catch (SQLException e) { 
				
			e.printStackTrace();
			
		}
	}
	
	public void creationTable() {
		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeQuery("CREATE TABLE Test(hey integer, PRIMARY KEY(hey));");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}

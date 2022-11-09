
public class Test {
	public static void main(String[] args) {
		DriverJDBC test = new DriverJDBC();
		test.connexion();
		test.creationTable();
		System.out.println("Table créée"); 
	}
}

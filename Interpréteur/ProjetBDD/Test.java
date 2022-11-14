
import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		DriverJDBC test = new DriverJDBC();
		Scanner interacteur = new Scanner(System.in);
		test.connexion();
		test.creationTable("Test", "(hey varchar (30), test varchar (30))");
		test.insertValeur("Test", "(\'Test\', \'Probando\')");
		test.insertValeur("Test", "(\'Test2\', \'Probando2\')");
		System.out.println("C'est qui le plus grand bg ? \n");
		String leBoss = interacteur.nextLine();
		System.out.println(leBoss + "\n Non mais en vrai, c'est qui ?\n");
		leBoss = interacteur.nextLine();
		System.out.println(leBoss);
		test.selectValues("Test", "hey,test");
		test.effacerTable("Test");
		test.fermeture();
	}
}

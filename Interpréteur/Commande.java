import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
public class Commande {
	public float prix;
	public int uid;
	public String type;
	public Map<String, Integer> plats;
	public String rNom;
	
	// Livraison
	public String texte;
	public String adresse;
	
	// Sur place
	public int nbPlaces;
	
	public int heure;
	public int minute;
	
	
	public Commande() {
		commencerCommande();
	}
	
	public void commencerCommande() {
		prix = 0;
		uid = -1;
		type = "";
		rNom = "";
		plats = new HashMap<String, Integer>();
	}
	
}

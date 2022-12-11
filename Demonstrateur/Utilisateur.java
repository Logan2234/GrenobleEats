public class Utilisateur {

    public int identifiant;
    public String eMail;
    public String nom;
    public String prenom;
    public String mdp;
    public String adresse;

    public Utilisateur(int identifiant, String eMail, String nom, String prenom, String mdp, String adresse) {
        this.identifiant = identifiant;
        this.eMail = eMail;
        this.nom = nom;
        this.prenom = prenom;
        this.mdp = mdp;
        this.adresse = adresse;
    }
    
    public Utilisateur() {
    	
    }
    

    public String getMail() {
        return this.eMail;
    }

    public int getIdentifiant() {
        return this.identifiant;
    }

    public String getMdp() {
        return this.mdp;
    }

    public String getNom() {
        return this.nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public String getPrenom() {
        return this.prenom;
    }

    @Override
    public String toString() {
        return "Cet utilisateur a pour identifiant "
                + this.identifiant + ", a pour eMail "
                + this.eMail + " et pour mot de passe : "
                + this.mdp + ", s'appelle " + this.prenom + " "
                + this.nom + " et habite au " + this.adresse;
    }
}

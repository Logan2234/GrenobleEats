public class Utilisateur {

    private String identifiant;
    private String eMail;
    private String nom;
    private String prenom;
    private String mdp;
    private String adresse;

    public Utilisateur(String identifiant, String mdp){
        this.identifiant = identifiant;
        this.mdp = mdp;
    }
    
    public String getMail() {
    	return this.eMail;
    }

    public String getIdentifiant(){
        return this.identifiant;
    }
    
    public String getMdp(){
        return this.mdp;
    }

    public String getNom(){
        return this.nom;
    }

    public String getAdresse(){
        return this.adresse;
    }

    public String getPrenom(){
        return this.prenom;
    }

    @Override
    public String toString(){
        return "Cet utilisateur a pour identifiant "
            + this.identifiant + " et pour mot de passe : " 
            + this.mdp + ", s'appelle " + this.prenom + " "
            + this.nom + " et habite au " + this.adresse;
    }
}

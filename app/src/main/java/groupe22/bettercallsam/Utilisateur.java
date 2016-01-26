package groupe22.bettercallsam;

/**
 * Created by goodhouse on 06/01/16.
 */
public class Utilisateur {
    private String nom;
    private String prenom;
    private int numero;
    private boolean estConducteur;

    public Utilisateur(){

    }

    public Utilisateur(String nom, String prenom, int numero, boolean estConducteur){
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.estConducteur = estConducteur;
    }

    public void setEstConducteur(boolean estConducteur) {this.estConducteur = estConducteur;}

    public boolean isEstConducteur() {return estConducteur;}

    public void setNumero(int numero) {this.numero = numero;}

    public int getNumero() {return numero;}

    public void setPrenom(String prenom) {this.prenom = prenom;}

    public String getPrenom() {return prenom;}

    public void setNom(String nom) {this.nom = nom;}

    public String getNom() {return nom;}
}

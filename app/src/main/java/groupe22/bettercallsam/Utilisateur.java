package groupe22.bettercallsam;

/**
 * Created by goodhouse on 06/01/16.
 */
public class Utilisateur {
    private String nom;
    private String prenom;
    private String numero;
    private boolean estConducteur;

    public Utilisateur() {

    }

    public Utilisateur(String nom, String prenom, String numero, boolean estConducteur) {
        this.nom = nom;
        this.prenom = prenom;
        this.numero = numero;
        this.estConducteur = estConducteur;
    }

    public boolean isEstConducteur() {
        return estConducteur;
    }

    public void setEstConducteur(boolean estConducteur) {
        this.estConducteur = estConducteur;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}

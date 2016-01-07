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

    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public int getNumero(){
        return numero;
    }

    public boolean isEstConducteur(){
        return estConducteur;
    }

}

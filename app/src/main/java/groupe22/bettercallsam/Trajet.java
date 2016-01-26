package groupe22.bettercallsam;

import java.util.Date;

/**
 * Created by goodhouse on 07/01/16.
 */
public class Trajet {
    private String villeDepart;
    private String adresseDepart;
    private String villeArrivee;
    private String adresseArrivee;
    private Date dateDepart;
    private int nombrePlace;

    public Trajet() {

    }

    public Trajet(String villeDepart, String adresseDepart, String villeArrivee, String adresseArrivee, Date dateDepart, int nombrePlace) {
        this.villeDepart = villeDepart;
        this.adresseDepart = adresseDepart;
        this.villeArrivee = villeArrivee;
        this.adresseArrivee = adresseArrivee;
        this.dateDepart = dateDepart;
        this.nombrePlace = nombrePlace;
    }

    public String getAdresseDepart() {
        return adresseDepart;
    }

    public void setAdresseDepart(String adresseDepart) {
        this.adresseDepart = adresseDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public String getAdresseArrivee() {
        return adresseArrivee;
    }

    public void setAdresseArrivee(String adresseArrivee) {
        this.adresseArrivee = adresseArrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNombrePlace() {
        return nombrePlace;
    }

    public void setNombrePlace(int nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }
}

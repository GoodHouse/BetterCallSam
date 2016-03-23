package groupe22.bettercallsam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goodhouse on 07/01/16.
 */
public class Trajet {
    private String villeDepart;
    private String adresseDepart;
    private String villeArrivee;
    private String adresseArrivee;
    private String dateDepart;
    private String heureDepart;
    private int nombrePlaceDisponibles;
    private String conducteur;


    public Trajet() {

    }

    public Trajet(String villeDepart, String adresseDepart, String villeArrivee, String adresseArrivee, String dateDepart, String heureDepart, int nombrePlaceDisponibles, String conducteur) {
        this.villeDepart = villeDepart;
        this.adresseDepart = adresseDepart;
        this.villeArrivee = villeArrivee;
        this.adresseArrivee = adresseArrivee;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.nombrePlaceDisponibles = nombrePlaceDisponibles;
        this.conducteur = conducteur;
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

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNombrePlaceDisponibles() {
        return nombrePlaceDisponibles;
    }

    public void setNombrePlaceDisponibles(int nombrePlaceDisponibles) {
        this.nombrePlaceDisponibles = nombrePlaceDisponibles;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getConducteur(){
        return this.conducteur;
    }
}

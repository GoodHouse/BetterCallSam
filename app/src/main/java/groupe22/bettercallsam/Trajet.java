package groupe22.bettercallsam;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Boolean detour;
    private int prix;


    public Trajet() {

    }

    public Trajet(String villeDepart, String adresseDepart, String villeArrivee, String adresseArrivee, String dateDepart, String heureDepart, int nombrePlaceDisponibles, String conducteur, Boolean detour, int prix) {
        this.villeDepart = villeDepart;
        this.adresseDepart = adresseDepart;
        this.villeArrivee = villeArrivee;
        this.adresseArrivee = adresseArrivee;
        this.dateDepart = dateDepart;
        this.heureDepart = heureDepart;
        this.nombrePlaceDisponibles = nombrePlaceDisponibles;
        this.conducteur = conducteur;
        this.detour = detour;
        this.prix = prix;
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

    public String getHeureDepart() {
        return heureDepart;
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

    public String getConducteur() {
        return this.conducteur;
    }

    public Boolean getDetour(){
        return detour;
    }

    public void setDetour(Boolean detour){
        this.detour = detour;
    }
}

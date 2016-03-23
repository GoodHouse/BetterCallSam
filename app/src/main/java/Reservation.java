/**
 * Created by goodhouse on 23/03/16.
 */
public class Reservation {
    private String trajet;
    private String passager;
    private int nbPlaces;
    private Boolean accepte;

    public Reservation() {
    }

    public Reservation(String trajet, String passager, int nbPlaces, Boolean accepte) {
        this.trajet = trajet;
        this.passager = passager;
        this.nbPlaces = nbPlaces;
        this.accepte = accepte;
    }

    public String getPassager() {
        return passager;
    }

    public void setPassager(String passager) {
        this.passager = passager;
    }

    public String getTrajet() {
        return trajet;
    }

    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public Boolean getAccepte() {
        return accepte;
    }

    public void setAccepte(Boolean accepte) {
        this.accepte = accepte;
    }

}

package groupe22.bettercallsam;

/**
 * Created by goodhouse on 23/03/16.
 */
public class Reservation {
    String trajet;
    private String passager;
    private int nbPlaces;
    private Boolean accepte;

    public Reservation() {
    }

    public Reservation(String passager, int nbPlaces, Boolean accepte, String trajet) {
        this.passager = passager;
        this.nbPlaces = nbPlaces;
        this.accepte = accepte;
        this.trajet = trajet;
    }

    public String getPassager() {
        return passager;
    }

    public void setPassager(String passager) {
        this.passager = passager;
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


    public String getTrajet() {
        return trajet;
    }

    public void setTrajet(String trajet) {
        this.trajet = trajet;
    }
}

package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

/**
 * Created by castazer on 23/03/16.
 */
public class AffichageDetailsTrajet extends AppCompatActivity {

    int nbPlacesReservees;
    String trajet;
    Trajet trajetSelec;
    Utilisateur conducteur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trajet);

        Intent intent = getIntent();
        trajet = intent.getStringExtra("trajet");
        nbPlacesReservees = intent.getIntExtra("nbPlacesReservees", 1);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        /*final TextView villeDepart = (TextView) findViewById(R.id.departVille);
        final TextView adresseDepart = (TextView) findViewById(R.id.departAdresse);
        final TextView villeArrivee = (TextView) findViewById(R.id.arriveeVille);
        final TextView adresseArrivee = (TextView) findViewById(R.id.arriveeAdresse);
        final TextView date = (TextView) findViewById(R.id.date);
        final TextView nbPlaces = (TextView) findViewById(R.id.nbPlaces);
*/

        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                trajetSelec = snapshot.child("trips").child(trajet).getValue(Trajet.class);
                conducteur = snapshot.child("users").child(trajetSelec.getConducteur()).getValue(Utilisateur.class);
                /*villeDepart.setText("Ville de départ : " + trajetSelec.getVilleDepart());
                adresseDepart.setText("Adresse de départ : " + trajetSelec.getAdresseDepart());
                villeArrivee.setText("Ville d'arrivée : " + trajetSelec.getVilleArrivee());
                adresseArrivee.setText("Adresse d'arrivée : " + trajetSelec.getAdresseArrivee());
                date.setText("Départ le " + trajetSelec.getDateDepart() + " à " + trajetSelec.getHeureDepart());
                nbPlaces.setText("Nombre de places disponibles : " + trajetSelec.getNombrePlaceDisponibles());
            */
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Button button = (Button) findViewById(R.id.boutonReserver);

        button.setText("Réserver " + nbPlacesReservees + " place(s)");
    }


    public void clickReservation(View view) {
        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        Firebase firebaseReservation = myFireBase.child("reservations");
        Firebase firebaseTrajets = myFireBase.child("trips");
        AuthData authData = myFireBase.getAuth();

        if (authData != null) {
            Random rdm = new Random();
            Reservation reserv = new Reservation(authData.getUid(), nbPlacesReservees, false, trajet);
            firebaseReservation.child(Integer.toString(rdm.nextInt(Integer.MAX_VALUE))).setValue(reserv);
            trajetSelec.setNombrePlaceDisponibles(trajetSelec.getNombrePlaceDisponibles() - nbPlacesReservees);
            firebaseTrajets.child(trajet).setValue(trajetSelec);


            Intent intentSms = new Intent(Intent.ACTION_VIEW);
            String sms = "Bonjour " + conducteur.getPrenom() + ", je voudrais réserver " + nbPlacesReservees + " places pour votre trajet entre " + trajetSelec.getAdresseDepart()
                    + ", " + trajetSelec.getVilleDepart() + " et " + trajetSelec.getAdresseArrivee() + ", " + trajetSelec.getVilleArrivee() + ".\n"
                    + "Pouvez vous me dire si vous acceptez de me prendre?";
            intentSms.putExtra("sms_body", sms);
            intentSms.putExtra("address", conducteur.getNumero());
            intentSms.setType("vnd.android-dir/mms-sms");
            startActivity(intentSms);
        }

        Intent intentMain = new Intent(this, MainActivity.class);
        Toast.makeText(this, "Votre trajet a été réservé", Toast.LENGTH_LONG).show();
        startActivity(intentMain);
    }
}
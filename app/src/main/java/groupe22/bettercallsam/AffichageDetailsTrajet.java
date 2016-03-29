package groupe22.bettercallsam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    String pointDepDemande;
    String pointArrDemande;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trajet);
        Button retourAccueil = (Button) findViewById(R.id.boutonRetourAccueil);
        retourAccueil.setVisibility(View.INVISIBLE);
        Button reservation = (Button) findViewById(R.id.boutonReserver);
        reservation.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        trajet = intent.getStringExtra("trajet");
        nbPlacesReservees = intent.getIntExtra("nbPlacesReservees", 1);
        pointDepDemande = intent.getStringExtra("pointDepDemande");
        pointArrDemande = intent.getStringExtra("pointArrDemande");
        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        final TextView adresseDepart = (TextView) findViewById(R.id.adresseDepart);
        final TextView adresseArrivee = (TextView) findViewById(R.id.adresseArrivee);
        final TextView villeDepart = (TextView) findViewById(R.id.villeDepart);
        final TextView villeArrivee = (TextView) findViewById(R.id.villeArrivee);
        final TextView date = (TextView) findViewById(R.id.date);
        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                trajetSelec = snapshot.child("trips").child(trajet).getValue(Trajet.class);
                conducteur = snapshot.child("users").child(trajetSelec.getConducteur()).getValue(Utilisateur.class);
                villeDepart.setText(trajetSelec.getVilleDepart() + ", ");
                villeArrivee.setText(trajetSelec.getVilleArrivee() + ", ");
                adresseDepart.setText(trajetSelec.getAdresseDepart());
                adresseArrivee.setText(trajetSelec.getAdresseArrivee());
                date.setText("Le " + trajetSelec.getDateDepart() + " à " + trajetSelec.getHeureDepart());
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
            Intent intentSms = new Intent(Intent.ACTION_VIEW);
            String sms = "Bonjour " + conducteur.getPrenom() + ", je voudrais réserver " + nbPlacesReservees + " places pour votre trajet entre " + trajetSelec.getAdresseDepart()
                    + ", " + trajetSelec.getVilleDepart() + " et " + trajetSelec.getAdresseArrivee() + ", " + trajetSelec.getVilleArrivee() + ".\n"
                    + "Je voudrais aller de " + pointDepDemande + " à " + pointArrDemande + ".\n"
                    + "Pouvez vous me dire si vous acceptez de me prendre?";
            intentSms.putExtra("sms_body", sms);
            intentSms.putExtra("address", conducteur.getNumero());
            intentSms.putExtra("exit_on_sent", true);
            intentSms.setType("vnd.android-dir/mms-sms");
            startActivity(intentSms);

            Random rdm = new Random();
            Reservation reserv = new Reservation(authData.getUid(), nbPlacesReservees, false, trajet);
            firebaseReservation.child(Integer.toString(rdm.nextInt(Integer.MAX_VALUE))).setValue(reserv);
            trajetSelec.setNombrePlaceDisponibles(trajetSelec.getNombrePlaceDisponibles() - nbPlacesReservees);
            firebaseTrajets.child(trajet).setValue(trajetSelec);

            Button retourAccueil = (Button) findViewById(R.id.boutonRetourAccueil);
            retourAccueil.setVisibility(View.VISIBLE);
            Button reservation = (Button) findViewById(R.id.boutonReserver);
            reservation.setVisibility(View.INVISIBLE);

            Toast.makeText(getApplicationContext(), "Votre trajet a bien été réservé", Toast.LENGTH_LONG).show();
        }
    }

    public void clickRetourAccueil(View view) {
        Intent intent = new Intent(this, Accueil.class);
        startActivity(intent);
    }
}
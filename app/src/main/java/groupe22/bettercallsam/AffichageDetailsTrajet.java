package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * Created by castazer on 23/03/16.
 */
public class AffichageDetailsTrajet extends AppCompatActivity {

    int nbPlacesReservees;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trajet);

        Intent intent = getIntent();
        final String trajet = intent.getStringExtra("trajet");
        nbPlacesReservees = intent.getIntExtra("nbPlacesReservees", 1);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/trips/");

        final TextView tv = (TextView)findViewById(R.id.textViewDep);

        myFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Trajet trajetSelec = snapshot.child(trajet).getValue(Trajet.class);
                tv.setText("Départ : " + trajetSelec.getVilleDepart() +", " + trajetSelec.getAdresseDepart() + "\n" +
                        "Arrivée : " + trajetSelec.getVilleArrivee() + ", " + trajetSelec.getAdresseArrivee() + "\n \n" +
                                "Le " + trajetSelec.getDateDepart() + " à " + trajetSelec.getHeureDepart());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Button button = (Button) findViewById(R.id.boutonReserver);

        button.setText("Réserver " + nbPlacesReservees + " place(s)");
    }

    public void clickReservation(View view){
        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        AuthData authData = myFireBase.getAuth();

        if(authData != null){
            Reservation reserv = new Reservation(authData.toString(), nbPlacesReservees, false);
            myFireBase.child("reservations").setValue(reserv);
        }
    }
}
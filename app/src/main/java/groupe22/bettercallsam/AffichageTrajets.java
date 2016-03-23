package groupe22.bettercallsam;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AffichageTrajets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_trajets);
        Intent intent = getIntent();
       /* final String villeDepart = intent.getStringExtra("villeDep");
        final String adresseDepart = intent.getStringExtra("adresseDepart");
        final String villeArrivee = intent.getStringExtra("villeArrivee");
        final String adresseArrivee = intent

        intent.putExtra("villeDepart", editTextVilleDepart.getText().toString().toLowerCase());
        intent.putExtra("adresseDepart", editTextAdresseDepart.getText().toString().toLowerCase());
        intent.putExtra("villeArrivee", editTextVilleArrivee.getText().toString().toLowerCase());
        intent.putExtra("adresseArrivee", editTextAdresseArrivee.getText().toString().toLowerCase());
        intent.putExtra("date", editTextDate.getText().toString());
        intent.putExtra("heure", editTextTemps.getText().toString());
        intent.putExtra("nbPlaces", Integer.parseInt(editTextNbPlaces.getText().toString()));*/


        final ListView listView = (ListView) findViewById(R.id.listView);


        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    snapshot = snapshot.child("trips");
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        postSnapshot.getValue();
                        Trajet trajet = postSnapshot.getValue(Trajet.class);
                        String villeDep = trajet.getVilleDepart();
                        /*if (ville.equals(villeDep)) {
                            String villeArr = trajet.getVilleArrivee();
                            String adDep = trajet.getAdresseDepart();
                            String adArr = trajet.getAdresseArrivee();
                            Toast.makeText(getApplicationContext(), villeDep + "\n" + villeArr + "\n" + adDep + "\n" + adArr, Toast.LENGTH_LONG).show();
                        }*/
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        } else
            //S'il n'est pas connecté, on le redirige sur l'activité principale
            startActivity(new Intent(this, MainActivity.class));
    }
}
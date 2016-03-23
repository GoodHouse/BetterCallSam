package groupe22.bettercallsam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AffichageTrajets extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    int compt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_trajets);
        Intent intent = getIntent();
        final String villeD = intent.getStringExtra("villeDep");
        final String villeA = intent.getStringExtra("villeArr");
        final String dateD = intent.getStringExtra("dateDep");
        final String heure = intent.getStringExtra("heure");


        Date dep = null;
        try {
            dep = sdf.parse(heure);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String nbPlaces = intent.getStringExtra("nbPlaces");
        final int nombrePlaces = Integer.valueOf(nbPlaces);


        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<String> listTrajet = new ArrayList<>();

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");


            final AuthData authData = myFireBase.getAuth();
            if(authData!=null)

            {
                final Date finalDep = dep;
                myFireBase.addValueEventListener(new ValueEventListener() {
                    //S'il est connecté, on récupère ses informations
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        snapshot = snapshot.child("trips");
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            postSnapshot.getValue();
                            Trajet trajet = postSnapshot.getValue(Trajet.class);
                            String villeDep = trajet.getVilleDepart();
                            String villeArr = trajet.getVilleArrivee();
                            String dateDepart = trajet.getDateDepart();
                            String heureDepart = trajet.getHeureDepart();
                            int places = trajet.getNombrePlaceDisponibles();


                            Date temps2 = null;
                            try {
                                temps2 = sdf.parse(heureDepart);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                                if  (villeD.equals(villeDep) &&
                                    (villeA.equals(villeArr)) &&
                                    (dateDepart.equals(dateD)) &&
                                    (places >= nombrePlaces) &&
                                    (((temps2.getMinutes() - finalDep.getMinutes()) <= 30) && ((temps2.getMinutes() - finalDep.getMinutes()) > 0))
                                    ) {
                                compt++;
                                String villeArrivee = trajet.getVilleArrivee().toString();
                                String adDep = trajet.getAdresseDepart().toString();
                                String adArr = trajet.getAdresseArrivee().toString();
                                String villeDepart = trajet.getVilleDepart().toString();
                                String dateDep = trajet.getDateDepart().toString();
                                String heureDep = trajet.getHeureDepart().toString();
                                int placeDispo = trajet.getNombrePlaceDisponibles();

                                String traj = "Trajet " + compt + " :\n \n" + "Adresse de départ : " + villeDepart + ", " + adDep + "\n" + "Adresse d'arrivée : " + villeArrivee + ", " + adArr + "\n \n"
                                        + "Date de départ : " + dateDep + " à " + heureDep + "\n" + "Places disponibles : " + placeDispo + "\n";
                                listTrajet.add(traj);

                            }
                        }


                        if (compt == 0) {
                            final String erreur = "Il n'y a pas de trajet";
                            listTrajet.add(erreur);

                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AffichageTrajets.this,
                                android.R.layout.simple_list_item_1,
                                listTrajet);
                        listView.setAdapter(adapter);

                        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = adapter.getItem(position);
                                final Intent intent = new Intent(getApplicationContext(), AffichageDetailsTrajet.class);
                                intent.putExtra("trajet", item);
                                startActivity(intent);

                            }
                        });*/
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            else

            //S'il n'est pas connecté, on le redirige sur l'activité principale
            startActivity(new Intent(this, MainActivity.class)

            );

        }

        protected void onListItemClick(ListView l, View v, int position, long id)
        {
            
        }

}
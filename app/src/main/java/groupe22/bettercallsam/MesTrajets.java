package groupe22.bettercallsam;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MesTrajets extends AppCompatActivity {

    int compt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_trajets);

        Intent intent = getIntent();
        final String typeDem = intent.getStringExtra("type");

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> listTrajet = new ArrayList<>();

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");


        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            final String idUtilisateur = authData.getUid();
            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    HashMap<String, String> map;

                    if (typeDem.equals("passager")) {
                        List<String> listReservIdTrajet = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : snapshot.child("reservations").getChildren()) {
                            Reservation reserv = postSnapshot.getValue(Reservation.class);
                            if (idUtilisateur.equals(reserv.getPassager())) {
                                listReservIdTrajet.add(reserv.getTrajet());
                            }
                        }

                        for (DataSnapshot postSnapshot : snapshot.child("trips").getChildren()) {
                            Trajet trajet = postSnapshot.getValue(Trajet.class);

                            if (listReservIdTrajet.contains(postSnapshot.getKey())) {
                                compt++;
                                String villeArrivee = trajet.getVilleArrivee().toString();
                                String adDep = trajet.getAdresseDepart().toString();
                                String adArr = trajet.getAdresseArrivee().toString();
                                String villeDepart = trajet.getVilleDepart().toString();
                                String dateDep = trajet.getDateDepart().toString();
                                String heureDep = trajet.getHeureDepart().toString();
                                String placeDispo = Integer.toString(trajet.getNombrePlaceDisponibles());

                                map = new HashMap<String, String>();

                                map.put("departVille", "Ville de depart  : " + villeDepart);
                                map.put("departAdresse", "Adresse de départ : " + adDep);
                                map.put("arriveeVille", "Ville d'arrivée : " + villeArrivee);
                                map.put("arriveeAdresse", "Adresse d'arrivée : " + adArr);
                                map.put("date", "Départ le " + dateDep + " à " + heureDep);
                                map.put("nbPlaces", "Nombre de places disponibles : " + placeDispo);
                                map.put("idTrajet", postSnapshot.getKey());

                                listTrajet.add(map);
                            }
                        }
                    } else {
                        for (DataSnapshot postSnapshot : snapshot.child("trips").getChildren()) {
                            Trajet trajet = postSnapshot.getValue(Trajet.class);


                            if (idUtilisateur.equals(trajet.getConducteur())) {
                                compt++;
                                String villeArrivee = trajet.getVilleArrivee().toString();
                                String adDep = trajet.getAdresseDepart().toString();
                                String adArr = trajet.getAdresseArrivee().toString();
                                String villeDepart = trajet.getVilleDepart().toString();
                                String dateDep = trajet.getDateDepart().toString();
                                String heureDep = trajet.getHeureDepart().toString();
                                String placeDispo = Integer.toString(trajet.getNombrePlaceDisponibles());

                                map = new HashMap<String, String>();

                                map.put("departVille", "Ville de depart  : " + villeDepart);
                                map.put("departAdresse", "Adresse de départ : " + adDep);
                                map.put("arriveeVille", "Ville d'arrivée : " + villeArrivee);
                                map.put("arriveeAdresse", "Adresse d'arrivée : " + adArr);
                                map.put("date", "Départ le " + dateDep + " à " + heureDep);
                                map.put("nbPlaces", "Nombre de places disponibles : " + placeDispo);
                                map.put("idTrajet", postSnapshot.getKey());

                                listTrajet.add(map);
                            }
                        }
                    }


                   /* if (compt == 0) {
                        final String erreur = "Il n'y a pas de trajet";
                        listTrajet.add(erreur);

                    }*/

                    final SimpleAdapter mSchedule = new SimpleAdapter(getApplicationContext(), listTrajet, R.layout.activity_affichage_item,
                            new String[]{"departVille", "departAdresse", "arriveeVille", "arriveeAdresse", "date", "nbPlaces", "idTrajet"}, new int[]{R.id.departVille, R.id.departAdresse, R.id.arriveeVille, R.id.arriveeAdresse, R.id.date, R.id.nbPlaces, R.id.idTrajet});


                    listView.setAdapter(mSchedule);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                            final Intent intent = new Intent(MesTrajets.this, AffichageDetailsTrajet.class);
                            intent.putExtra("trajet", map.get("idTrajet"));
                            intent.putExtra("nbPlacesReservees", 3);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        } else

            //S'il n'est pas connecté, on le redirige sur l'activité principale
            startActivity(new Intent(this, MainActivity.class)
            );
    }
}
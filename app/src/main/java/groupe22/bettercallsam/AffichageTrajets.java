package groupe22.bettercallsam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.StringTokenizer;

public class AffichageTrajets extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    int compt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_trajets);
        final Intent intent = getIntent();
        final String pointDepDemande = intent.getStringExtra("pointDep");
        final String pointArrDemande = intent.getStringExtra("pointArr");
        final String dateDem = intent.getStringExtra("dateDep");
        final String heure = intent.getStringExtra("heure");
        //final int nbPlaces = intent.getIntExtra("nbPlaces", 1);

        Date dep = null;
        try {
            dep = sdf.parse(heure);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String nbPlaces = intent.getStringExtra("nbPlaces");
        final int nombrePlaces = Integer.valueOf(nbPlaces);

        //Toast.makeText(this, nbPlaces, Toast.LENGTH_LONG).show();

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<HashMap<String,String>> listTrajet = new ArrayList<>();

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");


        final AuthData authData = myFireBase.getAuth();
        if (authData != null)

        {
            final Date heureDem = dep;
            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    snapshot = snapshot.child("trips");
                    HashMap<String, String> map;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        postSnapshot.getValue();
                        //idTrajet = postSnapshot.getKey().toString();
                        Trajet trajet = postSnapshot.getValue(Trajet.class);
                        String pointDepPropose = trajet.getAdresseDepart() + ", " + trajet.getVilleDepart();
                        String pointArrPropose = trajet.getAdresseArrivee() + ", " + trajet.getVilleArrivee();
                        String dateProp = trajet.getDateDepart();
                        String heureDepart = trajet.getHeureDepart();
                        int places = trajet.getNombrePlaceDisponibles();


                        Date heureProp = null;
                        try {
                            heureProp = sdf.parse(heureDepart);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(
                               //nbPlaces <= places &&
                                comparerHoraires(dateDem, dateProp, heureDem, heureProp) &&
                                        comparerAdresses(pointDepDemande, pointDepPropose) &&
                                        comparerAdresses(pointArrDemande, pointArrPropose)
                                ){
                            compt++;
                            String villeArrivee = trajet.getVilleArrivee().toString();
                            String adDep = trajet.getAdresseDepart().toString();
                            String adArr = trajet.getAdresseArrivee().toString();
                            String villeDepart = trajet.getVilleDepart().toString();
                            String dateDep = trajet.getDateDepart().toString();
                            String heureDep = trajet.getHeureDepart().toString();
                            String placeDispo = Integer.toString(trajet.getNombrePlaceDisponibles());

                            map = new HashMap<String, String>();

                            map.put("departVille", "Ville depart " + villeDepart);
                            map.put("departAdresse", adDep);
                            map.put("arriveeVille", villeArrivee);
                            map.put("arriveeAdresse", adArr);
                            map.put("date", dateDep+ " à " +heureDep);
                            map.put("nbPlaces", placeDispo);


                            listTrajet.add(map);

                        }
                    }


                   /* if (compt == 0) {
                        final String erreur = "Il n'y a pas de trajet";
                        listTrajet.add(erreur);

                    }*/

                    SimpleAdapter mSchedule = new SimpleAdapter(getApplicationContext(), listTrajet, R.layout.activity_affichage_item,
                            new String[] {"departVille","departAdresse","arriveeVille", "arriveeAdresse","date","nbPlaces"}, new int[] {R.id.departVille, R.id.departAdresse, R.id.arriveeVille, R.id.arriveeAdresse, R.id.date, R.id.nbPlaces});


                    listView.setAdapter(mSchedule);

                    /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = adapter.getItem(position);
                                final Intent intent = new Intent(AffichageTrajets.this, AffichageDetailsTrajet.class);
                                intent.putExtra("trajet", item);
                                intent.putExtra("nbPlacesReservees", nbPlaces);
                                startActivity(intent);

                            }
                        });*/

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
    public Boolean comparerAdresses(String pointProp, String pointDem) {
        Geocoder gc = new Geocoder(getApplicationContext());

        try {
            List<Address> adProp = gc.getFromLocationName(pointProp, 1);
            List<Address> adDEm = gc.getFromLocationName(pointDem, 1);
            Address addressProp = adProp.get(0);
            Address addressDem = adDEm.get(0);
            double latProp = addressProp.getLatitude();
            double longProp = addressProp.getLongitude();
            double latDem = addressDem.getLatitude();
            double longDem = addressDem.getLongitude();
            return latProp >= latDem - 0.004 && latProp <= latDem + 0.004 && longProp >= longDem - 0.004 && longProp <= longDem + 0.004;
        } catch (IOException e) {
            Intent intent = new Intent(this, RechercherTrajet.class);
            Toast.makeText(this, "Merci de vérifier les adresses", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        return false;
    }

    public Boolean comparerHoraires(String dateDem, String dateProp, Date heureDem, Date heureProp){
        int hProp = heureProp.getHours();
        int hDem = heureDem.getHours();
        int mProp = heureProp.getMinutes();
        int mDem = heureDem.getMinutes();
        if(dateDem.equals(dateProp)){
            if(hProp < hDem){
                hProp++;
                mProp -= 60;
            }
            if(hProp > hDem){
                hDem++;
                mDem -= 60;
            }
            if(hProp == hDem) {
                if (mProp - mDem <= 30 && mProp - mDem >= - 30) {
                    return true;
                }
            }
        }
        return false;
    }

}
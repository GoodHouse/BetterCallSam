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

public class AffichageTrajets extends AppCompatActivity {

    int compt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_trajets);
        final Intent intent = getIntent();
        final String pointDepDemande = intent.getStringExtra("pointDep");
        final String pointArrDemande = intent.getStringExtra("pointArr");
        final String dateDem = intent.getStringExtra("dateDep");
        final String heureDem = intent.getStringExtra("heure");
        final int nbPlaces = intent.getIntExtra("nbPlaces", 1);

        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> listTrajet = new ArrayList<>();

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");


        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {

            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    snapshot = snapshot.child("trips");
                    HashMap<String, String> map;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Trajet trajet = postSnapshot.getValue(Trajet.class);
                        String pointDepPropose = trajet.getAdresseDepart() + ", " + trajet.getVilleDepart();
                        String pointArrPropose = trajet.getAdresseArrivee() + ", " + trajet.getVilleArrivee();
                        String dateProp = trajet.getDateDepart();
                        String heureProp = trajet.getHeureDepart();
                        int places = trajet.getNombrePlaceDisponibles();
                        Boolean detour = trajet.getDetour();

                        int multiplicateur = detour ? 10 : 1;

                        if (
                                nbPlaces <= places &&
                                        comparerHoraires(dateDem, dateProp, heureDem, heureProp) &&
                                        comparerAdresses(pointDepDemande, pointDepPropose, multiplicateur) &&
                                        comparerAdresses(pointArrDemande, pointArrPropose, multiplicateur)
                                ) {
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
                            final Intent intent = new Intent(AffichageTrajets.this, AffichageDetailsTrajet.class);
                            intent.putExtra("trajet", map.get("idTrajet"));
                            intent.putExtra("nbPlacesReservees", nbPlaces);
                            intent.putExtra("pointDepDemande", pointDepDemande);
                            intent.putExtra("pointArrDemande", pointArrDemande);
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

    public Boolean comparerAdresses(String pointProp, String pointDem, int multiplicateur) {
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
            return latProp >= latDem - (0.004 * multiplicateur) && latProp <= latDem + (0.004 * multiplicateur) && longProp >= longDem - (0.004 * multiplicateur) && longProp <= longDem + (0.004 * multiplicateur);
        } catch (IOException e) {
            Intent intent = new Intent(this, RechercherTrajet.class);
            Toast.makeText(this, "Merci de vérifier les adresses", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        return false;
    }

    public Boolean comparerHoraires(String dateDem, String dateProp, String heureDem, String heureProp) {
        SimpleDateFormat heureFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date heureDemandee = null;
        Date heureProposee = null;
        Date dateProposee = null;
        Date dateDemandee = null;
        try {
            heureDemandee = heureFormat.parse(heureDem);
            heureProposee = heureFormat.parse(heureProp);
            dateDemandee = dateFormat.parse(dateDem);
            dateProposee = dateFormat.parse(dateProp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int hProp = heureProposee.getHours();
        int hDem = heureDemandee.getHours();
        int mProp = heureProposee.getMinutes();
        int mDem = heureDemandee.getMinutes();

        int jProp = dateProposee.getDay();
        int jDem = dateDemandee.getDay();
        int moisProp = dateProposee.getMonth();
        int moisDem = dateDemandee.getMonth();
        int aProp = dateProposee.getYear();
        int aDem = dateDemandee.getYear();

        if (aProp < aDem) {
            aProp++;
            moisProp -= 12;
        }
        if (aProp > aDem) {
            aDem++;
            moisDem -= 12;
        }
        if (aProp == aDem) {
            if (moisProp < moisDem) {
                switch (moisProp) {
                    case 1:
                        jProp -= 31;
                        break;
                    case 2:
                        jProp -= (aProp % 4 == 0) ? 29 : 28;
                        break;
                    case 3:
                        jProp -= 31;
                        break;
                    case 4:
                        jProp -= 30;
                        break;
                    case 5:
                        jProp -= 31;
                        break;
                    case 6:
                        jProp -= 30;
                        break;
                    case 7:
                        jProp -= 31;
                        break;
                    case 8:
                        jProp -= 31;
                        break;
                    case 9:
                        jProp -= 30;
                        break;
                    case 10:
                        jProp -= 31;
                        break;
                    case 11:
                        jProp -= 30;
                        break;
                    case 12:
                        jProp -= 31;
                        break;
                    default:
                        jProp = 0;
                        break;
                }
                moisProp++;
            }
            if (moisProp > moisDem) {
                switch (moisDem) {
                    case 1:
                        jDem -= 31;
                        break;
                    case 2:
                        jDem -= (aProp % 4 == 0) ? 29 : 28;
                        break;
                    case 3:
                        jDem -= 31;
                        break;
                    case 4:
                        jDem -= 30;
                        break;
                    case 5:
                        jDem -= 31;
                        break;
                    case 6:
                        jDem -= 30;
                        break;
                    case 7:
                        jDem -= 31;
                        break;
                    case 8:
                        jDem -= 31;
                        break;
                    case 9:
                        jDem -= 30;
                        break;
                    case 10:
                        jDem -= 31;
                        break;
                    case 11:
                        jDem -= 30;
                        break;
                    case 12:
                        jDem -= 31;
                        break;
                    default:
                        jDem = 0;
                        break;
                }
                moisDem++;
            }
            if (moisProp == moisDem) {

            }
        }


        if (hProp < hDem) {
            hProp++;
            mProp -= 60;
        }
        if (hProp > hDem) {
            hDem++;
            mDem -= 60;
        }
        if (hProp == hDem) {
            if (mProp - mDem <= 30 && mProp - mDem >= -30) {
                return true;
            }
        }


        return false;
    }

}
package groupe22.bettercallsam;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import groupe22.bettercallsam.TimePickerFragment;


public class ProposerTrajet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposer_trajet);
    }

    public void clickButtonProposer(View view) {
        final EditText textVilleDepart = (EditText) findViewById(R.id.editTextVilleDepart);
        final EditText textAdresseDepart = (EditText) findViewById(R.id.editTextAdresseDepart);
        final EditText textVilleArrivee = (EditText) findViewById(R.id.editTextVilleArrivee);
        final EditText textAdresseArrivee = (EditText) findViewById(R.id.editTextAdresseArrivee);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        final AuthData authData = myFireBase.getAuth();

        Random rdm = new Random();

        if (
            //Si l'un des champs est vide, on demande à l'utilisateur de tous les remplir
                textVilleDepart.getText().toString().equals("") ||
                        textAdresseDepart.getText().toString().equals("") ||
                        textVilleArrivee.getText().toString().equals("") ||
                        textAdresseArrivee.getText().toString().equals("")
                ) {
            //Un Toast est un petit rectangle aux bords arrondis gris avec un texte à l'intérieur
            Toast.makeText(getApplicationContext(), "Merci de renseigner tous les champs", Toast.LENGTH_LONG).show();
            return;
        }

        Trajet trajet = new Trajet(
                textVilleDepart.getText().toString(),
                textAdresseDepart.getText().toString(),
                textVilleArrivee.getText().toString(),
                textAdresseArrivee.getText().toString(),
                new Date(new Date().getYear(), new Date().getMonth(), new Date().getDay()).toString(),
                rdm.nextInt(5),
                authData.getUid()
        );

        Firebase trip = myFireBase.child("trips").child(Integer.toString(rdm.nextInt(Integer.MAX_VALUE)));


        //On envoie les données du nouveau trajet dans la base de données
        trip.setValue(trajet);

        Toast.makeText(getApplicationContext(), "Votre trajet a bien été proposé", Toast.LENGTH_LONG).show();

        final Intent intent = new Intent(this, Accueil.class);
        startActivity(intent);

    }

    public void clickEditTextDate(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date");
    }

    public void clickEditTextTime(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "Heure");
    }
}


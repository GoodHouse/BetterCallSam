package groupe22.bettercallsam;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.Calendar;
import java.util.Random;

public class ProposerTrajet extends AppCompatActivity
{
    static Activity thisAct = null;

    static EditText DateEdit;
    static EditText TempsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisAct = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposer_trajet);
        DateEdit = (EditText) findViewById(R.id.editTextDate);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Date");
            }
        });
        TempsEdit = (EditText) findViewById(R.id.editTextTemps);
        TempsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "Heure");
            }
        });

    }

    public void clickButtonProposer(View view) {
        final EditText textVilleDepart = (EditText) findViewById(R.id.editTextVilleDepart);
        final EditText textAdresseDepart = (EditText) findViewById(R.id.editTextAdresseDepart);
        final EditText textVilleArrivee = (EditText) findViewById(R.id.editTextVilleArrivee);
        final EditText textAdresseArrivee = (EditText) findViewById(R.id.editTextAdresseArrivee);
        final EditText textDate = (EditText) findViewById(R.id.editTextDate);
        final EditText textTemps = (EditText) findViewById(R.id.editTextTemps);

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

        Trajet trajet = new Trajet(textVilleDepart.getText().toString(),
                textAdresseDepart.getText().toString(),
                textVilleArrivee.getText().toString(),
                textAdresseArrivee.getText().toString(),
                textDate.getText().toString(),
                textTemps.getText().toString(),
                3,
                authData.getUid().toString()
                );

        Firebase trip = myFireBase.child("trips").child(Integer.toString(rdm.nextInt(Integer.MAX_VALUE)));


        //On envoie les données du nouveau trajet dans la base de données
        trip.setValue(trajet);

        Toast.makeText(getApplicationContext(), "Votre trajet a bien été proposé", Toast.LENGTH_LONG).show();

        final Intent intent = new Intent(this, Accueil.class);
        startActivity(intent);
    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month++;
            String mois = (month < 10) ? "0" + month : "" + month;
            String jour = (day < 10) ? "0" + day : "" + day;
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            m++;
            int d = c.get(Calendar.DAY_OF_MONTH);
            if(year <= y && month <= m && day < d){
                    Toast.makeText(thisAct , "La date ne peut pas être dans le passé", Toast.LENGTH_LONG).show();
            }
            else {
                DateEdit.setText(jour + "/" + mois + "/" + year);
            }
        }
    }


    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener
    {
        @Override
        public Dialog onCreateDialog (Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int heure = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this, heure, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int heureDuJour, int minute){
            String heure = (heureDuJour < 10) ? "0" + heureDuJour : "" + heureDuJour;
            String min = (minute < 10) ? "0" + minute : "" + minute;
            TempsEdit.setText(heure + ":" + min);
        }
    }
}
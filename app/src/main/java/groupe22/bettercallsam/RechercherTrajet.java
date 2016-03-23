package groupe22.bettercallsam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.*;

import android.location.*;

public class RechercherTrajet extends AppCompatActivity implements View.OnClickListener {

    static Activity thisAct = null;
    static EditText DateEdit;
    static EditText TempsEdit;
    static EditText nb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        thisAct = this;
        setContentView(R.layout.activity_rechercher_trajet);

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

        nb = (EditText) findViewById(R.id.editTextNbPlaces);
        nb.setOnClickListener(this);
    }


    public void clickButtonRechercher(View view) {
        final Intent intent = new Intent(this, AffichageTrajets.class);
        EditText editVilleDepart = (EditText) findViewById(R.id.editTextVilleDepart);
        EditText editAdresseDepart = (EditText) findViewById(R.id.editTextAdresseDepart);
        intent.putExtra("pointDep", editAdresseDepart.getText().toString() + ", " + editVilleDepart.getText().toString());

        EditText editTextVilleArrivee = (EditText) findViewById(R.id.editTextVilleArrivee);
        EditText editTextAdresseArrivee = (EditText) findViewById(R.id.editTextAdresseArrivee);
        intent.putExtra("pointArr", editTextAdresseArrivee.getText().toString() + ", " + editTextVilleArrivee.getText().toString());

        EditText dd = (EditText) findViewById(R.id.editTextDate);
        intent.putExtra("dateDep", dd.getText().toString());

        EditText nbP = (EditText) findViewById(R.id.editTextNbPlaces);
        intent.putExtra("nbPlaces", nbP.getText().toString());

        EditText h = (EditText) findViewById(R.id.editTextTemps);
        intent.putExtra("heure", h.getText().toString());

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        numberPickerDialog();

    }

    private void numberPickerDialog(){
        NumberPicker np = new NumberPicker(this);
        np.setMaxValue(4);
        np.setMinValue(1);

        NumberPicker.OnValueChangeListener myValChangedListener =
                new NumberPicker.OnValueChangeListener()
                {
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
                    {
                    nb.setText(""+newVal);
                    }
                };
        np.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(np);

        builder.setTitle("Nombre de places");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
        public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
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
            if (year <= y && month <= m && day < d) {
                Toast.makeText(thisAct, "La date ne peut pas être dans le passé", Toast.LENGTH_LONG).show();

            } else {
                DateEdit.setText(jour + "/" + mois + "/" + year);
            }
        }


    }


    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int heure = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, heure, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int heureDuJour, int minute) {
            String heure = (heureDuJour < 10) ? "0" + heureDuJour : "" + heureDuJour;
            String min = (minute < 10) ? "0" + minute : "" + minute;
            TempsEdit.setText(heure + ":" + min);
        }
    }

    public Boolean comparerTrajet(String pointDepart, String pointArrivee) {
        Geocoder gc = new Geocoder(getApplicationContext());

        try {
            List<Address> adTrajet1 = gc.getFromLocationName(pointDepart, 1);
            List<Address> adTrajet2 = gc.getFromLocationName(pointArrivee, 1);
            Address adT1 = adTrajet1.get(0);
            Address adT2 = adTrajet2.get(0);
            double lat1 = adT1.getLatitude();
            double long1 = adT1.getLongitude();
            double lat2 = adT2.getLatitude();
            double long2 = adT2.getLongitude();
            return lat1 >= lat2 + 0.004 && lat1 <= lat2 - 0.004 && long1 >= long2 + 0.004 && long1 <= long2 + 0.004;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}



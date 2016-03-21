package groupe22.bettercallsam;

import android.app.AlertDialog;
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
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class RechercherTrajet extends AppCompatActivity {

    static EditText DateEdit;
    static EditText TempsEdit;
    static EditText nb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechercher_trajet);
        DateEdit = (EditText) findViewById(R.id.editTextDate);
        DateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Date");
            }
        });
    }

    public void clickButtonRechercher(View view) {
        final Intent intent = new Intent(this, AffichageTrajets.class);
        EditText ed = (EditText) findViewById(R.id.editTextVilleDepart);
        intent.putExtra("villeDep", ed.getText().toString());
        //intent.putExtra("adresseDep",)
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
            DateEdit.setText(day + "/" + (month + 1) + "/" + year);
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
            TempsEdit.setText(heureDuJour + ":" + minute);
        }
    }
}
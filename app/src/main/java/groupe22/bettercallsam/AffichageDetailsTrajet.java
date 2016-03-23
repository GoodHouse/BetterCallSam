package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by castazer on 23/03/16.
 */
public class AffichageDetailsTrajet extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_trajet);

        Intent intent = getIntent();
        final String trajet = intent.getStringExtra("trajet");
        final String nbPlacesReservees = intent.getStringExtra("nombrePlaces");

        Toast.makeText(getBaseContext(), trajet, Toast.LENGTH_LONG).show();
        TextView tv = (TextView)findViewById(R.id.textViewDep);
        Button button = (Button) findViewById(R.id.boutonReserver);

        button.setText("Réserver " + nbPlacesReservees);
        tv.setText(trajet);
    }
}

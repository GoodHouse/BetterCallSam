package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RechercherTrajet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechercher_trajet);
    }

    public void clickButtonRechercher(View view){
        final Intent intent = new Intent(this, AffichageTrajets.class);
        startActivity(intent);
    }
}

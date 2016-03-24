package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MesTrajetsBoutons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_trajets_boutons);
    }

    public void clickPassager(View view) {
        Intent intent = new Intent(this, MesTrajets.class);
        intent.putExtra("type", "passager");
        startActivity(intent);
    }

    public void clickConducteur(View view) {
        Intent intent = new Intent(this, MesTrajets.class);
        intent.putExtra("type", "conducteur");
        startActivity(intent);
    }
}

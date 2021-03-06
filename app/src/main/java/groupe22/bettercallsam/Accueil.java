package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Accueil extends AppCompatActivity {

    Utilisateur user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On charge l'interface de l'activité
        setContentView(R.layout.activity_accueil);
        final Button buttonProposerTrajet = (Button) findViewById(R.id.buttonProposerTrajet);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        final Intent intentMain = new Intent(this, MainActivity.class);

        //On vérifie que l'utilisateur est bien connecté
        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    user = snapshot.child("users").child(authData.getUid()).getValue(Utilisateur.class);
                    if (!user.isEstConducteur()) {
                        buttonProposerTrajet.setEnabled(false);
                        buttonProposerTrajet.setVisibility(View.INVISIBLE);
                    } else {
                        buttonProposerTrajet.setEnabled(true);
                        buttonProposerTrajet.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        } else
            //S'il n'est pas connecté, on le redirige sur l'activité principale
            startActivity(intentMain);
    }

    public void clickButtonDeconnexion(View view) {
        Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        //On déconnecte l'utilisateur de la base
        myFireBase.unauth();

        //On le redirige vers l'activité principale
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickButtonRechercherTrajet(View view) {
        Intent intent = new Intent(this, RechercherTrajet.class);
        startActivity(intent);
    }

    public void clickButtonProposerTrajet(View view) {
        Intent intent = new Intent(this, ProposerTrajet.class);
        startActivity(intent);
    }

    public void clickButtonMesTrajets(View view) {
        if (user.isEstConducteur()) {
            Intent intent = new Intent(this, MesTrajetsBoutons.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MesTrajets.class);
            intent.putExtra("type", "passager");
            startActivity(intent);
        }
    }

}

package groupe22.bettercallsam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;


import groupe22.bettercallsam.Utilisateur;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        final Button buttonProposerTrajet = (Button) findViewById(R.id.buttonProposerTrajet);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        final Intent intentMain = new Intent(this, MainActivity.class);

        final AuthData authData = myFireBase.getAuth();
        if(authData != null){
            myFireBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Utilisateur user = postSnapshot.child(authData.getUid()).getValue(Utilisateur.class);
                        if (!user.isEstConducteur()) {
                            buttonProposerTrajet.setEnabled(false);
                            buttonProposerTrajet.setVisibility(View.INVISIBLE);
                        } else {
                            buttonProposerTrajet.setEnabled(true);
                            buttonProposerTrajet.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
        else
            startActivity(intentMain);


    }

    public void clickButtonDeconnexion(View view){
        Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        myFireBase.unauth();
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickButtonRechercherTrajet(View view){
        Intent intent = new Intent(this, RechercherTrajet.class);
        startActivity(intent);
    }

}

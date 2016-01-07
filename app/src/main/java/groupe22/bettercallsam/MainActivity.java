package groupe22.bettercallsam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        Intent intent = getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickButtonInscription(View view) {
        Intent intent = new Intent(this, Inscription.class);
        startActivity(intent);
    }

    public void clickButtonConnexion(View view) {
        final Intent intent = new Intent(this, Accueil.class);
        final EditText textMailCo = (EditText) findViewById(R.id.textMailCo);
        final EditText textMDPCo = (EditText) findViewById(R.id.textMDPCo);
        final String email, motDePasse;

        textMailCo.setText("adrq@email.fr");
        textMDPCo.setText("pass");


        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        final Snackbar snackbarSuccess = Snackbar.make(view, "Bienvenue", Snackbar.LENGTH_LONG);
        final Snackbar snackbarError = Snackbar.make(view, "On ne vous connait pas", Snackbar.LENGTH_LONG);
        final Snackbar snackbarChampVide = Snackbar.make(view, "Merci de renseigner les deux champs", Snackbar.LENGTH_LONG);
        email = textMailCo.getText().toString();
        motDePasse = textMDPCo.getText().toString();

        if (email.equals("") || motDePasse.equals("")) {
            snackbarChampVide.show();
        } /*else {*/

            myFireBase.authWithPassword(email, motDePasse, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(final AuthData authData) {
                    snackbarSuccess.show();

                    startActivity(intent);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    snackbarError.show();
                }
            });
        //}


    }

    public void clickMDPOublie(View view) {
        final Button button = (Button) findViewById(R.id.buttonConnexion);
        button.setText("Ca marche");
    }
}

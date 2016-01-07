package groupe22.bettercallsam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
        Intent intentAccueil = new Intent(this, Accueil.class);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            startActivity(intentAccueil);
        }
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
        final Intent intentAccueil = new Intent(this, Accueil.class);
        final Intent intentInscription = new Intent(this, Inscription.class);
        final EditText textMailCo = (EditText) findViewById(R.id.textMailCo);
        final EditText textMDPCo = (EditText) findViewById(R.id.textMDPCo);
        final String email, motDePasse;

        textMailCo.setText("adrq@email.fr");
        textMDPCo.setText("pass");


        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        final Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        email = textMailCo.getText().toString();
        motDePasse = textMDPCo.getText().toString();

        if (email.equals("") || motDePasse.equals("")) {
            snackbar.setText("Merci de renseigner les deux champs");
            snackbar.show();
            return;
        } /*else {*/

            myFireBase.authWithPassword(email, motDePasse, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(final AuthData authData) {
                    startActivity(intentAccueil);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    switch (firebaseError.getCode()) {
                        case FirebaseError.INVALID_EMAIL:
                            alertDialog.setTitle("Erreur");
                            alertDialog.setMessage("Cet utilisateur n'est pas connu, veuillez vous inscrire");
                            alertDialog.setIcon(R.drawable.icon);
                            alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "S'inscrire", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intentInscription);
                                }
                            });
                            alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Réessayer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.cancel();
                                }
                            });
                            alertDialog.show();
                            break;
                        case FirebaseError.INVALID_PASSWORD:
                            snackbar.setText("Mot de passe incorrect");
                            snackbar.show();
                            break;
                        default:
                            snackbar.setText("Une erreur est survenue, veuillez réessayer");
                            snackbar.show();
                            break;
                    }
                }
            });
        //}


    }

    public void clickMDPOublie(View view) {
        final EditText textMailCo = (EditText) findViewById(R.id.textMailCo);
        final String email;

        final Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        email = textMailCo.getText().toString();

        if (email.equals("")) {
            snackbar.setText("Veuillez remplir la case email");
            snackbar.show();
            return;
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Mot de passe oublié");
        alertDialog.setMessage("Voulez vous envoyer le mail de récupération de mot de passe à " + email + " ?");
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
                myFireBase.resetPassword(email, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        snackbar.setText("Email envoyé");
                        snackbar.show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_EMAIL:
                                snackbar.setText("Cet email est incorrect");
                                snackbar.show();
                                break;
                            default:
                                snackbar.setText("Une erreur est survenue, veuillez réessayer");
                                snackbar.show();
                                break;
                        }
                    }
                });
            }
        });
        alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                return;
            }
        });
        alertDialog.show();
    }
}

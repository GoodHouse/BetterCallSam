package groupe22.bettercallsam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        //Si l'utilisateur n'est pas déconnecté, on lui signale puis on le dirige directement vers l'activité d'accueil
        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            Toast.makeText(getApplicationContext(), "Vous n'étiez pas déconnecté", Toast.LENGTH_LONG).show();
            startActivity(intentAccueil);
        }
    }


    //INUTILE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //INUTILE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    //Si l'utilisateur clique sur le bouton d'inscription, on le dirige directement sur l'activité d'inscription
    public void clickButtonInscription(View view) {
        Intent intent = new Intent(this, Inscription.class);
        startActivity(intent);
    }

    //Si l'utilisateur clique sur le bouton de connexion ;
    public void clickButtonConnexion(View view) {

        //On charge les deux activités qui peuvent être utiles : Accueil et Inscription
        final Intent intentAccueil = new Intent(this, Accueil.class);
        final Intent intentInscription = new Intent(this, Inscription.class);

        //On charge les éléments de l'interface utiles à la connexion
        final EditText textMailCo = (EditText) findViewById(R.id.textMailCo);
        final EditText textMDPCo = (EditText) findViewById(R.id.textMDPCo);
/*
        textMailCo.setText("adrq@email.fr");
        textMDPCo.setText("pass");
*/

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        //Un alertDialog est une boite avec un titre, un texte et un ou plusieurs boutons
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        //On teste si l'utilisateur a bien rempli les deux champs
        if (textMailCo.getText().toString().equals("") || textMDPCo.getText().toString().equals("")) {
            //S'il ne les a pas rempli, on lui demande de le faire
            Toast.makeText(getApplicationContext(), "Merci de renseigner les deux champs", Toast.LENGTH_LONG).show();
            return;
        } else {

            //On essaye de connecter l'utilisateur à la base de données
            myFireBase.authWithPassword(textMailCo.getText().toString(), textMDPCo.getText().toString(), new Firebase.AuthResultHandler() {

                //Si on y arrive, on le redirige directement sur l'activité d'accueil
                @Override
                public void onAuthenticated(final AuthData authData) {
                    startActivity(intentAccueil);
                }

                //Si on n'y arrive pas
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                    //On récupère le code d'erreur
                    switch (firebaseError.getCode()) {

                        //Si l'email est incorrect, on propose à l'utilisateur de s'inscrire
                        //Il peut soit dire "Réessayer", s'il a fait une erreur de saisie, soit dire "S'incrire" s'il veut s'incrire
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
                            //Si le mot de passe est incorrect, on lui signale
                            Toast.makeText(getApplicationContext(), "Mot de passe incorrect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            //On ne sait pas quelle erreur est apparue, donc on dit à l'utilisateur de réessayer
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
            }


        }

    //Si l'utilisateur clique sur le texte "Mot de passe oublié"
    public void clickMDPOublie(View view) {
        final EditText textMailCo = (EditText) findViewById(R.id.textMailCo);

        //On vérifie que l'utilisateur a bien saisi une adresse email
        if (textMailCo.getText().toString().equals("")) {
            //S'il n'a pas saisi d'adresse, on lui demande de le faire
            Toast.makeText(getApplicationContext(), "Veuillez remplir la case email", Toast.LENGTH_LONG).show();
            return;
        }

        //Si une adresse a été saisie
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        //On demande à l'utilisateur si l'adresse est correcte
        alertDialog.setTitle("Mot de passe oublié");
        alertDialog.setMessage("Voulez vous envoyer le mail de récupération de mot de passe à " + textMailCo.getText().toString() + " ?");
        alertDialog.setIcon(R.drawable.icon);
        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

                //Si elle est correcte et que l'utilisateur clique sur le bouton "Oui"
                //On lui envoie un email de récupération de mot de passe
                myFireBase.resetPassword(textMailCo.getText().toString(), new Firebase.ResultHandler() {

                    //Si aucune erreur ne s'est produite, on lui signale qu'un email a été envoyé
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Email envoyé", Toast.LENGTH_LONG).show();
                    }


                    //Si une erreur est sruvenue, on lui signale
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        switch (firebaseError.getCode()) {

                            //Si l'email n'est pas correct
                            case FirebaseError.INVALID_EMAIL:
                                Toast.makeText(getApplicationContext(), "Email incorrect", Toast.LENGTH_LONG).show();
                                break;

                            //Dans tout autre cas que l'on ne peut pas différencier
                            default:
                                Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        });

        //Si l'utilisateur s'est trompé dans la saisie de l'email, il clique sur "Non"
        //On ferme alors l'alertDialog pour qu'il puisse saisir correctement son adresse
        alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                return;
            }
        });
        alertDialog.show();
    }
}

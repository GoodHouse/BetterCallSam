package groupe22.bettercallsam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Hashtable;
import java.util.Map;

public class Inscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Firebase.setAndroidContext(this);

        Intent intent = getIntent();
    }

    public void onClickConducteur(View view) {
        final CheckBox checkPassager = (CheckBox) findViewById(R.id.checkPassager);
        final CheckBox checkConducteur = (CheckBox) findViewById(R.id.checkConducteur);

        if (!checkPassager.isChecked()) {
            if (checkConducteur.isChecked()) {
                checkPassager.setChecked(true);
            }
        }
    }

    public void clickInscription(View view) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        final EditText textEMail = (EditText) findViewById(R.id.textEMail);
        final EditText textMotDePasse = (EditText) findViewById(R.id.textMotDePasse);
        final EditText textNom = (EditText) findViewById(R.id.textNom);
        final EditText textPrenom = (EditText) findViewById(R.id.textPrenom);
        final EditText textPhone = (EditText) findViewById(R.id.textPhone);
        final CheckBox checkConducteur = (CheckBox) findViewById(R.id.checkConducteur);

        final Intent intent = new Intent(this, MainActivity.class);

        final String email, motDePasse;

        if(
                textEMail.getText().toString().equals("") ||
                textMotDePasse.getText().toString().equals("") ||
                textNom.getText().toString().equals("") ||
                textPrenom.getText().toString().equals("") ||
                textPhone.getText().toString().equals("")
          ){
            Toast.makeText(getApplicationContext(), "Merci de renseigner tous les champs", Toast.LENGTH_LONG).show();
            return;
        }

        email = textEMail.getText().toString();
        motDePasse = textMotDePasse.getText().toString();


        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        myFireBase.createUser(textEMail.getText().toString(), textMotDePasse.getText().toString(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                myFireBase.authWithPassword(email, motDePasse, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        String nom = textNom.getText().toString();
                        String prenom = textPrenom.getText().toString();
                        int numero = Integer.parseInt(textPhone.getText().toString());
                        boolean estConducteur = checkConducteur.isChecked();
                        Firebase user = myFireBase.child("users").child(authData.getUid());
                        Utilisateur utilisateur = new Utilisateur(nom, prenom, numero, estConducteur);
                        user.setValue(utilisateur);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setTitle("Inscription");
                alertDialog.setMessage("Vous avez bien été inscrit");
                alertDialog.setIcon(R.drawable.icon);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
            }
        });
    }


}

package groupe22.bettercallsam;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

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
        final Snackbar snackbarError = Snackbar.make(view, "Il y a eu un problème lors de l'inscritpion", Snackbar.LENGTH_LONG);
        final EditText textEMail = (EditText) findViewById(R.id.textEMail);
        final EditText textMotDePasse = (EditText) findViewById(R.id.textMotDePasse);
        final Intent intent = new Intent(this, MainActivity.class);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText textNom = (EditText) findViewById(R.id.textNom);
        final EditText textPrenom = (EditText) findViewById(R.id.textPrenom);
        final EditText textPhone = (EditText) findViewById(R.id.textPhone);
        final CheckBox checkConducteur = (CheckBox) findViewById(R.id.checkConducteur);
        final String email, motDePasse;
        if(textEMail.equals("") || textMotDePasse.equals("") || textNom.equals("") || textPrenom.equals("") || textPhone.equals("")){
            snackbarError.show();
        }

        email = textEMail.getText().toString();
        motDePasse = textMotDePasse.getText().toString();


        Firebase.setAndroidContext(this);
        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");
        myFireBase.createUser(textEMail.getText().toString(), textMotDePasse.getText().toString(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                myFireBase.authWithPassword(email, motDePasse, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Map<String, Object> map = new Hashtable<String, Object>();
                        map.put("nom", textNom.getText().toString());
                        map.put("prenom", textPrenom.getText().toString());
                        map.put("numero", textPhone.getText().toString());
                        map.put("estConducteur", checkConducteur.isChecked());
                        myFireBase.child("users").child(authData.getUid()).setValue(map);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        snackbarError.show();
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
                snackbarError.show();
            }
        });
    }


}

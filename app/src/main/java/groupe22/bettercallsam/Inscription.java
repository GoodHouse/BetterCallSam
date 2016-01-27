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
    }

    //Un conducteur EST UN PASSAGER mais un passager n'est pas forcément conducteur
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
        //On récupère tous les éléments utiles de l'interface pour pouvoir récupérer les textes entrés
        final EditText textEMail = (EditText) findViewById(R.id.textEMail);
        final EditText textMotDePasse = (EditText) findViewById(R.id.textMotDePasse);
        final EditText textNom = (EditText) findViewById(R.id.textNom);
        final EditText textPrenom = (EditText) findViewById(R.id.textPrenom);
        final EditText textPhone = (EditText) findViewById(R.id.textPhone);
        final CheckBox checkConducteur = (CheckBox) findViewById(R.id.checkConducteur);

        //On prépare la redirection vers l'activité principale
        final Intent intent = new Intent(this, MainActivity.class);

        if(
                //Si l'un des champs est vide, on demande à l'utilisateur de tous les remplir
                textEMail.getText().toString().equals("") ||
                textMotDePasse.getText().toString().equals("") ||
                textNom.getText().toString().equals("") ||
                textPrenom.getText().toString().equals("") ||
                textPhone.getText().toString().equals("")
          ){
            //Un Toast est un petit rectangle aux bords arrondis gris avec un texte à l'intérieur
            Toast.makeText(getApplicationContext(), "Merci de renseigner tous les champs", Toast.LENGTH_LONG).show();
            return;
        }

        //Connexion à la base de données
        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        //On crée un nouvel utilisateur dans la base
        myFireBase.createUser(textEMail.getText().toString(), textMotDePasse.getText().toString(), new Firebase.ResultHandler() {
            @Override
            //Si le nouvel utilisateur a bien été crée
            public void onSuccess() {
                //On se connecte grâce à son compte
                myFireBase.authWithPassword(textEMail.getText().toString(), textMotDePasse.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    //Si la connexion a fonctionné
                    public void onAuthenticated(AuthData authData) {
                        //On se place où il faut dans la base de données
                        Firebase user = myFireBase.child("users").child(authData.getUid());

                        //On crée un nouvel utilisateur, puis on complete ses attributs
                        Utilisateur utilisateur = new Utilisateur();
                        utilisateur.setNom(textNom.getText().toString());
                        utilisateur.setPrenom(textPrenom.getText().toString());
                        utilisateur.setNumero(Integer.parseInt(textPhone.getText().toString()));
                        utilisateur.setEstConducteur(checkConducteur.isChecked());

                        //On envoie les données du nouvel utilisateur dans la base de données
                        user.setValue(utilisateur);
                    }

                    //S'il y a eu une erreur durant l'authentification
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                    }
                });
                //On signale à l'utilisateur que son inscription est un succès
                Toast.makeText(getApplicationContext(), "Vous avez bien été inscrit", Toast.LENGTH_LONG).show();
                //On redirige l'utilisateur vers l'activité principale
                startActivity(intent);
            }

            @Override
            //Si l'utilisateur n'a pas pu être crée
            public void onError(FirebaseError firebaseError) {
                switch(firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        Toast.makeText(getApplicationContext(), "Cet email est déjà utilisé", Toast.LENGTH_LONG).show();
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        Toast.makeText(getApplicationContext(), "Ce mot de passe est incorrect", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Une erreur est survenue, veuillez réessayer", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


}

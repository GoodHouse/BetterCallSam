package groupe22.bettercallsam;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AffichageTrajets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_trajets);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);


        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        final Firebase myFireBase = new Firebase("https://bettercallsam.firebaseio.com/");

        final AuthData authData = myFireBase.getAuth();
        if (authData != null) {
            myFireBase.addValueEventListener(new ValueEventListener() {
                //S'il est connecté, on récupère ses informations
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    snapshot = snapshot.child("trips");
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        postSnapshot.getValue();
                        Trajet trajet = postSnapshot.getValue(Trajet.class);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        } else
            //S'il n'est pas connecté, on le redirige sur l'activité principale
            startActivity(new Intent(this, MainActivity.class));
    }
}


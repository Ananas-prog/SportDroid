package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Accueil extends AppCompatActivity {
    private String role;
    private Button athlete;
    private Button coach;
    private TextView mdp;
    private Button valider;
    private LinearLayout layout;
    private String MDPDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        layout=(LinearLayout)findViewById(R.id.layoutmdp);
        mdp=(TextView)findViewById(R.id.editTextTextPassword);
        athlete=(Button) findViewById(R.id.buttonAthlete);
        coach=(Button) findViewById(R.id.buttonCoach);
        valider=(Button)findViewById(R.id.buttonValiderMDP);

        //permet de savoir le role de la personne pour faire oui ou non les les boutons d'ajout
        athlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role="athlete";
                Intent intent = new Intent(Accueil.this, MainActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_top,R.anim.slide_out_top);

            }
        });

        coach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.VISIBLE);
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("mdp");
                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MDPDataBase = dataSnapshot.getValue(String.class);
                        if(mdp.getText().toString().equals(MDPDataBase)){
                            role="coach";
                            Intent intent = new Intent(Accueil.this, MainActivity.class);
                            intent.putExtra("role", role);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_top,R.anim.slide_out_top);

                        }else{
                            Toast.makeText(Accueil.this,"mdp érroné ",Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }
        });



    }
}
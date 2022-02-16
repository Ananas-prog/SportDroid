package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonEntrainement;
    private Button Buttonhome;
    private Button ButtonAjouterEntrainement;
    public ArrayList<Object> listeEntrainement=new ArrayList<>();
    public ArrayList<activite> activite= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonAjouterEntrainement =(Button) findViewById(R.id.ButtonAjouterEntrainement);
        ButtonAjouterEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterEntrainement();
            }
        });

        Buttonhome =(Button) findViewById(R.id.ButtonHome);
        Buttonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();
            }
        });
        ButtonCalendrier = (Button) findViewById(R.id.ButtonCalendrier);
        ButtonCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calender();
            }
        });

        ButtonEntrainement= (Button) findViewById(R.id.ButtonEntrainement);
        ButtonEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrainement();
            }
        });





        ListView listView=(ListView) findViewById(R.id.listViewPrincipal);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"clique sur l 'item "+ i+ ""+activite.get(i).toString(),Toast.LENGTH_LONG).show();


            }
        });






    }
    public void rafraichissementListe(){

        ListView listView=(ListView) findViewById(R.id.listViewPrincipal);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,activite);
        listView.setAdapter(blockAdapter);
        // Write a message to the database
    }
    public void ajouterEntrainement(){
        Intent intent = new Intent(this, ajout_entrainement.class);
        startActivity(intent);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        activite un = new activite("running","16/03/2020");
        activite.add(un);
        rafraichissementListe();


        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    public void home(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void calender(){
        Intent intent = new Intent(this, calendrier.class);
        startActivity(intent);
    }
    public void entrainement(){
        Intent intent = new Intent(this, entrainement.class);
        startActivity(intent);
    }
}
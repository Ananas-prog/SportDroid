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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


class activite implements Serializable {
    private String typeDeSport;
    private String date;
    //private ArrayList<detaille_entrainement> block=new ArrayList<>();

    public activite(String typeDeSport,String date) {
        this.typeDeSport=typeDeSport;
        this.date=date;
        // block= new ArrayList<detaille_entrainement>("Echauffement","aucune","Temps",30);
    }
    public String getDate() {
        return date;
    }
    public String getTypeDeSport() {
        return typeDeSport;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTypeDeSport(String typeDeSport) {
        this.typeDeSport = typeDeSport;
    }
    public String toString(){
        return  this.typeDeSport+"  "+ this.date;
    }

}
public class MainActivity extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonEntrainement;
    private Button Buttonhome;
    private Button ButtonAjouterEntrainement;
    public ArrayList<Object> listeEntrainement=new ArrayList<>();
    public ArrayList<activite> tabActivite= new ArrayList<>();


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
                Toast.makeText(MainActivity.this,"clique sur l 'item "+ i+ ""+tabActivite.get(i).toString(),Toast.LENGTH_LONG).show();


            }
        });




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite/0");

        //myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String sport = String.valueOf(dataSnapshot.child("/typeDeSport").getValue());
                        String date = String.valueOf(dataSnapshot.child("/date").getValue());
                        tabActivite.clear();
                        tabActivite.add(new activite(sport, date));
                        rafraichissementListe();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });








    }
    public void rafraichissementListe(){

        ListView listView=(ListView) findViewById(R.id.listViewPrincipal);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,tabActivite);
        listView.setAdapter(blockAdapter);
        // Write a message to the database
    }
    public void ajouterEntrainement(){
        //activiter par default
        activite un = new activite("running","16/03/2020");
        tabActivite.add(un);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite/"+tabActivite.size());
        myRef.setValue(un);
        rafraichissementListe();

        Intent intent = new Intent(this, ajout_entrainement.class);
        intent.putExtra("nEntrainement", String.valueOf(tabActivite.size()));
        startActivity(intent);




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
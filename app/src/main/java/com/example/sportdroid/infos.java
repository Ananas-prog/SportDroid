package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

class info extends AppCompatActivity{
    private String image;
    private String titre;
    private String date;
    private String lien;
    private String paragraphe;
    private String lieu;

    public info(String image, String titre, String date, String lien, String paragraphe, String lieu){
        this.image=image;
        this.titre=titre;
        this.date=date;
        this.lien=lien;
        this.paragraphe=paragraphe;
        this.lieu=lieu;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }
    public String getImage() {
        return image;
    }
    public String getLien() {
        return lien;
    }
    public String getParagraphe() {
        return paragraphe;
    }
    public String getTitre() {
        return titre;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setLien(String lien) {
        this.lien = lien;
    }
    public void setParagraphe(String paragraphe) {
        this.paragraphe = paragraphe;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getLieu() {
        return lieu;
    }
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    public String toString(){
        return  this.image+"  "+ this.titre+"  "+ this.date+"  "+ this.lien+"  "+ this.paragraphe + " "+this.lieu;
    }
}


public class infos extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonInfos;
    private Button Buttonhome;
    public ArrayList<info> listeInfo=new ArrayList<>();
    private ImageButton Bajouter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);
        Bajouter = (ImageButton) findViewById(R.id.imageButton) ;
        Bajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterInfo();
            }
        });

        ListView listView=(ListView) findViewById(R.id.listViewInfos);
        //listeInfo.add(new info("nj","nj","nkj","ni","nj"));
        //rafraichissementListe();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(infos.this,"clique sur l 'item "+ i,Toast.LENGTH_LONG).show();
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("infos");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listeInfo.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // String value = ds.getValue(String.class);
                    String image = String.valueOf(ds.child("/image").getValue());
                    String titre = String.valueOf(ds.child("/titre").getValue());
                    String date = String.valueOf(ds.child("/date").getValue());
                    String lien = String.valueOf(ds.child("/lien").getValue());
                    String paragraphe = String.valueOf(ds.child("/paragraphe").getValue());
                    String lieu = String.valueOf(ds.child("/lieu").getValue());


                    info element = new info(image, titre,date,lien,paragraphe,lieu);
                    listeInfo.add(element);
                    // RECUPERER LE CODE DANS CALENDRIER
                    rafraichissementListe();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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

        ButtonInfos= (Button) findViewById(R.id.ButtonInfos);
        ButtonInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informations();
            }
        });



    }
    public void rafraichissementListe(){

        ListView listView=(ListView) findViewById(R.id.listViewInfos);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listeInfo);
        listView.setAdapter(blockAdapter);
        // Write a message to the database
    }
    public void home(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void calender(){
        Intent intent = new Intent(this, calendrier.class);
        startActivity(intent);
    }
    public void informations(){
       // Intent intent = new Intent(this, entrainement.class);
        //startActivity(intent);
    }

    public void ajouterInfo(){
        info un = new info("","","","","","");
        listeInfo.add(un);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ajout = database.getReference("infos/"+listeInfo.size());
        //ajout.setValue(un);
        rafraichissementListe();

        Intent intent = new Intent(infos.this, ajout_infos.class);
        intent.putExtra("nInfos", String.valueOf(listeInfo.size()));
        startActivity(intent);
    }
    }


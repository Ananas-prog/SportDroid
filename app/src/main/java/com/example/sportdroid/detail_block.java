package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class detail_block extends AppCompatActivity {


    Spinner spinnerEtape;
    Spinner spinnerDuree;

    EditText note;
    EditText duree;

    public int i;
    private EditText TexteDuree;
    public String nActivite;
    public String nBlock;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_block);

        Intent intent = getIntent();
        if (intent.hasExtra("nActivite")){ // vérifie qu'une valeur est associée à la clé “edittext”
            nActivite = intent.getStringExtra("nActivite"); // on récupère la valeur associée à la clé
            nBlock = intent.getStringExtra("nBlock"); // on récupère la valeur associée à la clé
        }


// SPINNER Etape
        spinnerEtape = (Spinner) findViewById(R.id.typeEtape);
        List typeEtape = new ArrayList();
        typeEtape.add("Echauffement");
        typeEtape.add("Course");
        typeEtape.add("Etirement");
        typeEtape.add("Repos");
        typeEtape.add("Récupérer");
        typeEtape.add("Autre");


        ArrayAdapter adapterEtape = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                typeEtape
        );
        adapterEtape.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtape.setAdapter(adapterEtape);




//SPINNER TYPE DE DUREE
        spinnerDuree = (Spinner) findViewById(R.id.typeDuree);
        List typeDureee = new ArrayList();
        typeDureee.add("Temps");
        typeDureee.add("Distance");

        ArrayAdapter adapterDuree = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                typeDureee
        );
        adapterDuree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDuree.setAdapter(adapterDuree);


    }

    public void valider(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.duree =(EditText) findViewById(R.id.intduree);
        DatabaseReference test = database.getReference("activite/"+nActivite+"/block/"+nBlock+"/duree");
        test.setValue(this.duree.getText().toString());

        this.note =(EditText) findViewById(R.id.texteEtape);
        DatabaseReference note = database.getReference("activite/"+nActivite+"/block/"+nBlock+"/notes");
        note.setValue(this.note.getText().toString());

        DatabaseReference typeDetape = database.getReference("activite/"+nActivite+"/block/"+nBlock+"/typeDetape");
        typeDetape.setValue(spinnerEtape.getSelectedItem().toString());

        DatabaseReference typeDuree = database.getReference("activite/"+nActivite+"/block/"+nBlock+"/typeDeDuree");
        typeDuree.setValue(spinnerDuree.getSelectedItem().toString());
        finish();

    }
}
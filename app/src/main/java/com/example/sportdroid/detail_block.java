package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class detail_block extends AppCompatActivity {


    Spinner spinnerEtape;
    Spinner spinnerDuree;

    EditText note;
    EditText duree;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_block);
        Intent intent = getIntent();

        //String monString = intent.getStringExtra("case");
        

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


               //str. settypeDetape(settypeDetape(spinnerEtape.getSelectedItem().toString() ));


        // TEXTEFILED NOTE

        note = (EditText) findViewById (R.id.texteEtape);
        String TexteNote = note.getText().toString();


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

        //t1.settypeDetape(spinnerEtape.getSelectedItem().toString() );



        // TEXTEFILED duree

        duree = (EditText) findViewById (R.id.texteDuree);
        String TexteDuree = duree.getText().toString();

    }

    public void valider(View view){
        Toast.makeText(detail_block.this,"valider",Toast.LENGTH_LONG).show();
        finish();
    }
}
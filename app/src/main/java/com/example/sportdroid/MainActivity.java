package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonEntrainement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    }

    public void calender(){
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }
    public void entrainement(){
        Intent intent = new Intent(this, entrainement.class);
        startActivity(intent);
    }
}
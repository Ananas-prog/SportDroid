package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class infos extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonInfos;
    private Button Buttonhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);
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
    }


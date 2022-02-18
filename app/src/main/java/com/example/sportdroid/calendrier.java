package com.example.sportdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class calendrier extends AppCompatActivity {
    private Button ButtonCalendrier;
    private Button ButtonEntrainement;
    private Button Buttonhome;

    CalendarView calendar;
    TextView date;
    ListView listView;
    public ArrayList<activite> tabActivite= new ArrayList<>();
    public String DateSelectionner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);




        calendar=(CalendarView)findViewById(R.id.calendarView);
        date =(TextView) findViewById(R.id.textDate);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String dateSelectioner=getMonthFormar(i1+1)+" "+i2+" "+i;
                DateSelectionner=dateSelectioner;
                date.setText(dateSelectioner);


            }

        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tabActivite.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // String value = ds.getValue(String.class);
                    String date = String.valueOf(ds.child("/date").getValue());
                    if(date.equals(date)) {
                        String sport = String.valueOf(ds.child("/typeDeSport").getValue());
                        //String date = String.valueOf(ds.child("/date").getValue());
                        activite element = new activite(sport, date);
                        tabActivite.add(element);
                        rafraichissementListe();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





        listView=(ListView)findViewById(R.id.listeViewCalendrier);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(calendrier.this,"clique sur l 'item "+ i+ "",Toast.LENGTH_LONG).show();



            }
        });
















        // BOUTTON DE CHANGEMENT D ACTIVITE
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

    }


    public void rafraichissementListe(){

        ListView listView=(ListView) findViewById(R.id.listeViewCalendrier);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,tabActivite);
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
    public void entrainement(){
        Intent intent = new Intent(this, entrainement.class);
        startActivity(intent);
    }

    private String getMonthFormar(int month) {
        if(month==1)
            return "JAN";
        if(month==2)
            return "FEV";
        if(month==3)
            return "MAR";
        if(month==4)
            return "AVR";
        if(month==5)
            return "MAI";
        if(month==6)
            return "JUN";
        if(month==7)
            return "JUL";
        if(month==8)
            return "AUG";
        if(month==9)
            return "SEP";
        if(month==10)
            return "OCT";
        if(month==11)
            return "NOV";
        if(month==12)
            return "DEC";
        // default
        return "JAN";

    }




    }


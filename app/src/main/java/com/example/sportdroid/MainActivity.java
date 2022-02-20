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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    public ArrayList<activite> listeEntrainementActuelle=new ArrayList<>();
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
                //Toast.makeText(MainActivity.this,"clique sur l 'item "+ i+ ""+tabActivite.get(i).toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ajout_entrainement.class);
                intent.putExtra("nEntrainement", String.valueOf(i+1));
                startActivity(intent);

            }
        });

        TextView test = (TextView) findViewById(R.id.textView2);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite");

        //myRef.setValue("Hello, World!");
        Calendar cal= Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        int day =cal.get(Calendar.DAY_OF_MONTH);
        String dateActuelle = (getMonthFormar(month + 1) + " " + day + " " + year);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tabActivite.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                       // String value = ds.getValue(String.class);
                        String sport = String.valueOf(ds.child("/typeDeSport").getValue());
                        String date = String.valueOf(ds.child("/date").getValue());
                        activite element = new activite(sport, date);
                        tabActivite.add(element);
                       // String split[]=date.split(" ",0);

                        //test.setText(split[1]);
                        //split[0].equals(getMonthFormar(month))
                       // if(split[0].equals(String.valueOf(month))) {
                      //      tabActivite.add(element);
                       // }






                        rafraichissementListe();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });








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

    public void rafraichissementListe(){
        ListView listView=(ListView) findViewById(R.id.listViewPrincipal);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,tabActivite);
        listView.setAdapter(blockAdapter);
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
       // Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
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
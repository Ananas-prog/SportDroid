package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


class activite implements Serializable {
    private String typeDeSport;
    private String date;
    private String note;
    private String heure;
    private String lieu;

    //private ArrayList<detaille_entrainement> block=new ArrayList<>();

    public activite(String typeDeSport,String date, String note,String heure,String lieu) {
        this.typeDeSport=typeDeSport;
        this.date=date;
        this.note=note;
        this.heure=heure;
        this.lieu=lieu;
        // block= new ArrayList<detaille_entrainement>("Echauffement","aucune","Temps",30);
    }

    public String getHeure() {
        return heure;
    }
    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTypeDeSport() {
        return typeDeSport;
    }
    public void setTypeDeSport(String typeDeSport) {
        this.typeDeSport = typeDeSport;
    }
    public String getLieu() {
        return lieu;
    }

    public String toString(){
        return  this.typeDeSport+"  "+ this.date+ " "+ this.note+" "+this.heure+" "+this.lieu;
    }

}

public class MainActivity extends AppCompatActivity {

    private Button ButtonCalendrier;
    private Button ButtonInfos;
    private Button Buttonhome;
    private Button ButtonAjouterEntrainement;
    public ArrayList<activite> listeEntrainementJournee=new ArrayList<>();
    public ArrayList<activite> tabActivite= new ArrayList<>();
    private LinearLayout ajoutEntrainement;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ajoutEntrainement= (LinearLayout) findViewById(R.id.layoutAjoutEntrainement);
        Intent intent = getIntent();

        if (intent.hasExtra("role")){ // vérifie qu'une valeur est associée à la clé “edittext”
            role = intent.getStringExtra("role"); // on récupère la valeur associée à la clé
        }
        if(role.equals("athlete")){
            ajoutEntrainement.setVisibility(View.INVISIBLE);
        }

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

        ButtonInfos= (Button) findViewById(R.id.ButtonInfos);
        ButtonInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informations();
            }
        });

        //TextView test = (TextView) findViewById(R.id.textView2);



        //myRef.setValue("Hello, World!");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateActuelle = (day + " " + getMonthFormar(month + 1) + " " + year);

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
                        String sport = String.valueOf(ds.child("/typeDeSport").getValue());
                        String date = String.valueOf(ds.child("/date").getValue());
                        String note = String.valueOf(ds.child("/note").getValue());
                        String heure = String.valueOf(ds.child("/heure").getValue());
                        String lieu = String.valueOf(ds.child("/lieu").getValue());

                        activite element = new activite(sport, date,note,heure,lieu);
                        tabActivite.add(element);
                        listeEntrainementJournee.clear();
                        for (int j = 0; j < tabActivite.size(); j++) {
                            if(tabActivite.get(j).getDate().equals(dateActuelle)){
                                listeEntrainementJournee.add(tabActivite.get(j));
                                //rafraichissementListe();
                            }
                        }
                        if(listeEntrainementJournee.size()==0){
                            listeEntrainementJournee.clear();
                            //rafraichissementListe();
                        }

                        // RECUPERER LE CODE DANS CALENDRIER
                        rafraichissementListe();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        ListView listView_EntrainementToday=(ListView) findViewById(R.id.listViewPrincipal);
        listView_EntrainementToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"clique sur l 'item "+ i+ ""+tabActivite.get(i).toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ajout_entrainement.class);
                intent.putExtra("nEntrainement", String.valueOf(i+1));
                startActivity(intent);

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
        listView.setAdapter(null);
        listView.setAdapter(new listViewAdapterHome(this, listeEntrainementJournee));
       // ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,listeEntrainementJournee);
        //listView.setAdapter(blockAdapter);
    }
    public void ajouterEntrainement(){
        //activiter par default
        activite un = new activite("","","","","");
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
        intent.putExtra("role", role);
        startActivity(intent);
    }
    public void informations(){
        Intent intent = new Intent(this, infos.class);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
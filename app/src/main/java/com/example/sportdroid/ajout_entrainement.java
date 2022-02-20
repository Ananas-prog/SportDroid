package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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


class detaille_entrainement implements Serializable{
    private String typeDetape;
    private String notes;
    private String typeDeDuree;
    private String duree;



    public detaille_entrainement(String typeDetape, String notes, String typeDeDuree, String duree){
        this.typeDetape=typeDetape;
        this.notes=notes;
        this.typeDeDuree=typeDeDuree;
        this.duree=duree;
    }

    public void settypeDetape(String typeDetape) {
        this.typeDetape = typeDetape;
    }
    public String gettypeDetape() {
        return typeDetape;
    }
    public void setnotes(String notes) {
        this.notes = notes;
    }
    public String getnotes() {
        return notes;
    }
    public void settypeDeDuree(String typeDeDuree) {
        this.typeDeDuree = typeDeDuree;
    }
    public String gettypeDeDuree() {
        return typeDeDuree;
    }
    public void setduree(String duree) {
        this.duree = duree;
    }
    public String getduree() {
        return duree;
    }
    public String toString(){
        return  this.typeDetape+"  "+ this.notes+"  "+ this.typeDeDuree+"  "+ this.duree;
    }
}
public class ajout_entrainement extends AppCompatActivity {

    private static final String TAG = "";
    public DatePickerDialog datePickerDialog;
    public Button dateButton;
    Spinner spinner;
    //ListView listView;
    public ArrayList<detaille_entrainement> block=new ArrayList<detaille_entrainement>();
    public DatabaseReference databaseReference;
    public String str = "";



    private TextView test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_entrainement);
        
        TextView t1 = (TextView) findViewById(R.id.textView);
        ListView listView=(ListView) findViewById(R.id.listView);
        Button supprimerActiviter=(Button)findViewById(R.id.buttonSupprimer);
        Intent intent = getIntent();
        if (intent.hasExtra("nEntrainement")){ // vérifie qu'une valeur est associée à la clé “edittext”
            str = intent.getStringExtra("nEntrainement"); // on récupère la valeur associée à la clé
        }


        //Récupération du Spinner déclaré dans le fichier main.xml de res/layout
        spinner = (Spinner) findViewById(R.id.typeDeSport);
        //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
        List exempleList = new ArrayList();
        exempleList.add("Natation");
        exempleList.add("Vélo");
        exempleList.add("Running");
        exempleList.add("Musculation");


        /*Le Spinner a besoin d'un adapter pour sa presentation alors on lui passe le context(this) et
                un fichier de presentation par défaut( android.R.layout.simple_spinner_item)
        Avec la liste des elements (exemple) */
        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                exempleList
        );


        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Enfin on passe l'adapter au Spinner et c'est tout
        spinner.setAdapter(adapter);

        // pour recuperer l'element selection a mettre dans le bouton de valider a la fin
        //t1.setText(spinner.getSelectedItem().toString() );

        initDatePicker();
        dateButton=findViewById(R.id.date_picker_Button);
        dateButton.setText(getTodaysDate());



        test=(TextView)findViewById(R.id.textView3);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ajout_entrainement.this,"clique sur l 'item "+ i+ ""+block.get(i).toString(),Toast.LENGTH_LONG).show();

                // pour modifier le block
                Intent intent = new Intent(ajout_entrainement.this, detail_block.class);
                intent.putExtra("nActivite",str);
                intent.putExtra("nBlock",String.valueOf(i));
                startActivity(intent);



               // startActivity(new Intent(ajout_entrainement.this,pop.class));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference blockr = database.getReference("activite/"+str+"/block");
                blockr.setValue(block);


            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("activite/"+str+"/block");
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                block.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // String value = ds.getValue(String.class);

                    String duree = String.valueOf(ds.child("/duree").getValue());
                    String notes = String.valueOf(ds.child("/notes").getValue());
                    String typeDeDuree = String.valueOf(ds.child("/typeDeDuree").getValue());
                    String typeDetape = String.valueOf(ds.child("/typeDetape").getValue());
                    detaille_entrainement element = new detaille_entrainement(typeDetape,notes,typeDeDuree,duree);
                    block.add(element);
                    rafraichissementListe();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





    }

    public void supprimerActivite(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("activite/1");
        myRef1.child("/date").removeValue();

    }

    public void rafraichissementListe(){

        ListView listView=(ListView) findViewById(R.id.listView);
        ArrayAdapter blockAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,block);
        listView.setAdapter(blockAdapter);
        // Write a message to the database
    }
    public void AjoutActivite(View view){
        detaille_entrainement un = new detaille_entrainement("recup","?","temps","15");
        block.add(un);
        rafraichissementListe();
    }

    private String getTodaysDate() {
        Calendar cal= Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        month=month+1;
        int day =cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date =makeDateString(day,month,year);
                dateButton.setText(date);
            }
        };
        Calendar cal= Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        int day =cal.get(Calendar.DAY_OF_MONTH);

        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog=new DatePickerDialog(this,style,dateSetListener,year,month,day);


    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormar(month)+" "+day+" "+year;
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

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    public void valider(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference typeSport = database.getReference("activite/"+str+"/typeDeSport");
        typeSport.setValue(spinner.getSelectedItem().toString());
        DatabaseReference date = database.getReference("activite/"+str+"/date");
        date.setValue(dateButton.getText());
        finish();
    }




}
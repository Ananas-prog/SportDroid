package com.example.sportdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class block_entrainement implements Serializable{
    private String typeBlock;
    private String comBlock;
    private String typeParam;
    private String valParam;

    public block_entrainement(String typeBlock,String comBlock, String typeParam, String valParam) {
        this.typeBlock=typeBlock;
        this.comBlock=comBlock;
        this.typeParam=typeParam;
        this.valParam=valParam;
    }

    public String getTypeBlock() {
        return typeBlock;
    }
    public void setTypeBlock(String name) {
        this.typeBlock = name;
    }

    public String getComBlock() {
        return comBlock;
    }
    public void setComBlock(String note) {
        this.comBlock = note;
    }

    public String getTypeParam() {
        return typeParam;
    }
    public void setTypeParam(String param) {
        this.typeParam = param;
    }

    public String getValParam() {
        return valParam;
    }
    public void setValParam(String val) {
        this.valParam = val;
    }

}

class popUp_detail_entrainement extends Dialog implements Serializable{
    private EditText commentaire;
    private EditText valParam;
    private Button retour;
    private Button valider;

    Spinner typeDetape;
    Spinner typeDeParam;


    public popUp_detail_entrainement(Activity activity){
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);

        setContentView(R.layout.pop_up_etape_entrainement);
        this.retour = (Button) findViewById(R.id.btnRetour);
        this.valider = (Button) findViewById(R.id.btnValider);
        this.commentaire = (EditText) findViewById(R.id.commEtape);
        this.valParam = (EditText) findViewById(R.id.valParam);

        typeDetape =(Spinner)findViewById(R.id.spinnerEtape);
        List exempleEtape =new ArrayList();
        exempleEtape.add("Course");
        exempleEtape.add("Récupération");
        exempleEtape.add("Echauffement");
        ArrayAdapter adapterEtape = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, exempleEtape);
        adapterEtape.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeDetape.setAdapter(adapterEtape);

        typeDeParam =(Spinner)findViewById(R.id.spinnerParam);
        List exempleParam =new ArrayList();
        exempleParam.add("Kilomètres");
        exempleParam.add("Mètres");
        exempleParam.add("Minutes");
        exempleParam.add("Secondes");
        ArrayAdapter adapterParam = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, exempleParam);
        adapterParam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeDeParam.setAdapter(adapterParam);
    }

    public Button getRetour() { return retour; }
    public Button getValider() { return valider; }

    public String toString(){
        return  this.typeDetape+"  "+ this.commentaire+"  "+ this.typeDeParam+"  "+ this.valParam;
    }

    public void LancerAjoutEtape(){

        show();
    }

}

public class ajout_entrainement extends AppCompatActivity {

    private static final String TAG = "";
    public DatePickerDialog datePickerDialog;
    public Button dateButton;
    Spinner spinner;
    Spinner spinnerDate;
    Spinner spinnerLieu;
    ListView listView;
    public ArrayList<block_entrainement> block=new ArrayList<block_entrainement>();
    public DatabaseReference databaseReference;
    public String str = "";
    private EditText note;
    private TextView time;
    private Button newEtape;
    private int heure,minute;
    private String role;
    private TextView test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_entrainement);
        note=(EditText)findViewById(R.id.texteNoteEntrainement);
        time= (TextView) findViewById(R.id.textViewTime);
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.UK);
        String localizedDate = df.format(date);
        time.setText(localizedDate);
        TextView t1 = (TextView) findViewById(R.id.titreHome);
        listView=(ListView) findViewById(R.id.listView);
        newEtape = (Button) findViewById(R.id.buttonAjoutEtape);
        newEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp_detail_entrainement ajoutEtape = new popUp_detail_entrainement(ajout_entrainement.this);
                ajoutEtape.LancerAjoutEtape();
                ajoutEtape.getRetour().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ajoutEtape.dismiss();
                    }
                });
               ajoutEtape.getValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                        ajoutEtape.dismiss();
                    }
                });
            }
        });
      //  Button supprimerActiviter=(Button)findViewById(R.id.buttonSupprimerActivite);
       // supprimerActiviter.setOnClickListener(new View.OnClickListener() {
       //     @Override
         ///   public void onClick(View view) {
            //    FirebaseDatabase database = FirebaseDatabase.getInstance();
              //  DatabaseReference activite = database.getReference("activite/");
             //   activite.child(str+"/note").setValue("annulé");
              //   rafraichissementListe();
               // int num = Integer.getInteger(str);
               // finish();

          //  }
        //});

        // Gestion heure


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initalisation
                TimePickerDialog timePickerDialog= new TimePickerDialog(ajout_entrainement.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int heureDay, int minuteDay) {
                        // initialise heure et minute
                        heure=heureDay;
                        minute=minuteDay;
                        // iniatilse
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,heure,minute);
                        time.setText(DateFormat.format("HH:mm",calendar));
                    }
                },24,0,true
                );
                timePickerDialog.updateTime(heure,minute);
                timePickerDialog.show();
            }
        });



        Intent intent = getIntent();
        if (intent.hasExtra("nEntrainement")){ // vérifie qu'une valeur est associée à la clé “edittext”
            str = intent.getStringExtra("nEntrainement"); // on récupère la valeur associée à la clé
        }


        spinnerLieu=(Spinner)findViewById(R.id.spinnerLieu);
        List exempleLieu=new ArrayList();
        exempleLieu.add("Les Atlantides");
        exempleLieu.add("Coubertin");
        exempleLieu.add("Ardriers");
        exempleLieu.add("Ile aux sports");
        exempleLieu.add("Bois de l'Epau (parking du verger)");

        ArrayAdapter adapterlieu = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                exempleLieu
        );

        /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
        adapterlieu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Enfin on passe l'adapter au Spinner et c'est tout
        spinnerLieu.setAdapter(adapterlieu);


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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // i est la postion ou on clique
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ajout_entrainement.this,"clique sur l 'item "+ i+ ""+block.get(i).toString(),Toast.LENGTH_LONG).show();
               // popUp_detail_entrainement newEtape = new popUp_detail_entrainement(ajout_entrainement.this);
               // newEtape.LancerAjoutEtape();
                // pour modifier le block
               // Intent intent = new Intent(ajout_entrainement.this, detail_block.class);
               // intent.putExtra("nActivite",str);
               // intent.putExtra("nBlock",String.valueOf(i));
               // startActivity(intent);



               // startActivity(new Intent(ajout_entrainement.this,pop.class));
               // FirebaseDatabase database = FirebaseDatabase.getInstance();
               // DatabaseReference blockr = database.getReference("activite/"+str+"/block");
               // blockr.setValue(block);


            }
        });







        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("activite/"+str+"/block");


        DatabaseReference get = database.getReference("activite/"+str);
        get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String heure = (String) snapshot.child("/heure").getValue();
               // time.setText(heure);
                //String date = (String) snapshot.child("/date").getValue();
                //dateButton.setText(date);
                String typeDeSport = (String) snapshot.child("/typeDeSport").getValue();
               // time.setText(heure);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                    if(duree.equals("null")&&notes.equals("null")&&typeDeDuree.equals("null")&&typeDetape.equals("null")){

                    }else{
                        block_entrainement element = new block_entrainement(typeDetape,notes,typeDeDuree,duree);
                        block.add(element);
                    }

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

        ListView listView=(ListView) findViewById(R.id.listView);
        listView.setAdapter(null);
        listView.setAdapter(new listViewAdapterDetailsBlock(this, block));
        // Write a message to the database
    }
    public void AjoutActivite(View view){
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
        return day+" "+getMonthFormar(month)+" "+year;
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
        DatabaseReference note = database.getReference("activite/"+str+"/note");
        note.setValue(this.note.getText().toString());
        DatabaseReference time = database.getReference("activite/"+str+"/heure");
        time.setValue(this.time.getText().toString());
        DatabaseReference lieu = database.getReference("activite/"+str+"/lieu");
        lieu.setValue(spinnerLieu.getSelectedItem().toString());

        finish();
    }




}
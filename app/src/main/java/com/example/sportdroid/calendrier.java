package com.example.sportdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Calendar;

public class calendrier extends AppCompatActivity {
    private Button ButtonCalendrier;
    private Button ButtonInfos;
    private Button Buttonhome;
    private String role;
    CalendarView calendar;
    TextView date;
    ListView listView;
    private ImageView imgInfo;
    private int u;
    private String str;

    public ArrayList<activite> tabActivite= new ArrayList<>();
    public ArrayList<activite> tabActiviteJournee= new ArrayList<>();
    public String DateSelectionner;
    private ConstraintLayout layoutPrincipalCalendrier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Calendar cal= Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        int day =cal.get(Calendar.DAY_OF_MONTH);
        imgInfo=(ImageView)findViewById(R.id.imgInfo);

        calendar=(CalendarView)findViewById(R.id.calendarView);
        date =(TextView) findViewById(R.id.textDate);
        date.setText(day+" "+getMonthFormar(month+1)+" "+year);

        Intent intent = getIntent();

        if (intent.hasExtra("role")){ // vérifie qu'une valeur est associée à la clé “edittext”
            role = intent.getStringExtra("role"); // on récupère la valeur associée à la clé
        }

        layoutPrincipalCalendrier = (ConstraintLayout) findViewById(R.id.linearLayoutcalendar);
        layoutPrincipalCalendrier.setOnTouchListener(new OnSwipeTouchListener(calendrier.this){
            @Override
            public void onSwipeRight(){
                informations();
            }
            @Override
            public void onSwipeLeft(){
                home();
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String dateSelectioner=i2+" "+getMonthFormar(i1+1)+" "+i;
                DateSelectionner=dateSelectioner;
                date.setText(dateSelectioner);
                //tabActiviteJourne.add(tabActivite.get(1));
                tabActiviteJournee.clear();
                for (int j = 0; j < tabActivite.size(); j++) {
                    if(tabActivite.get(j).getDate().equals(DateSelectionner)){
                        tabActiviteJournee.add(tabActivite.get(j));
                        rafraichissementListe();
                    }
                }
                if(tabActiviteJournee.size()==0){
                    tabActiviteJournee.clear();
                    rafraichissementListe();
                }
            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Accueil.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom,R.anim.slide_out_bottom);
                finish();
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite");
        ArrayList<block_entrainement> tabBlock = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tabActivite.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    //blocks.clear();
                    String dateR = String.valueOf(ds.child("/date").getValue());
                    String sport = String.valueOf(ds.child("/typeDeSport").getValue());
                    String note = String.valueOf(ds.child("/note").getValue());
                    String heure = String.valueOf(ds.child("/heure").getValue());
                    String lieu = String.valueOf(ds.child("/lieu").getValue());

                    tabActivite.add(new activite(sport,dateR,note,heure,lieu));
                    if(dateR.equals(date.getText())){
                        activite element = new activite(sport, dateR,note,heure,lieu);
                        tabActiviteJournee.add(element);
                    }


                    rafraichissementListe();
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
                if (role.equals("coach")) {
                    Intent intent = new Intent(calendrier.this, ajout_entrainement.class);
                    intent.putExtra("date", tabActiviteJournee.get(i).getDate());
                    intent.putExtra("heure", tabActiviteJournee.get(i).getHeure());
                    intent.putExtra("lieu", tabActiviteJournee.get(i).getLieu());
                    startActivity(intent);
                } else {
                    activite currentActivity = tabActiviteJournee.get(i);
                    String activityName = currentActivity.getTypeDeSport();
                    String activityHeure = currentActivity.getHeure();
                    String activityLieu = currentActivity.getLieu();
                    String activityDate =currentActivity.getDate();
                    String activityDescription = currentActivity.getNote();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference rechercheacti = database.getReference("activite/");
                    rechercheacti.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            u=0;
                            for(DataSnapshot ds : snapshot.getChildren()){
                                u=u+1;
                                String date = String.valueOf(ds.child("/date").getValue());
                                String heure = String.valueOf(ds.child("/heure").getValue());
                                String lieu = String.valueOf(ds.child("/lieu").getValue());
                                if(date.equals(activityDate)&& heure.equals(activityHeure)&& lieu.equals(activityLieu)){
                                    str=String.valueOf(u);
                                    // Toast.makeText(ajout_entrainement.this,"trouver "+ i ,Toast.LENGTH_LONG).show();
                                    DatabaseReference myRef1 = database.getReference("activite/"+str+"/block");
                                    myRef1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            tabBlock.clear();
                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                                String duree = String.valueOf(ds.child("/valParam").getValue());
                                                String notes = String.valueOf(ds.child("/comBlock").getValue());
                                                String typeDeDuree = String.valueOf(ds.child("/typeParam").getValue());
                                                String typeDetape = String.valueOf(ds.child("/typeBlock").getValue());

                                                if(duree.equals("null")&&notes.equals("null")&&typeDeDuree.equals("null")&&typeDetape.equals("null")){

                                                }else{
                                                    block_entrainement element = new block_entrainement(typeDetape,notes,typeDeDuree,duree);
                                                    tabBlock.add(element);
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    //Toast.makeText(MainActivity.this,"trouver "+ tabBlock ,Toast.LENGTH_LONG).show();
                    afficheActivity showActivity = new afficheActivity(calendrier.this);
                    showActivity.LancerAffichageActivity(activityName, activityHeure, activityLieu, activityDescription, (String)date.getText(), tabBlock);
                    showActivity.getOk().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showActivity.dismiss();
                        }
                    });
                }
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

        ButtonInfos= (Button) findViewById(R.id.ButtonInfos);
        ButtonInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informations();
            }
        });
    }

    public void rafraichissementListe(){
        ListView listView=(ListView) findViewById(R.id.listeViewCalendrier);
        listView.setAdapter(null);
        listView.setAdapter(new listViewAdapterCalendar(this, tabActiviteJournee));
    }

    public void home(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("role", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();

    }
    public void calender(){
      //  Intent intent = new Intent(this, calendrier.class);
       // startActivity(intent);
    }
    public void informations(){
        Intent intent = new Intent(this, infos.class);
        intent.putExtra("role", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();
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


package com.example.sportdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


class activite implements Serializable {
    private String typeDeSport;
    private String date;
    private String note;
    private String heure;
    private String lieu;
    ArrayList<block_entrainement> tabBlock;

    public activite(String typeDeSport,String date, String note,String heure,String lieu, ArrayList<block_entrainement> tabBlock) {
        this.typeDeSport=typeDeSport;
        this.date=date;
        this.note=note;
        this.heure=heure;
        this.lieu=lieu;
        this.tabBlock=tabBlock;
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

    public String getTypeDeSport() { return typeDeSport; }
    public void setTypeDeSport(String typeDeSport) {
        this.typeDeSport = typeDeSport;
    }

    public ArrayList<block_entrainement> getBlock(){ return tabBlock; }

    public String getLieu() {
        return lieu;
    }

    public String toString(){
        return  this.typeDeSport+"  "+ this.date+ " "+ this.note+" "+this.heure+" "+this.lieu;
    }

}

class afficheActivity extends Dialog implements Serializable {

    private Button btnOk;
    private TextView heureEnt;
    private TextView dateEnt;
    private TextView typeEnt;
    private TextView comEnt;
    private TextView lieuEnt;
    ListView listView;
    Context context;

    public afficheActivity(Activity activity){
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        context = activity;
        setContentView(R.layout.pop_up_affiche_entrainement);
        this.btnOk = (Button) findViewById(R.id.ok);
        this.dateEnt = (TextView) findViewById(R.id.valDate);
        this.heureEnt = (TextView) findViewById(R.id.valHeure);
        this.typeEnt = (TextView) findViewById(R.id.valEntrainement);
        this.comEnt = (TextView) findViewById(R.id.valCom);
        this.lieuEnt = (TextView) findViewById(R.id.valLieu);
        this.listView = (ListView) findViewById(R.id.llstViewBlock);
    }

    public Button getOk() { return btnOk; }

    public void LancerAffichageActivity(String titre, String heure, String lieu, String commentaire, String date, ArrayList<block_entrainement> tabBlock){
        dateEnt.setText(date);
        heureEnt.setText(heure);
        lieuEnt.setText(lieu);
        typeEnt.setText(titre);
        if(commentaire.equals(""))
            comEnt.setText("Aucun commentaire");
        else comEnt.setText(commentaire);
        listView.setAdapter(null);
        ArrayList<block_entrainement> test = new ArrayList<>();
        test.add(new block_entrainement("Course", "seuil", "Mètres", "5"));
        listView.setAdapter(new listViewAdapterBlock(context, test));
        show();
    }
}

public class MainActivity extends AppCompatActivity {
    private ImageView imgInfo;
    private Button ButtonCalendrier;
    private Button ButtonInfos;
    private Button Buttonhome;
    private Button ButtonAjouterEntrainement;
    private TextView Temp,Resenti,vent;
    public ArrayList<activite> listeEntrainementJournee=new ArrayList<>();
    public ArrayList<activite> tabActivite= new ArrayList<>();
    private LinearLayout ajoutEntrainement;
    private String role;
    private  TextView rMeteo;
    private final String url="http://api.openweathermap.org/data/2.5/weather";
    private final String appid="73f07a7eda6bf4d485c06b24ac0d17d3";
    DecimalFormat df=new DecimalFormat("#.##");
    private Context context=this;
    private ConstraintLayout layoutPrincipalAceuil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // rMeteo=(TextView)findViewById(R.id.textViewMeteo);
        Temp=(TextView)findViewById(R.id.textViewTemp);
        Resenti=(TextView)findViewById(R.id.textViewRessentie);
        vent=(TextView)findViewById(R.id.textViewVent);
        layoutPrincipalAceuil=(ConstraintLayout)findViewById(R.id.linearLayout19);
        imgInfo=(ImageView)findViewById(R.id.imgInfo);
        ajoutEntrainement = (LinearLayout) findViewById(R.id.layoutAjoutEntrainement);
        Intent intent = getIntent();

        if (intent.hasExtra("role")){ // vérifie qu'une valeur est associée à la clé “edittext”
            role = intent.getStringExtra("role"); // on récupère la valeur associée à la clé
        }
        if(role.equals("athlete")){
            ajoutEntrainement.setVisibility(View.INVISIBLE);
        }

        layoutPrincipalAceuil.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            @Override
            public void onSwipeRight(){
                calender();
            }
            @Override
            public void onSwipeLeft(){
                informations();
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

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dateActuelle = (day + " " + getMonthFormar(month + 1) + " " + year);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("activite");
        ArrayList<block_entrainement> tabBlock = new ArrayList<>();
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
                        //recuperationBlock BDD
                        activite element = new activite(sport, date,note,heure,lieu, tabBlock);
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
                getWeatherDetails();

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
               // Toast.makeText(MainActivity.this,"clique sur l 'item "+ i+ ""+listeEntrainementJournee.get(i).toString(),Toast.LENGTH_LONG).show();
                if(role.equals("coach")){
                    Intent intent = new Intent(MainActivity.this, ajout_entrainement.class);
                    intent.putExtra("date", listeEntrainementJournee.get(i).getDate());
                    intent.putExtra("heure", listeEntrainementJournee.get(i).getHeure());
                    intent.putExtra("lieu", listeEntrainementJournee.get(i).getLieu());

                    // ici ne pas injecter le numero mais la date/ heure / lieux
                    startActivity(intent);
                }else{
                    activite currentActivity = listeEntrainementJournee.get(i);
                    String activityName = currentActivity.getTypeDeSport();
                    String activityHeure = currentActivity.getHeure();
                    String activityLieu = currentActivity.getLieu();
                    String activityDescription = currentActivity.getNote();
                    ArrayList<block_entrainement> tabBlock = currentActivity.getBlock();
                    afficheActivity showActivity = new afficheActivity(MainActivity.this);
                    showActivity.LancerAffichageActivity(activityName, activityHeure, activityLieu, activityDescription, dateActuelle, tabBlock);
                    showActivity.getOk().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showActivity.dismiss();
                        }
                    });
                }


            }
        });

    }

    public void getWeatherDetails(){
        String tempUrl="";
        String city="Le Mans";
        String country="France";
        tempUrl=url+"?q="+city+","+country+"&appid="+appid+"&units=metric";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                String output="";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    String icon = jsonObjectWeather.getString("icon");

                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") ;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") ;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    output += "Current weather of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa"
                            + "\n icon: " + icon;
                  //  rMeteo.setText(output);
                    Temp.setText(" Temp: " + df.format(temp) + " °C");
                    vent.setText(" Vent: " + wind + "m/s ");
                    Resenti.setText(" Ressentie: " + df.format(feelsLike) + " °C");
                    //Affichage de l'image correspondante
                    ImageView iconMeteo = (ImageView) findViewById(R.id.imageViewMeteo);
                    Glide.with(context).load(Uri.parse(getImage(icon))).into(iconMeteo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @NonNull
    public static String getImage(String icon){
        return String.format("http://openweathermap.org/img/w/%s.png",icon);
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
        ArrayList<block_entrainement> tabBlockNull = null;
        //tabBlockNull.add(new block_entrainement("", "", "",""));
        activite un = new activite("","","","","", tabBlockNull);
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
        Intent intent = new Intent(getApplicationContext(), calendrier.class);
        intent.putExtra("role", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }
    public void informations(){
        Intent intent = new Intent(getApplicationContext(), infos.class);
        intent.putExtra("role", role);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();
    }
}
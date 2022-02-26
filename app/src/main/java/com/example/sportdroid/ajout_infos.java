package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ajout_infos extends AppCompatActivity {
    public DatePickerDialog datePickerDialog;


    private EditText image;
    private EditText titre;
    private EditText lien;
    private EditText paragraphe;
    private Button valider;
    public String nInfos;
    public Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_infos);
        Intent intent = getIntent();
        if (intent.hasExtra("nInfos")){ // vérifie qu'une valeur est associée à la clé “edittext”
            nInfos = intent.getStringExtra("nInfos"); // on récupère la valeur associée à la clé
        }
        image=(EditText) findViewById(R.id.editTextImage);
        titre=(EditText) findViewById(R.id.editTextTextTitre);
        dateButton=(Button) findViewById(R.id.buttondateinfo);
        lien=(EditText) findViewById(R.id.editTextTextLien);
        paragraphe=(EditText) findViewById(R.id.editTextTextParagraphe);
        valider=(Button) findViewById(R.id.buttonvaliderinfo);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference test = database.getReference("infos/"+nInfos+"/image");
                test.setValue(image.getText().toString());
                DatabaseReference test1 = database.getReference("infos/"+nInfos+"/titre");
                test1.setValue(titre.getText().toString());
                DatabaseReference test2 = database.getReference("infos/"+nInfos+"/date");
                test2.setValue(dateButton.getText().toString());
                DatabaseReference test3 = database.getReference("infos/"+nInfos+"/lien");
                test3.setValue(lien.getText().toString());
                DatabaseReference test4 = database.getReference("infos/"+nInfos+"/paragraphe");
                test4.setValue(paragraphe.getText().toString());
                finish();
            }
        });

        initDatePicker();
        dateButton.setText(getTodaysDate());




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

}
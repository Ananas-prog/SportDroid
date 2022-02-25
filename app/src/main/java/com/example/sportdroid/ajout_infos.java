package com.example.sportdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ajout_infos extends AppCompatActivity {


    private EditText image;
    private EditText titre;
    private EditText date;
    private EditText lien;
    private EditText paragraphe;
    private Button valider;
    public String nInfos;

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
        date=(EditText) findViewById(R.id.editTextTextDate);
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
                test2.setValue(date.getText().toString());
                DatabaseReference test3 = database.getReference("infos/"+nInfos+"/lien");
                test3.setValue(lien.getText().toString());
                DatabaseReference test4 = database.getReference("infos/"+nInfos+"/paragraphe");
                test4.setValue(paragraphe.getText().toString());
                finish();
            }
        });




    }
}
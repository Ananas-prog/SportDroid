package com.example.sportdroid;

import java.util.ArrayList;

public class activite {
    private String typeDeSport;
    private String date;
    private ArrayList<detaille_entrainement> block=new ArrayList<>();


    public activite(String typeDeSport,String date) {
        this.typeDeSport=typeDeSport;
        this.date=date;
       // block= new ArrayList<detaille_entrainement>("Echauffement","aucune","Temps",30);
    }
}

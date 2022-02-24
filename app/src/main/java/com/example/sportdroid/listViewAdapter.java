package com.example.sportdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class listViewAdapter extends BaseAdapter {
    private Context context;
    private List<activite> listActivity;
    private LayoutInflater inflater;

    public listViewAdapter(Context context, List<activite> list){
        this.context = context;
        this.listActivity = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listActivity.size();
    }

    @Override
    public activite getItem(int position) {
        return listActivity.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_layout, null);
        //Informations à propos de l'item
        activite currentActivity = getItem(i);
        String activityName = currentActivity.getTypeDeSport();
        String activityHeure = currentActivity.getHeure();
        String activityDescription = currentActivity.getNote();

        //Affichage du nom du sport
        TextView itemNameView = view.findViewById(R.id.NomSport_listItem);
        itemNameView.setText(activityName);

        //Affichage de l'heure de la séance
        TextView itemHeureView = view.findViewById(R.id.HeureSport_listItem);
        itemHeureView.setText(activityHeure);

        //Affichage de la description de la séance
        TextView itemDescriptionView = view.findViewById(R.id.DescriptSport_listItem);
        itemDescriptionView.setText(activityDescription);

        //Affichage de l'image correspondante
        ImageView itemImageView = view.findViewById(R.id.imgSport_listItem);
        String ressourceImg = Normalizer.normalize(activityName, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "").toLowerCase();
        int imgId = context.getResources().getIdentifier(ressourceImg, "mipmap", context.getPackageName());
        itemImageView.setImageResource(imgId);
        return view;
    }
}

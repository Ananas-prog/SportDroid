package com.example.sportdroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.List;

public class listViewAdapterInfo extends BaseAdapter {
    private Context context;
    private List<info> listInfo;
    private LayoutInflater inflater;

    public listViewAdapterInfo(Context context, List<info> list){
        this.context = context;
        this.listInfo = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listInfo.size();
    }

    @Override
    public info getItem(int position) {
        return listInfo.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void openLien(View view, String urlButton){
        String url = urlButton;
        Intent intentLien = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        context.startActivity(intentLien );
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_info_layout, null);
        //Informations à propos de l'item
        info currentInfo = getItem(i);
        String infoImage = currentInfo.getImage();
        String infoTitre = currentInfo.getTitre();
        String infoDate = currentInfo.getDate();
        String infoLieu = currentInfo.getLieu();
        String infoParagraphe = currentInfo.getParagraphe();
        String infoLien = currentInfo.getLien();

        //Affichage du titre de l'info
        TextView itemTitreView = view.findViewById(R.id.titreInfo);
        itemTitreView.setText(infoTitre);

        //Affichage de la date et du lieu de l'info
        TextView itemDateLieuView = view.findViewById(R.id.dateLieuInfo);
        itemDateLieuView.setText(infoLieu+", "+"le "+infoDate);

        //Affichage de la description de la séance
        TextView itemDescriptionView = view.findViewById(R.id.desciptionInfo);
        itemDescriptionView.setText(infoParagraphe);

        //Button lien de l'info
        Button buttonLienView = view.findViewById(R.id.lienInfo);
        buttonLienView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLien(view, infoLien);
            }
        });
        buttonLienView.setText("En savoir plus");

        //Affichage de l'image correspondante
        ImageView itemImageView = view.findViewById(R.id.imageInfo);
        Glide.with(context).load(Uri.parse(infoImage)).into(itemImageView);



        return view;

    }
}

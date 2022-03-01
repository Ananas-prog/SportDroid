package com.example.sportdroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class listViewAdapterDetailsBlock extends BaseAdapter {
    private Context context;
    private List<block_entrainement> listBlock;
    private LayoutInflater inflater;

    public listViewAdapterDetailsBlock(Context context, List<block_entrainement> list){
        this.context = context;
        this.listBlock = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listBlock.size();
    }

    @Override
    public block_entrainement getItem(int position) {
        return listBlock.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_ajout_entrainement_layout, null);
        //Informations à propos de l'item
        block_entrainement currentBlock = getItem(i);
        String name = currentBlock.getTypeBlock();
        String com = currentBlock.getComBlock();
        String unite;
        switch (currentBlock.getTypeParam()){
            case "Kilomètres" : unite="km"; break;
            case "Mètres" : unite="m"; break;
            case "Minutes" : unite="min"; break;
            case "Secondes" : unite="sec"; break;
            default: unite = "";
        };
        String param = currentBlock.getValParam()+unite;

        //Affichage du nom du block
        TextView itemTitreView = view.findViewById(R.id.titreInfo);
        itemTitreView.setText(name);

        //Affichage du commentaire du block
        TextView itemComView = view.findViewById(R.id.dateLieuInfo);
        itemComView.setText(com);

        //Affichage du paramètre du block
        TextView itemParamView = view.findViewById(R.id.desciptionInfo);
        itemParamView.setText(param);

        return view;

    }
}

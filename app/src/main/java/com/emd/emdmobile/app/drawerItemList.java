package com.emd.emdmobile.app;

import android.app.Activity;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class drawerItemList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] itemText;
    private final Integer[] itemImage;

    public drawerItemList(Activity context, String[] itemText, Integer[] itemImage){
        super(context, R.layout.drawer_item_list, itemText);

        this.context = context;
        this.itemText = itemText;
        this.itemImage = itemImage;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.drawer_item_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.drawertext);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.drawerImg);
        txtTitle.setText(itemText[position]);
        imageView.setImageResource(itemImage[position]);
        return rowView;
    }

}



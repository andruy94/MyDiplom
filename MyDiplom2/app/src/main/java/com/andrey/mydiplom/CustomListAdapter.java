package com.andrey.mydiplom;

/**
 * Created by Андрей on 04.02.2016.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<PictureDscr> {

    private final Activity context;
    List<PictureDscr> pics;
    ImageView imageView;

    public CustomListAdapter(Activity context, List<PictureDscr> Pics) {// наверно буду отдавать массив картоинок
        super(context, R.layout.mylist, Pics);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.pics=Pics;
    }


    @Override
    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        imageView = (ImageView) rowView.findViewById(R.id.picture);
        TextView extratxt = (TextView) rowView.findViewById(R.id.tvInfo);

        imageView.setImageBitmap(pics.get(position).bitmap);
        if(pics.get(position).Points>1){
            rowView.findViewById(R.id.isCheck).setVisibility(View.VISIBLE);
            extratxt.setText("очки: " + pics.get(position).Points);
        }else {
            rowView.findViewById(R.id.isCheck).setVisibility(View.INVISIBLE);
            int k=pics.get(position).Points+100;
            extratxt.setText("будет получено: " + k + " очков");
        }
        return rowView;

    }


}
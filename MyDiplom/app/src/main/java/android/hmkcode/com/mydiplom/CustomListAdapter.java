package android.hmkcode.com.mydiplom;

/**
 * Created by Андрей on 04.02.2016.
 */


import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    //private final String[] itemname;
    List<PictureDscr> Pics;
    Handler h;
    ImageView imageView;
   // private final Integer[] imgid;

    private final String LOG_TAG="TAG";
    public CustomListAdapter(Activity context, List<PictureDscr> Pics, List<String> strings) {// наверно буду отдавать массив картоинок
        super(context, R.layout.mylist, strings);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.Pics=Pics;
    }


    @Override
    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Head);
        imageView = (ImageView) rowView.findViewById(R.id.Picture);
        TextView extratxt = (TextView) rowView.findViewById(R.id.Picinfo);

        txtTitle.setText(Pics.get(position).filename);
        extratxt.setText("очки: "+Pics.get(position).Points+"");
        imageView.setImageBitmap(Pics.get(position).bitmap);
        if(Pics.get(position).Points>=100){
            rowView.findViewById(R.id.Check).setVisibility(View.VISIBLE);
        }else
            rowView.findViewById(R.id.Check).setVisibility(View.INVISIBLE);
/*
        h=new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        Log.d(LOG_TAG,"WAIT p="+position);
                        break;
                    case 1:
                        imageView.setImageResource(imgid[position]);

                }


            }
        };
        //h.sendEmptyMessage(0);
        new Thread(){// не выход, когда листаешь лютые потери в производительности
            @Override
            public void run() {
                try{
                    TimeUnit.SECONDS.sleep(2);// грузим из файла
                    h.sendEmptyMessage(1);
                    //просто делаем нужный bmp глобалкой
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        */
        return rowView;

    }


}
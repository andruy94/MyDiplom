package com.andrey.mydiplom;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentList extends Fragment {
    ListView list;
    protected View view;
    DBWorker dbWorker;
    CustomListAdapter adapter=null;
    TextView tVinfo;
    Handler h;
    List<PictureDscr> Pics;


    public FragmentList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_thrid, container, false);
        tVinfo=(TextView) view.findViewById(R.id.textView1);
        list=(ListView) view.findViewById(R.id.list);

        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        dbWorker = new DBWorker(getActivity());
        tVinfo.setText("Идёт загрузка");

        h=new Handler(){
            int k=0;
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        k++;
                        k%=msg.arg1+1;
                        tVinfo.setText("Загрузка "+k+'/'+msg.arg1);
                        break;
                    case 1:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Pics=dbWorker.GetAllPics();
                                for (int i=0;i<Pics.size();i++){
                                    try {
                                        Pics.get(i).DownloadPic(getActivity(),"/MyFolder3/");
                                        Log.d(MainActivity.TAG, "PicName=" + Pics.get(i).filename);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                h.sendEmptyMessage(2);
                            }
                        }).start();
                        break;
                    case 2:
                        if(getActivity()!=null) {
                            adapter=new CustomListAdapter(getActivity(), Pics);
                            list.setAdapter(adapter);//@argument for AsyncTask

                        }
                        else
                            Log.e(MainActivity.TAG, "I am Fat Bug");
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // TODO Auto-generated method stub
                                if (Pics.get(position).Points < 1) {
                                    adapter.notifyDataSetChanged();
                                    Intent intent = new Intent(getActivity(), GameActivity.class);
                                    intent.putExtra(DBWorker.DBHelper.id, Pics.get(position).id);
                                    intent.putExtra(DBWorker.DBHelper.Filename, Pics.get(position).filename);// передаём имя  картинку
                                    intent.putExtra(DBWorker.DBHelper.Answer, Pics.get(position).Answer);
                                    intent.putExtra(DBWorker.DBHelper.Hint, Pics.get(position).Hint);
                                    intent.putExtra(DBWorker.DBHelper.Points, Pics.get(position).Points);
                                    startActivityForResult(intent, 228);
                                    dbWorker.close();// отпустим DB*/
                                } else {
                                    Toast.makeText(getActivity(), "Обнуление ребуса", Toast.LENGTH_SHORT).show();
                                    dbWorker.updatePic(Pics.get(position).id, Pics.get(position).filename,
                                            Pics.get(position).Answer, 0, Pics.get(position).Hint);
                                    Pics.get(position).Points = 0;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        tVinfo.setText("");
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);//@argument for AsyncTask
                        if(adapter.getCount()==0){
                            tVinfo.setText("Ошбика подключения, попробуйте ещё раз");
                        }
                        break;
                }
            }
        };

        try {
            dbWorker.open();
            (new U3AsyncTask(h, dbWorker,getActivity())).execute(getString(R.string.url));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK && data!=null) {
            PictureDscr pic = Pics.get(0);
            int k=0;
            for (PictureDscr tmp:Pics){
                if(tmp.id==data.getIntExtra(DBWorker.DBHelper.id,0)) {
                    pic = tmp;
                    break;
                }
                k++;
            }

            pic.Points = data.getIntExtra(DBWorker.DBHelper.Points,666);
            Pics.set(k, pic);
            adapter.notifyDataSetChanged();
        }
    }


}

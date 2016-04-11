package android.hmkcode.com.mydiplom;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThrid extends Fragment {
    ListView list;
    protected View view;
    DBWorker dbWorker;
    CustomListAdapter adapter;
    TextView tVinfo;
    Handler h;
    List<PictureDscr> Pics;


    public FragmentThrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_thrid, container, false);
        tVinfo=(TextView) view.findViewById(R.id.textView1);
        list=(ListView) view.findViewById(R.id.list);

        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        //adapter.setNotifyOnChange(true);
        dbWorker = new DBWorker(getActivity());
        tVinfo.setText("Идёт загрузка");

        h=new Handler(){// тут будет утечка памяти, ибо вложенные анонимные классы имеют ссыль на верзний класс, т е на mainActivity
            int k=0;
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        k++;
                        k%=5;
                        tVinfo.setText("Загрузка "+k+'/'+msg.arg1);
                        break;
                    case 1:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Pics=dbWorker.GetAllPics();
                                for (int i=0;i<Pics.size();i++){
                                    try {
                                        Pics.get(i).DownloadPic("/MyFolder/");
                                        Log.d(MainActivity.TAG,"PicName="+Pics.get(i).filename);
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
                            adapter=new CustomListAdapter(getActivity(), Pics,
                                    dbWorker.GetColumn(DBWorker.DBHelper.Filename));
                            adapter.notifyDataSetChanged();
                            list.setAdapter(adapter);
                        }
                        else
                            Log.e(MainActivity.TAG,"I am Fat Bug");
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                // TODO Auto-generated method stub
                                if(Pics.get(position).Points<100) {
                                    Intent intent = new Intent(getActivity(), GameActivity.class);
                                    intent.putExtra(DBWorker.DBHelper.Filename, Pics.get(position).filename);// передаём имя  картинку
                                    intent.putExtra(DBWorker.DBHelper.Answer, Pics.get(position).Answer);
                                    intent.putExtra(DBWorker.DBHelper.Hint, Pics.get(position).Hint);
                                    intent.putExtra(DBWorker.DBHelper.Points, Pics.get(position).Points);
                                    //getActivity().startActivity(intent);
                                    startActivityForResult(intent, 228);
                                    dbWorker.close();// отпустим DB
                                }else
                                    Toast.makeText(getActivity(),"Это уже было угадано",Toast.LENGTH_SHORT).show();
                            }
                        });
                        tVinfo.setText("");
                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };

        try {
            dbWorker.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        (new U3AsyncTask(h, dbWorker)).execute(getString(R.string.url));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbWorker.close();
    }
    //вот тут мы и будем апдейтить нашу таблицу
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            PictureDscr pic = Pics.get(1);
            pic.Points = 1000;
            Pics.set(1, pic);
            adapter.notifyDataSetChanged();
        }
        //(new U3AsyncTask(h, dbWorker)).execute(getString(R.string.url));
    }
}

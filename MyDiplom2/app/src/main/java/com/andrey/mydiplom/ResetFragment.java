package com.andrey.mydiplom;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View bigView;
    private TextView tvDialog;
    private AlertDialog alert=null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetFragment newInstance(String param1, String param2) {
        ResetFragment fragment = new ResetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ResetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reset, container, false);
        Button btnReset=(Button)view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        //----------
        bigView=View.inflate(getActivity(),R.layout.reset_dialog_layout,null);
        bigView.findViewById(R.id.btnYes).setOnClickListener(this);
        bigView.findViewById(R.id.btnNo).setOnClickListener(this);
        tvDialog=(TextView) bigView.findViewById(R.id.tvDialog);

        //---------
        return view;
    }







    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnReset:
               // tvDialog.setText("Вы уверены, что хотите сбросить все результаты?");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.icon).setTitle("Вы уверены, что хотите сбросить все результаты?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.cancel();
                                DBWorker dbWorker=new DBWorker(getActivity());
                                try {
                                    dbWorker.open();
                                    dbWorker.getDB().delete(DBWorker.DBHelper.TableName,DBWorker.DBHelper.id+" > 0",null);
                                    dbWorker.close();
                                    Toast.makeText(getActivity(),"Всё удаленно",Toast.LENGTH_SHORT).show();
                                } catch (SQLException e) {
                                    dialog.cancel();
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.cancel();
                    }
                });
                builder.create().getWindow().findViewById();
                alert = builder.create();
                alert.show();
                break;
            case R.id.btnYes:
                Toast.makeText(getActivity(),"Сброс",Toast.LENGTH_SHORT).show();
                //alert.cancel();
                break;
            case R.id.btnNo:
                Toast.makeText(getActivity(),"На нет и суда нет",Toast.LENGTH_SHORT).show();
                //alert.cancel();
                break;
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


}

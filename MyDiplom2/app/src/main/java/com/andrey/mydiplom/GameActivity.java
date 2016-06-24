package com.andrey.mydiplom;

/**
 * Created by Андрей on 08.04.2016.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Button> buttonList=new ArrayList<Button>();
    int k=0;
    int m;
    int n;
    String Answer="null";
    PictureDscr pictureDscr;
    Button[] ArrayAnswerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up notitle
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

        Intent intent=getIntent();


        pictureDscr=new PictureDscr();
        pictureDscr.id=intent.getIntExtra(DBWorker.DBHelper.id, -1);
        pictureDscr.filename=intent.getStringExtra(DBWorker.DBHelper.Filename);
        pictureDscr.Answer=intent.getStringExtra(DBWorker.DBHelper.Answer);
        pictureDscr.Hint=intent.getStringExtra(DBWorker.DBHelper.Hint);
        pictureDscr.Points=intent.getIntExtra(DBWorker.DBHelper.Points, 666);
        Answer=pictureDscr.Answer;
        try {
            pictureDscr.DownloadPic(getApplicationContext(),"/MyFolder3/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((ImageView)this.findViewById(R.id.imageView)).setImageBitmap(pictureDscr.bitmap);
        findViewById(R.id.btnDel).setOnClickListener(this);
        findViewById(R.id.btnHint).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        LinearLayout LBtn1=(LinearLayout) findViewById(R.id.Lbutton1);
        LinearLayout LBtn2=(LinearLayout) findViewById(R.id.Lbutton3);
        LinearLayout LBtn3=(LinearLayout) findViewById(R.id.Lbutton2);
        int btnCount=LBtn1.getChildCount()-1;
        Button[] ArrayButton1=new Button[btnCount];
        Button[] ArrayButton2=new Button[btnCount];
        Button[] ArrayButton3=new Button[btnCount];
        int k2=65;//начало первой буквы

        for (int i=0,j=0;i<btnCount;i++){//плохая логика
            ArrayButton1[i]=((Button) LBtn1.getChildAt(i));
            if(i%2==0 && Answer.length()>j){
                if(  Answer.charAt(j)!=' ')
                    ArrayButton1[i].setText((Answer.charAt(j) + "").toUpperCase());
                else
                    ArrayButton1[i].setText(((char) (k2+i))+"");
                j++;
            }
            else
                ArrayButton1[i].setText(((char) (k2+i))+"");
            ArrayButton1[i].setOnClickListener(this);
            ArrayButton1[i].setId(234+i);
            ArrayButton2[i]=((Button) LBtn2.getChildAt(i));
            if(i%2==1 && Answer.length()>j){
                if( Answer.charAt(j)!=' ')
                    ArrayButton2[i].setText((Answer.charAt(j)+"").toUpperCase());
                j++;
            }else
                ArrayButton2[i].setText(((char) (k2+i+8))+"");
            ArrayButton2[i].setOnClickListener(this);
            ArrayButton2[i].setId(2340 + i);
            ArrayButton3[i]=((Button) LBtn3.getChildAt(i));
            if(i%2==0 && Answer.length()>j){
                if( Answer.charAt(j)!=' ')
                    ArrayButton3[i].setText((Answer.charAt(j)+"").toUpperCase());
                j++;
            }
            else
                ArrayButton3[i].setText(((char) (k2+i+16))+"");

            ArrayButton3[i].setOnClickListener(this);
            ArrayButton3[i].setId(23400+i);
        }
        LinearLayout LAnswBtn=((LinearLayout)findViewById(R.id.Answerbutton));
        int tmp=0;
        for (int i=0;i<Answer.length();i++)
            if(Answer.charAt(i)==' ')
                tmp++;
        int LAnswBtnlen=LAnswBtn.getChildCount();
        m=(LAnswBtnlen-Answer.length())/2+(LAnswBtnlen-Answer.length())%2;
        n=(LAnswBtnlen-Answer.length())/2;
        ArrayAnswerBtn = new Button[LAnswBtnlen-m-n-tmp];
        char c='1';
        for (int i=0, j=0,z=0;i<LAnswBtnlen;i++){//плохая логика
            int p=LAnswBtnlen-n-1;

            if(z<Answer.length())
                c = Answer.charAt(z);
            if(m>0 || i>p || c==' '){
                ((Button)LAnswBtn.getChildAt(i)).setVisibility(View.INVISIBLE);
                if(m>0) m--;
                if(c==' ') z++;
            }else {
                if(j<ArrayAnswerBtn.length) {
                    ArrayAnswerBtn[j] = ((Button) LAnswBtn.getChildAt(i));
                    j++;
                    z++;
                }

            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                ///answerView.drawText(((Button) view).getText().toString());
                if (k < ArrayAnswerBtn.length) {
                    ArrayAnswerBtn[k].setText(((Button) view).getText().toString());
                    k++;
                    ((Button) view).setTextColor(Color.GRAY);
                    view.setEnabled(false);
                    buttonList.add((Button) view);
                }
                if (buttonList.size() == ArrayAnswerBtn.length) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Button btn : ArrayAnswerBtn) {
                        stringBuffer.append(btn.getText().toString());
                    }
                    Log.d("TAG", stringBuffer.toString() + Answer.replaceAll(" ", ""));
                    if (stringBuffer.toString().equals(Answer.replaceAll(" ", "").toUpperCase())) {
                        (findViewById(R.id.LLWin)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.Result)).setText("Правильный ответ!");
                        (findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                (findViewById(R.id.LLGame)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.LLWin)).setVisibility(View.INVISIBLE);
                                DBWorker dbWorker = new DBWorker(getApplicationContext());
                                try {
                                    dbWorker.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                dbWorker.updatePic(pictureDscr.id, pictureDscr.filename, pictureDscr.Answer,
                                        pictureDscr.Points + 100, pictureDscr.Hint);
                                dbWorker.close();
                                Intent intent = new Intent().putExtra(DBWorker.DBHelper.Points, pictureDscr.Points + 100);
                                intent.putExtra(DBWorker.DBHelper.id, pictureDscr.id);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    } else {
                        (findViewById(R.id.LLWin)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.Result)).setText("Неправильный ответ!, -10 очков.");
                        (findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                (findViewById(R.id.LLGame)).setVisibility(View.VISIBLE);
                                (findViewById(R.id.LLWin)).setVisibility(View.INVISIBLE);
                                DBWorker dbWorker = new DBWorker(getApplicationContext());
                                try {
                                    dbWorker.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (pictureDscr.Points > -50) {//меньше 50очков глупо получать человеку
                                    pictureDscr.Points -= 10;
                                }
                                dbWorker.updatePic(pictureDscr.id, pictureDscr.filename, pictureDscr.Answer,
                                        pictureDscr.Points, pictureDscr.Hint);
                                dbWorker.close();
                                for (int i = 0; i < k; i++) {
                                    ArrayAnswerBtn[i].setText("");
                                    buttonList.get(i).setTextColor(Color.BLACK);
                                    buttonList.get(i).setEnabled(true);
                                }
                                buttonList.clear();
                                k = 0;
                            }
                        });
                    }
                }
                break;
            case R.id.btnDel:
                if(buttonList.size()>0){
                    k--;
                    ArrayAnswerBtn[k].setText("");
                    buttonList.get(buttonList.size() - 1).setTextColor(Color.BLACK);
                    buttonList.remove(buttonList.size() - 1).setEnabled(true);
                }
                break;
            case R.id.btnHint:
                Toast.makeText(getApplicationContext(),"Подсказка: "+pictureDscr.Hint,Toast.LENGTH_LONG).show();
                break;
            case R.id.btnBack:
                Intent intent = new Intent().putExtra(DBWorker.DBHelper.Points, pictureDscr.Points);
                intent.putExtra(DBWorker.DBHelper.id, pictureDscr.id);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }



    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}

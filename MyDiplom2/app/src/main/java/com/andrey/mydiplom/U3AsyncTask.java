package com.andrey.mydiplom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


public class U3AsyncTask extends AsyncTask<String, Void, Void> {
    private final Handler h;
    private DBWorker dbWorker;
    private View view;
    private List<PictureDscr> Pics;
    Context context;
        public U3AsyncTask(Handler h,DBWorker dbWorker,Context context){
            this.dbWorker=dbWorker;
            this.h=h;
            this.context=context;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... urls) {// тут входные параметры
            try {
                SharedPreferences sharedPreferences=context.getSharedPreferences(context.getString(R.string.SharedPref),
                        Context.MODE_PRIVATE);
                //Если нет логина или пароля то получаем его
                if(sharedPreferences.getString("Login","").equals("") ||
                        sharedPreferences.getString("Password","").equals("")){
                    // 1. create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();
                    // 2. make POST request to the given URL
                    HttpPost httpPost = new HttpPost(urls[0]);
                    String json = "";
                    // 3. build jsonObject
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("login","");
                    jsonObject.put("password","");
                    // 4. convert JSONObject to JSON to String
                    json=jsonObject.toString();
                    Log.d(MainActivity.TAG, json);
                    // 5. set json to StringEntity
                    StringEntity se = new StringEntity(json);
                    // 6. set httpPost Entity
                    httpPost.setEntity(se);
                    // 7. Set some headers to inform server about the type of the content
                    httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Content-type", "application/json");
                    // 8. Execute POST request to the given URL
                    HttpResponse httpResponse = httpclient.execute(httpPost);// вот тут мы указываем что это POST
                    // 9. receive response as inputStream and Parse Json
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    String buffer=convertInputStreamToString(inputStream);
                    Log.d(MainActivity.TAG, buffer.toString() + "php");
                        JSONObject dataJsonObj = new JSONObject(buffer.toString());
                    //в начале запишем прижедший логин
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Login",dataJsonObj.getString("Login"));
                    editor.putString("Password",dataJsonObj.getString("Password"));
                    editor.apply();
                    Log.e(MainActivity.TAG,"Login="+sharedPreferences.getString(context.getString(R.string.Login),""));
                }
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(urls[0]);
                String json = "";
                // 3. build jsonObject
                List<PictureDscr> Pics= dbWorker.GetAllPics();
                JSONArray Pics_id=new JSONArray();
                JSONArray Pics_Points=new JSONArray();// получаем массив Очков
                for(int i=0;i<Pics.size();i++){
                    Pics_id.put(i,Pics.get(i).id);
                    Pics_Points.put(i, Pics.get(i).Points);
                }
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("login", sharedPreferences.getString("Login",""));
                jsonObject.put("password",sharedPreferences.getString("Password",""));
                jsonObject.put("Pics_id",Pics_id);
                jsonObject.put("Points",Pics_Points);
                // 4. convert JSONObject to JSON to String
                json=jsonObject.toString();
                Log.d(MainActivity.TAG, json);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);// вот тут мы указываем что это POST
                // 9. receive response as inputStream and Parse Json
                InputStream inputStream = httpResponse.getEntity().getContent();
                String buffer=convertInputStreamToString(inputStream);
                JSONArray jsonFilename,jsonAnswer,jsonUrl,jsonPoints,jsonHint,jsonid;
                Log.d(MainActivity.TAG, buffer.toString() + "php");
                try {
                    JSONObject dataJsonObj = new JSONObject(buffer.toString());
                    //в начале запишем прижедший логин
                    jsonid = dataJsonObj.getJSONArray("id");
                    jsonUrl = dataJsonObj.getJSONArray("Url");
                    jsonAnswer = dataJsonObj.getJSONArray("Answer");
                    jsonFilename = dataJsonObj.getJSONArray("Name");
                    jsonPoints = dataJsonObj.getJSONArray("Points");
                    jsonHint=dataJsonObj.getJSONArray("Hint");
                }catch (JSONException e) {
                    Log.e(MainActivity.TAG, "не можем взять JSONARRAy");
                    h.sendEmptyMessage(666);
                    // если не получиться распарсить JSON то нет смысла дальше что-то делать, выходим из потока и переходим к U2
                    h.sendEmptyMessage(1);
                    return null;// выход
                }

                for (int i = 0; i < jsonFilename.length(); i++) {
                    try{
                        Integer id=jsonid.getInt(i);
                        String filename=jsonFilename.getString(i);
                        String Answer = jsonAnswer.getString(i);
                        Integer Point = jsonPoints.getInt(i);
                        String Hint = jsonHint.getString(i);
                        if(!jsonUrl.getString(i).equals("null")) {//сохраняем фотку
                            Log.d("TAG", filename + "U3");
                            Log.d(MainActivity.TAG, jsonUrl.getString(i));
                            URL networkUrl = new URL(jsonUrl.getString(i));
                            Bitmap networkBitmap = BitmapFactory.decodeStream(networkUrl.openConnection().getInputStream());

                            PictureDscr.SavePic(context,filename, "/MyFolder3/", networkBitmap);//"/com.studio1101/data/"
                            dbWorker.insertPic(id, filename, Answer, Point, Hint);
                            Log.d("TAG", id + filename + "_" + Answer + "_" + Point + "U3if");
                        }else {
                            dbWorker.updatePic(id, filename, Answer, Point, Hint);
                            Log.d("TAG", id + filename + "_" + Answer + "_" + Point + "U3else");
                        }
                    }catch (IOException e) { // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
                        h.sendEmptyMessage(666);// ошибка где-то
                        Log.d(MainActivity.TAG, "Проблемы с памятью"+e.toString());
                    }catch (JSONException e){ // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
                        h.sendEmptyMessage(667);// ошибка где-то
                        Log.d(MainActivity.TAG, "ошибка от сервера");
                    }
                    //h.sendEmptyMessage(0);// индикатор загрузки
                    Message msg= new Message();
                    msg.what=0;
                    msg.arg1=jsonFilename.length();
                    h.sendMessage(msg);
                }
            } catch (IOException e) {
                Log.d(MainActivity.TAG, "нет инета");
                h.sendEmptyMessage(668);// ошибка где-то
            } catch (JSONException e) {
                e.printStackTrace();
            }

            h.sendEmptyMessage(1);// если всё же удалась наша затея или не удалась
            return null;


            //-------------

        }

    protected void onPostExecute(Void result) {// тут нам приходит наш прекрасный результат
            // он нам не нужен

    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}


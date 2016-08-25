package com.andrey.mydiplom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Андрей on 18.03.2016.
 */
public class PictureDscr extends Object {
    public Integer id;
    public String filename;
    public String Answer;
    public Integer Points;
    public String Hint;
    public Integer Empty;
    public Bitmap bitmap;
    public PictureDscr(){
        Empty=0;// сначала все картинки непустые
    }

    static public void SavePic(Context context,String filename,String dir,Bitmap networkBitmap)throws IOException {
        File folderToSave = new File(context.getFilesDir()+dir);
        folderToSave.mkdir();// Делаем директорию, если иещё нет или случайно удалил
        File file = new File(folderToSave, filename);
        if(file.exists()) {
            file.delete();
            Log.e(MainActivity.TAG,"Удаляем файл");
        }
        OutputStream fOut = new FileOutputStream(file);
        networkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // сохранять картинку в jpeg-формате без сжатия
        fOut.flush();// принудительная очитска буфера
        fOut.close();

    }
    public void DownloadPic(Context context,String dir) throws IOException {
        File text1=new File(context.getFilesDir()+ dir);// пишем директорию
        File inputFile=new File(text1,filename);//имя файла
        FileInputStream fis = new FileInputStream(inputFile);
        this.bitmap= BitmapFactory.decodeStream(fis);
    }

}

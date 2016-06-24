package android.hmkcode.com.mydiplom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Андрей on 18.03.2016.
 */
public class PictureDscr extends Object{
    public Integer id;
    public String filename;
    public String Answer;
    public Integer Points;
    public String Hint;
    public Integer Empty;
    public Bitmap bitmap;// очень плохо, прям адос как плохо
    public PictureDscr(){
        Empty=0;// сначала все картинки непустые
    }

    static public void SavePic(String filename,String dir,Bitmap networkBitmap)throws IOException {
        File folderToSave = new File(Environment.getExternalStorageDirectory().getPath()+dir);
        folderToSave.mkdir();// Делаем директорию, если иещё нет или случайно удалил
        File file = new File(folderToSave, filename);
        OutputStream fOut = new FileOutputStream(file);
        networkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.-100 нет сжатия, наверно всё таки в bmp буду пилить
        fOut.flush();// принудительная очитска буфера
        fOut.close();
    }
    public void DownloadPic(String dir) throws IOException{
        File text1=new File(Environment.getExternalStorageDirectory().getPath() + dir);// пишем директорию
        File inputFile=new File(text1,filename);//имя файла
        FileInputStream fis = new FileInputStream(inputFile);
        this.bitmap=BitmapFactory.decodeStream(fis);
    }

}

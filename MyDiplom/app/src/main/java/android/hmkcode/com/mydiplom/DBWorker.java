package android.hmkcode.com.mydiplom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 01.03.2016.
 */
public class DBWorker {
    public static String DBName = "GameDB";
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBWorker(Context context) {
        this.dbHelper = new DBHelper(context, DBName);
    }

    public void open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null)
            db.close();
    }

    public long insertPic(String FileName, String Answer, Integer Point,String Hint) {
        ContentValues cv=new ContentValues();
        cv.put(DBWorker.DBHelper.TableColumn[0],FileName);
        cv.put(DBWorker.DBHelper.TableColumn[1],Answer);
        cv.put(DBWorker.DBHelper.TableColumn[2], Point);
        cv.put(DBWorker.DBHelper.TableColumn[3], Hint);

            return db.insert(DBWorker.DBHelper.TableName, null, cv);

    }

    public long updatePic(String FileName, String Answer, Integer Point,String Hint) {
        ContentValues cv=new ContentValues();
        cv.put(DBWorker.DBHelper.TableColumn[1],Answer);
        cv.put(DBWorker.DBHelper.TableColumn[2], Point);
        cv.put(DBWorker.DBHelper.TableColumn[3], Hint);
        return db.update(DBWorker.DBHelper.TableName, cv, DBHelper.TableColumn[0] + "= ?",
                new String[]{FileName});
    }

    public List<String> GetColumn(String ColumnName){
        List<String> Rows=new ArrayList<String>();
        Cursor c = db.query(DBWorker.DBHelper.TableName,new String[] {ColumnName} , null, null, null, null, null);
        if(c.moveToFirst()){
            int filenameColIndex = c.getColumnIndex(ColumnName);
            do{
                Rows.add(c.getString(filenameColIndex));
            }while (c.moveToNext());
        }else
            Log.d(MainActivity.TAG,"cursor is null");
        c.close();
        return Rows;
    }

    public List<PictureDscr> GetAllPics(){
        List<PictureDscr> Pics=new ArrayList<PictureDscr>();
        Cursor c = db.query(DBWorker.DBHelper.TableName,null , null, null, null, null, null);
        if(c.moveToFirst()) {
            do {
                PictureDscr pic=new PictureDscr();
                int filenameColIndex = c.getColumnIndex(DBHelper.Filename);
                pic.filename = c.getString(filenameColIndex);
                pic.Answer = c.getString(c.getColumnIndex(DBHelper.Answer));
                pic.Points = c.getInt(c.getColumnIndex(DBHelper.Points));
                pic.Hint = c.getString(c.getColumnIndex(DBHelper.Hint));
                Pics.add(pic);
            }while (c.moveToNext());
        }else {
            Log.d(MainActivity.TAG, "cursor=null");
        }
        return Pics;
    }

    public PictureDscr getOnePic(String FileName){
        Cursor c = db.query(DBWorker.DBHelper.TableName, null, DBHelper.TableColumn[0] + " = " + '"' + FileName + '"', null, null, null,
                null);
        PictureDscr pic=new PictureDscr();
        if(c.moveToFirst()) {
            int filenameColIndex = c.getColumnIndex(DBHelper.Filename);
            pic.filename = c.getString(filenameColIndex);
            pic.Answer = c.getString(c.getColumnIndex(DBHelper.Answer));
            pic.Points = c.getInt(c.getColumnIndex(DBHelper.Points));
            pic.Hint = c.getString(c.getColumnIndex(DBHelper.Hint));
        }else {
            Log.d(MainActivity.TAG, "cursor=-1");
            pic.Empty=1;
        }
            return pic;
    }

    public boolean DeletePic(/* я не знаю, что тут надо:(*/) {
        return true;
    }

    public SQLiteDatabase getDB() {
        if (db != null)
            try {
                open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return db;// может понадобиться
    }


    /**
     * Created by Андрей on 14.02.2016.
     */
    static public class DBHelper extends SQLiteOpenHelper {
        public static String[] TableColumn = {
                "FileName",
                "Answer",
                "Points",
                "Hint"};
        public static String Filename=TableColumn[0];
        public static String Answer=TableColumn[1];
        public static String Points=TableColumn[2];
        public static String Hint=TableColumn[3];

        public static String TableName = "Gametable";

        public DBHelper(Context context, String DBName) {
            // конструктор суперкласса
            super(context, DBName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TAG", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table " + TableName + " ("
                    + TableColumn[0] + " text primary key,"
                    + TableColumn[1] + " text,"
                    + TableColumn[2] + " INTEGER,"
                    + TableColumn[3] + " text );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}

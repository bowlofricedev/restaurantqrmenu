package com.bowlofricedev.restaurantqrmenu.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bowlofricedev.restaurantqrmenu.beans.Enlace;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "enlaces";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "URL";
    public static final String COL4 = "TYPE";
    public static final String COL5 = "FAV";
    public static final String COL6 = "TIME";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " INTEGER )";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Enlace item) {

        //TODO: COMPROBAR QUE NO HAYA SIDO YA VISITADO

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item.getName());
        contentValues.put(COL3, item.getUrl());
        contentValues.put(COL4, item.getType());
        contentValues.put(COL5, item.getFav());
        contentValues.put(COL6, item.getTimeMillis());

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //si se inserta correctamente será -1
        if (result == -1) {
            return false;

        } else {
            return true;
        }
    }


    //para recoger datos de la db
    public Cursor getData(String col, String order){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.query(TABLE_NAME, null, null, null, null, null, col + " " + order);
        return data;
    }

    public boolean deleteEnlace(int id){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, COL1 + "=" + id, null) > 0;


    }

    public boolean updateEnlace(int id, Enlace item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item.getName());
        contentValues.put(COL3, item.getUrl());
        contentValues.put(COL4, item.getType());
        contentValues.put(COL5, item.getFav());
        contentValues.put(COL6, item.getTimeMillis());

        return db.update(TABLE_NAME, contentValues, COL1 + "=" + id, null) > 0;


    }

    public boolean deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null) > 0;

    }

}

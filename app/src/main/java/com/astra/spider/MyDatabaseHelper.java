package com.astra.spider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";
    private static final String DATABASE_NAME = "spider.db";
    private static final String TABLE_NAME = "theme";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_DESCRIPTION = "description";

    private Context mContext;
    private SQLiteDatabase db;

    MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        mContext = context;

        try {
            context.deleteDatabase(DATABASE_NAME);
            installDatabaseFromAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //@Override
    //public SQLiteDatabase getReadableDatabase()  {
        //installOrUpdateIfNecessary()
        //mContext.deleteDatabase(DATABASE_NAME);

//        try {
//            installDatabaseFromAssets();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return super.getReadableDatabase();
//    }

    private void installDatabaseFromAssets() {
        try {

            InputStream mInputStream = mContext.getAssets().open(DATABASE_NAME);
            OutputStream mOutputStream = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addEntity(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + entity.getName());

        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_DESCRIPTION, entity.getDescription());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    List<Entity> getEntities() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Entity> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        //db.getVersion();

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name", null);

        if (cursor.moveToFirst()) {
            do {
                String table_name = cursor.getString(0);
                String rows_count = getRowCount(table_name);

                if(!table_name.equals("android_metadata")){
                    Entity entity = new Entity();
                    entity.setName(table_name + rows_count);

                    noteList.add(entity);
                }
            } while (cursor.moveToNext());
        }

        return noteList;
    }

    @NotNull
    private String getRowCount(String table_name) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS cnt FROM " + table_name, null);

        if(cursor.moveToFirst()) {
            String result = cursor.getString(0);
            if(!result.equals("0")){
                cursor.close();
                return " (" + result + ")";
            }
        }

        cursor.close();
        return "";
    }

    void updateNote(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + entity.getName());

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_DESCRIPTION, entity.getDescription());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(entity.getId())});
    }

    void deleteTheme(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + entity.getName() );

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(entity.getId()) });
        db.close();
    }
}
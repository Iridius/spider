package com.astra.spider.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.astra.spider.dao.Entity;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";
    private static final String DATABASE_NAME = "spider.db";
    private static final String TABLE_NAME = "theme";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_DESCRIPTION = "description";
    private SQLiteDatabase db;

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);

        context.deleteDatabase(DATABASE_NAME);
        installDatabaseFromAssets(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addEntity(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + entity.getName());

        db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_DESCRIPTION, entity.getDescription());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Entity> getEntities(String entityName) {
        List<Entity> list = new ArrayList<>();
        String query = "";

        if(entityName.length() == 0){
            query = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
        } else {
            query = "SELECT name FROM " + entityName;
        }

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String table_name = cursor.getString(0);

                if(!table_name.equals("android_metadata")){
                    Entity entity = new Entity();
                    entity.setName(table_name);

                    /* Если просматривается весь список таблиц, то определить количество записей в каждой из них */
                    if(entityName.length() == 0) {
                        int rows_count = getRowCount(table_name);
                        entity.setTag(String.valueOf(rows_count));
                    }

                    list.add(entity);
                }
            } while (cursor.moveToNext());
        }

        return list;
    }

    @NotNull
    private int getRowCount(String table_name) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS cnt FROM " + table_name, null);

        if(cursor.moveToFirst()) {
            int result = cursor.getInt(0);
                cursor.close();
                return result;
        }

        cursor.close();
        return 0;
    }

    public void updateEntity(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + entity.getName());

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_DESCRIPTION, entity.getDescription());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(entity.getId())});
    }

    public void deleteEntity(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + entity.getName() );

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(entity.getId()) });
        db.close();
    }

    private void installDatabaseFromAssets(Context context) {
        try {

            InputStream mInputStream = context.getAssets().open(DATABASE_NAME);
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
}
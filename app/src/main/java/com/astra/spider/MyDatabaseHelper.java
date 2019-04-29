package com.astra.spider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

//    void createDefaultNotesIfNeed()  {
//        int count = this.getNotesCount();
//        if(count ==0 ) {
//            Theme theme1 = new Theme("Авиация","");
//            Theme theme2 = new Theme("Бронетехника", "");
//            this.addTheme(theme1);
//            this.addTheme(theme2);
//        }
//    }

    private void installDatabaseFromAssets() throws IOException {
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

    void addTheme(Entity theme) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + theme.getName());

        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, theme.getName());
        values.put(COLUMN_DESCRIPTION, theme.getDescription());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Entity getName(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[] {COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION},
                COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Entity theme = new Entity(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return theme;
    }

    List<Entity> getEntities() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Entity> noteList = new ArrayList<Entity>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        //SQLiteDatabase db = mContext.g

        @SuppressLint("Recycle")
        //Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name", null);

        if (cursor.moveToFirst()) {
            do {
                Entity theme = new Entity();
                theme.setName(cursor.getString(0));
                noteList.add(theme);
            } while (cursor.moveToNext());
        }

        return noteList;
    }

//    private int getNotesCount() {
//        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );
//
//        String countQuery = "SELECT  * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        return count;
//    }

    void updateNote(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + entity.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_DESCRIPTION, entity.getDescription());

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(entity.getId())});
    }

    void deleteTheme(Entity entity) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + entity.getName() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(entity.getId()) });
        db.close();
    }
}
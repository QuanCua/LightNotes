package org.sitf_jica.tot.lightnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 11/20/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "lightNotes.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLOR = "color";


    private static final String TABLE_NAME2 = "trash";
    private static final String KEY_ID2 = "id";
    private static final String KEY_TITLE2 = "title";
    private static final String KEY_NOTE2 = "note";
    private static final String KEY_TIME2 = "time";
    private static final String KEY_COLOR2 = "color";

    private static final String TABLE_NAME3 = "archive";
    private static final String KEY_ID3 = "id";
    private static final String KEY_TITLE3 = "title";
    private static final String KEY_NOTE3 = "note";
    private static final String KEY_TIME3 = "time";
    private static final String KEY_COLOR3 = "color";


    public MyDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void addIntoTrash(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_TITLE2, note.getTitle());
        value.put(KEY_NOTE2, note.getNote());
        value.put(KEY_TIME2, note.getTime());
        value.put(KEY_COLOR2, note.getColor());
        db.insert(TABLE_NAME2, null, value);
        db.close();
    }

    public void addIntoArchive(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_TITLE3, note.getTitle());
        value.put(KEY_NOTE3, note.getNote());
        value.put(KEY_TIME3, note.getTime());
        value.put(KEY_COLOR3, note.getColor());
        db.insert(TABLE_NAME3, null, value);
        db.close();
    }

    public void deleteTrash(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, null, null);
        db.close();
    }

    public void addOrUpdate(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        if (checkOjectExist(note.getId())){
            value.put(KEY_TITLE, note.getTitle());
            value.put(KEY_NOTE, note.getNote());
            value.put(KEY_TIME, note.getTime());
            value.put(KEY_COLOR, note.getColor());
            db.update(TABLE_NAME, value, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
        }
        else{
            value.put(KEY_TITLE, note.getTitle());
            value.put(KEY_NOTE, note.getNote());
            value.put(KEY_TIME, note.getTime());
            value.put(KEY_COLOR, note.getColor());
            db.insert(TABLE_NAME, null, value);
        }
        db.close();
    }

    public void updateArchive(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        if (checkOjectExistOfArchive(note.getId())){
            value.put(KEY_TITLE3, note.getTitle());
            value.put(KEY_NOTE3, note.getNote());
            value.put(KEY_TIME3, note.getTime());
            value.put(KEY_COLOR3, note.getColor());
            db.update(TABLE_NAME3, value, KEY_ID3 + "=?", new String[]{String.valueOf(note.getId())});
        }
        db.close();
    }

    public void delete(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[] {String.valueOf(note.getId())});
        db.close();
    }

    public void deleteObjectOfTrash(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, KEY_ID2 + "=?", new String[] {String.valueOf(note.getId())});
        db.close();
    }

    public void deleteArchive(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME3, KEY_ID3 + "=?", new String[] {String.valueOf(note.getId())});
        db.close();
    }


    public boolean checkOjectExist(int id){
        boolean hasObject = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + KEY_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});
        if(cursor.getCount() >0){
            hasObject = true;
        }
        return hasObject;
    }

    public boolean checkOjectExistOfArchive(int id){
        boolean hasObject = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME3 + " WHERE " + KEY_ID3 + "=?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});
        if(cursor.getCount() >0){
            hasObject = true;
        }
        return hasObject;
    }

    public ArrayList<Note> getAllNote() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setColor(cursor.getInt(4));
                // Thêm vào danh sách.
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        // return note list
        return noteList;
    }

    public ArrayList<Note> getArchive(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME3;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setColor(cursor.getInt(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }


    public ArrayList<Note> getTrash(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setTime(cursor.getString(3));
                note.setColor(cursor.getInt(4));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_note_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME, KEY_ID, KEY_TITLE, KEY_NOTE, KEY_TIME, KEY_COLOR);
        sqLiteDatabase.execSQL(create_note_table);

        String create_trash_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME2, KEY_ID2, KEY_TITLE2, KEY_NOTE2, KEY_TIME2, KEY_COLOR2);
        sqLiteDatabase.execSQL(create_trash_table);

        String create_archive_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME3, KEY_ID3, KEY_TITLE3, KEY_NOTE3, KEY_TIME3, KEY_COLOR3);
        sqLiteDatabase.execSQL(create_archive_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_note_table = String.format("Drop table if exist!", TABLE_NAME);
        sqLiteDatabase.execSQL(drop_note_table);

        String drop_trash_table = String.format("Drop table if exist!", TABLE_NAME2);
        sqLiteDatabase.execSQL(drop_trash_table);

        String drop_archive_table = String.format("Drop table if exist!", TABLE_NAME3);
        sqLiteDatabase.execSQL(drop_archive_table);

        onCreate(sqLiteDatabase);
    }
}

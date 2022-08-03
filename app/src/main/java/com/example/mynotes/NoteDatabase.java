package com.example.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NoteDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME="Notes";
    public static final int DB_VERSION=1;
    public NoteDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE AddNote(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , note TEXT, date TEXT UNIQUE, password TEXT   )");
        sqLiteDatabase.execSQL("CREATE TABLE LockedNote(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , note TEXT, date TEXT UNIQUE, password TEXT   )");
        sqLiteDatabase.execSQL("CREATE TABLE FavoriteNote(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , note TEXT, date TEXT UNIQUE, password TEXT   )");
        sqLiteDatabase.execSQL("CREATE TABLE DeletedNote(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , note TEXT, date TEXT UNIQUE, password TEXT   )");
        sqLiteDatabase.execSQL("CREATE TABLE ArchiveNote(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT , note TEXT, date TEXT UNIQUE, password TEXT   )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AddNote");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FavoriteNote");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DeletedNote");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ArchiveNote");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LockedNote");
        onCreate(sqLiteDatabase);
    }


    public boolean insertNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        if(!note.getPassword().equals("00000")){
            insertLockedNote(note);
        }
        long r = dp.insert("AddNote",null,values);
        return r!=-1;
    }


    public boolean insertLockedNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());

        long r = dp.insert("LockedNote",null,values);
        return r!=-1;
    }

    public boolean favoriteNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());

        long r = dp.insert("FavoriteNote",null,values);
        return r!=-1;
    }


    public long getNoteCount(){
        SQLiteDatabase dp=getReadableDatabase();
        return DatabaseUtils.queryNumEntries(dp,"AddNote");
    }

    public long getFavoriteCount(){
        SQLiteDatabase dp=getReadableDatabase();
        return DatabaseUtils.queryNumEntries(dp,"FavoriteNote");
    }

    public long getDeletedCount(){
        SQLiteDatabase dp=getReadableDatabase();
        return DatabaseUtils.queryNumEntries(dp,"DeletedNote");
    }

    public long getArchiveCount(){
        SQLiteDatabase dp=getReadableDatabase();
        return DatabaseUtils.queryNumEntries(dp,"ArchiveNote");
    }


    public boolean unFavoriteNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getDate()};
        int result=dp.delete("FavoriteNote", "date=?",args);

        return (result>0);
    }

    public void deleteFavoriteNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("FavoriteNote", "title=?",args);

    }
    public void deleteLockedNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("LockedNote", "title=?",args);
    }


    public boolean deleteNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("AddNote", "title=?",args);
        deleteFavoriteNote(note);
        deleteLockedNote(note);
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        long r = dp.insert("DeletedNote",null,values);
        return (result>0&&r!=-1);
    }


    public boolean deleteOneNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("DeletedNote", "title=?",args);

        return (result>0);
    }


    public void deleteAllDeletedNote(){
        SQLiteDatabase dp=getWritableDatabase();
        //String args[]={note.getTitle()};
        Cursor c=dp.rawQuery("SELECT * from DeletedNote",null );
        if(c.moveToFirst()){
            do{
                dp.delete("DeletedNote","title=?", new String[]{c.getString(1)});

            }while (c.moveToNext());
        }

    }


    public boolean ArchivedNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("AddNote", "title=?",args);
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        long r = dp.insert("ArchiveNote",null,values);

        return (result>0&&r!=-1);
    }

    public boolean unArchivedNote(Note note){
        SQLiteDatabase dp=getWritableDatabase();
        String args[]={note.getTitle()};
        int result=dp.delete("ArchiveNote", "title=?",args);
        insertNote(note);

        return (result>0);
    }


    public ArrayList<Note>getAllNotes(){
        ArrayList<Note>allNotes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from AddNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                allNotes.add(note1);
            }while (c.moveToNext());
        }
        return allNotes;
    }

    public ArrayList<Note>getDeletedNotes(){
        ArrayList<Note>allNotes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from DeletedNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                allNotes.add(note1);
            }while (c.moveToNext());
        }
        return allNotes;
    }

    public ArrayList<Note>getArchivedNotes(){
        ArrayList<Note>allNotes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from ArchiveNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                allNotes.add(note1);
            }while (c.moveToNext());
        }
        return allNotes;
    }




    public ArrayList<Note>getFavoriteNotes(){
        ArrayList<Note>allNotes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from FavoriteNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                allNotes.add(note1);
            }while (c.moveToNext());
        }
        return allNotes;
    }


    public ArrayList<Note>getLockedNotes(){
        ArrayList<Note>allNotes=new ArrayList<>();
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from LockedNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                allNotes.add(note1);
            }while (c.moveToNext());
        }
        return allNotes;
    }


    public boolean updateNote(Note note){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        String args[]={note.getDate()};
        int r = db.update("AddNote",values,"date=?",args);
        updateFavoriteNote(note);
        updateLockedNote(note);
        return r>0;
    }


    public void restoreAllDeletedNote(){

        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from DeletedNote",null );
        if(c.moveToFirst()){
            do{
                String title=c.getString(1);
                String note=c.getString(2);
                String date=c.getString(3);
                String password=c.getString(4);
                Note note1=new Note(title,note,date,password);
                insertNote(note1);
            }while (c.moveToNext());
        }
        deleteAllDeletedNote();


    }


    public void updateLockedNote(Note note){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        String args[]={note.getDate()};
        int r = db.update("LockedNote",values,"date=?",args);
    }


    public void updateFavoriteNote(Note note){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("title",note.getTitle());
        values.put("note",note.getNote());
        values.put("date",note.getDate());
        values.put("password",note.getPassword());
        String args[]={note.getDate()};
        int r = db.update("FavoriteNote",values,"date=?",args);
    }

    public Note findNote(String date){
        Note n = null;
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from AddNote",null );
        if(c.moveToFirst()){
            do{
                if(date.equals(c.getString(3))){
                    n=new Note(c.getString(1),c.getString(2),date,c.getString(4));
                    break;
                }
            }while (c.moveToNext());
        }
        return n;
    }

    public Note findDeletedNote(String date){
        Note n = null;
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from DeletedNote",null );
        if(c.moveToFirst()){
            do{
                if(date.equals(c.getString(3))){
                    n=new Note(c.getString(1),c.getString(2),date,c.getString(4));
                    break;
                }
            }while (c.moveToNext());
        }
        return n;
    }

    public Note findArchivedNote(String date){
        Note n = null;
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from ArchiveNote",null );
        if(c.moveToFirst()){
            do{
                if(date.equals(c.getString(3))){
                    n=new Note(c.getString(1),c.getString(2),date,c.getString(4));
                    break;
                }
            }while (c.moveToNext());
        }
        return n;
    }


    public Note findFavoriteNote(String date){
        Note n = null;
        SQLiteDatabase dp=getReadableDatabase();
        Cursor c=dp.rawQuery("SELECT * from FavoriteNote",null );
        if(c.moveToFirst()){
            do{
                if(date.equals(c.getString(3))){
                    n=new Note(c.getString(1),c.getString(2),date,c.getString(4));
                    break;
                }
            }while (c.moveToNext());
        }
        return n;
    }
}

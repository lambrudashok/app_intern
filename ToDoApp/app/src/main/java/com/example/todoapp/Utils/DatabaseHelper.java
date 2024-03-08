package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase dbs;
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        dbs = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase dbs) {
        dbs.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbs, int oldVersion, int newVersion) {
        dbs.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(dbs);
    }
   public void insertTask(ToDoModel model){
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTask());
        values.put(COL_3 , 0);
        dbs.insert(TABLE_NAME , null , values);
   }

   public void updateTask(int id , String task){
        dbs = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , task);
        dbs.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
   }
   public void updateStatus(int id, int status){
       dbs = this.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COL_3 , status);
       dbs.update(TABLE_NAME , values , "ID=?", new String[]{String.valueOf(id)});
   }

   public void deleteTask(int id){
        dbs.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
   }

   @SuppressLint("Range")
   public List<ToDoModel>getAllTasks(){
        dbs = this.getWritableDatabase();
       Cursor cursor = null;
       List<ToDoModel>modelList = new ArrayList<>();
       dbs.beginTransaction();
       try {
         cursor = dbs.query(TABLE_NAME , null , null , null ,null , null , null);
         if(cursor != null){
             if(cursor.moveToFirst()){
                 do{
                     ToDoModel task = new ToDoModel();
                     task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                     task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                     task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                     modelList.add(task);
                 }while (cursor.moveToNext());
             }
         }
       }finally {
           dbs.endTransaction();
           cursor.close();
       }
       return modelList;
   }
}

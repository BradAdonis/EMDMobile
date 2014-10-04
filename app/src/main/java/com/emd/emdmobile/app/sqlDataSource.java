package com.emd.emdmobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class sqlDataSource {
    private SQLiteDatabase db;
    private sqlDatabase dbHelper;
    private String[] allColumns = {sqlDatabase.COLUMN_ID, sqlDatabase.COLUMN_UNID, sqlDatabase.COLUMN_MODDATE, sqlDatabase.COLUMN_TYPE, sqlDatabase.COLUMN_KEYWORDS, sqlDatabase.COLUMN_JSON};
    private String listLimits;

    public sqlDataSource(Context context){
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            listLimits = pm.getString("pref_key_ui_limit",null);
        }
        catch(Exception e){listLimits = "50";};

        dbHelper = new sqlDatabase(context);
    }

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long insertObject(sqlObject obj){
        ContentValues values = new ContentValues();
        values.put(sqlDatabase.COLUMN_UNID, obj.getUnid());
        values.put(sqlDatabase.COLUMN_MODDATE, obj.getModDate());
        values.put(sqlDatabase.COLUMN_TYPE, obj.getType());
        values.put(sqlDatabase.COLUMN_KEYWORDS, obj.getKeywords());
        values.put(sqlDatabase.COLUMN_JSON, obj.getJson());
        long id = db.insert(sqlDatabase.TABLE_OBJECT,null,values);
        return id;
    }

    public long deleteObject(sqlObject obj){
        List<String> args = new ArrayList<String>();

        if(obj.getType() != null){
            args.add(obj.getType());
        }

        if(obj.getUnid() != null){
            args.add(obj.getUnid());
        }

        String[] argsarray = new String[args.size()];
        int argCount = args.size();
        int i = 0;
        argsarray = args.toArray(argsarray);

        switch(argCount){
            case 0:
                i = db.delete(sqlDatabase.TABLE_OBJECT,null,null);
                break;

            case 1:
                i = db.delete(sqlDatabase.TABLE_OBJECT,sqlDatabase.COLUMN_TYPE + " = ?",argsarray);
                break;

            case 2:
                i = db.delete(sqlDatabase.TABLE_OBJECT,sqlDatabase.COLUMN_TYPE + " = ? and " + sqlDatabase.COLUMN_UNID + " = ? ",argsarray);
                break;
        }

        return Long.valueOf(i);
    }

    public List<sqlObject> getAllObjects(sqlObject obj){
        List<sqlObject> objs = new ArrayList<sqlObject>();
        List<String> args = new ArrayList<String>();

        if(obj.getType() != null){
            args.add(obj.getType());
        }

        if(obj.getKeywords() != null){
            args.add("%" + obj.getKeywords().toLowerCase() + "%");
        }

        String[] argsarray = new String[args.size()];
        int argCount = args.size();
        argsarray = args.toArray(argsarray);
        Cursor cursor = null;

        switch(argCount){
            case 0:
                cursor = db.query(sqlDatabase.TABLE_OBJECT, allColumns, null,null,null,null, sqlDatabase.COLUMN_MODDATE + " DESC",listLimits);
                break;

            case 1:
                cursor = db.query(sqlDatabase.TABLE_OBJECT, allColumns, sqlDatabase.COLUMN_TYPE + " = ?", argsarray, null, null, sqlDatabase.COLUMN_MODDATE + " DESC",listLimits);
                break;

            case 2:
                cursor = db.query(sqlDatabase.TABLE_OBJECT, allColumns, sqlDatabase.COLUMN_TYPE + " = ? and " + sqlDatabase.COLUMN_KEYWORDS + " LIKE ?", argsarray, null, null, sqlDatabase.COLUMN_MODDATE + " DESC",listLimits);
                break;
        }


        int count = cursor.getCount();

        if(count > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                sqlObject Obj = cursorToObj(cursor);
                objs.add(Obj);
                cursor.moveToNext();
            }
        }

        return objs;
    }

    private sqlObject cursorToObj(Cursor cursor){
        sqlObject Obj = new sqlObject();
        Obj.setID(cursor.getLong(0));
        Obj.setUnid(cursor.getString(1));
        try {
            String d = cursor.getString(2);
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss ZZZ yyyy");
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Obj.setModDate(df.parse(d));
        }
        catch(ParseException e){}
        Obj.setType(cursor.getString(3));
        Obj.setKeywords(cursor.getString(4));
        Obj.setJson(cursor.getString(5));
        return Obj;
    }
}

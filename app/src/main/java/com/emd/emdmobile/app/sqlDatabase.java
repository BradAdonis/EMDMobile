package com.emd.emdmobile.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bradl_000 on 2014-07-30.
 */
public class sqlDatabase extends SQLiteOpenHelper {

    public static final String TABLE_OBJECT = "emdobjects";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UNID = "unid";
    public static final String COLUMN_MODDATE = "modifieddate";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_KEYWORDS = "keywords";
    public static final String COLUMN_JSON = "json";

    private static final String DATABASE_NAME = "emd.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE = "create table " + TABLE_OBJECT + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_UNID +
            " text not null, " + COLUMN_MODDATE + " text not null, " + COLUMN_TYPE + " text not null, " + COLUMN_KEYWORDS + " text not null, " + COLUMN_JSON + " text not null);";

    public sqlDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECT);
        onCreate(db);
    }

}

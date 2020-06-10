package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetDataEntry;

public class PetDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shelter.db";

    private static final String TEXT_TYPE = "TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + PetDataEntry.TABLE_NAME + "(" +
            PetDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PetDataEntry.COLUMN_PET_Name + TEXT_TYPE + COMMA_SEP +
            PetDataEntry.COLUMN_PET_BREED + TEXT_TYPE + COMMA_SEP + PetDataEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL DEFAULT 3,"+
            PetDataEntry.COLUMN_PET_WEIGHT + "INTEGER NOT NULL DEFAULT 0"+ ");";

    public PetDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


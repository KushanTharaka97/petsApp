package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetDataEntry;

public class PetDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "shelter.db";



    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetDataEntry.TABLE_NAME + " ("
                + PetDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetDataEntry.COLUMN_PET_NAME + "  TEXT NOT NULL, "
                + PetDataEntry.COLUMN_PET_BREED + "  TEXT,"
                + PetDataEntry.COLUMN_PET_GENDER + "  INTEGER NOT NULL DEFAULT 3, "
                + PetDataEntry.COLUMN_PET_WEIGHT + "  INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DELETE ENTRIES
        String SQL_DELETE_ENTRIES = " DROP TABLE IF EXISTS " + PetDataEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}


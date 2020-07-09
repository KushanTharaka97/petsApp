package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.pets.data.PetContract.PetDataEntry;

public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;


    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+ "/#" ,PET_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
   private PetDbHelper mPetDbHelperObject;

    @Override
    public boolean onCreate() {

        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mPetDbHelperObject  = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mPetDbHelperObject.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                cursor = database.query(PetDataEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case PET_ID:
                selection = PetDataEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetDataEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot querry unknown uri :"+ uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
            final int match = sUriMatcher.match(uri);
            switch(match){
                case PETS:
                    return insertPet(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Insertion Not Suppor for "+ uri);
        }


    }

    private Uri insertPet(Uri uri, ContentValues values){

        String name = values.getAsString(PetDataEntry.COLUMN_PET_NAME);
        if(name == null){
            throw new IllegalArgumentException("Pet require a name");
        }

        Integer gender = values.getAsInteger(PetDataEntry.COLUMN_PET_GENDER);
        if( gender == null || !PetDataEntry.isValidGender(gender)){
            throw new IllegalArgumentException("Valid Gender Required");
        }

        Integer weight = values.getAsInteger(PetDataEntry.COLUMN_PET_WEIGHT);
        if(weight == null || weight<0){
            throw new IllegalArgumentException("Must be higher than 0 Kg");
        }

        SQLiteDatabase database = mPetDbHelperObject.getWritableDatabase();
        // https://developer.android.com/guide/topics/providers/content-provider-basics#Inserting

        //sanity check: input validation

        // TODO: Insert a new pet into the pets database table with the given ContentValues
        // Defines an object to contain the new values to insert
       long id = database.insert(PetDataEntry.TABLE_NAME,null,values);

       if(id == -1){
           Log.e(LOG_TAG,"Failed to insert "+ uri);
           return null;
       }
       return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    final int match = sUriMatcher.match(uri);

    switch(match){
        case PETS:
            return update(uri,values,selection,selectionArgs);

        case PET_ID:
            selection = PetDataEntry._ID+"=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

            return updatePet(uri,values,selection,selectionArgs);

        default:
            throw new IllegalArgumentException("Update is not support for "+ uri);
    }

    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        //sanity check
        String name = values.getAsString(PetDataEntry.COLUMN_PET_NAME);
        if(name == null){
            throw new IllegalArgumentException("Pet require a name");
        }

        Integer gender = values.getAsInteger(PetDataEntry.COLUMN_PET_GENDER);
        if(gender == null || !PetDataEntry.isValidGender(gender)){
            throw new IllegalArgumentException("Valid Gender Required");
        }

        Integer weight = values.getAsInteger(PetDataEntry.COLUMN_PET_WEIGHT);
        if(weight<0 || weight == null){
            throw new IllegalArgumentException("Must be higher than 0 Kg");
        }

        if(values.size()==0){
            return 0;
        }

        SQLiteDatabase databaseForUpdatePet = mPetDbHelperObject.getWritableDatabase();


        // Returns the number of database rows affected by the update statement
        long id = databaseForUpdatePet.update(PetDataEntry.TABLE_NAME,values,selection,selectionArgs);

        if(id == -1){
            Log.e(LOG_TAG,"Failed to insert "+ uri);
            return 0;
        }

        //return ContentUris.withAppendedId(uri, id);
        return 0;
    }
}

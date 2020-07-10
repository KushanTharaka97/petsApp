/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.android.pets.data.PetContract.PetDataEntry;
import com.example.android.pets.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
   // private PetDbHelper mDbHelper;

    //identify a certain loader
    private static final int PET_LOADER = 0;

    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //display in List
        ListView petDisplayList = findViewById(R.id.lvItems);

        //set empty view
        View emptyView = findViewById(R.id.empty_view);
        petDisplayList.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this, null);
        petDisplayList.setAdapter(mCursorAdapter);

        //kick off the loader
      //getLoaderManager().initLoader(PET_LOADER,null,this);


        LoaderManager.getInstance(this).initLoader(PET_LOADER, null, this);
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        //  SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                PetDataEntry._ID,
                PetDataEntry.COLUMN_PET_NAME,
                PetDataEntry.COLUMN_PET_GENDER,
                PetDataEntry.COLUMN_PET_BREED,
                PetDataEntry.COLUMN_PET_WEIGHT
        };

        Cursor cursor = getContentResolver().query(
                PetDataEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
     PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);
     petDisplayList.setAdapter(adapter);


    }
     */

    private void InsertData() {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetDataEntry.COLUMN_PET_NAME, "TotoTest");
        values.put(PetDataEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetDataEntry.COLUMN_PET_GENDER, PetDataEntry.GENDER_MALE);
        values.put(PetDataEntry.COLUMN_PET_WEIGHT, 9);


        //   Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(PetDataEntry.CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                InsertData();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        //projection defined
        String[] projection = {
                PetDataEntry._ID,
                PetDataEntry.COLUMN_PET_NAME,
                PetDataEntry.COLUMN_PET_BREED
        };
        return new CursorLoader(this, PetDataEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //update with the new cursor
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //clear the cursor
        mCursorAdapter.swapCursor(null);
    }
}

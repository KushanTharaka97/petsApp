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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetDataEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private PetDbHelper mDbHelper;

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
        mDbHelper = new PetDbHelper(this);
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PetDataEntry.COLUMN_PET_NAME,
                PetDataEntry.COLUMN_PET_GENDER,
                PetDataEntry.COLUMN_PET_BREED,
                PetDataEntry.COLUMN_PET_WEIGHT,
                PetDataEntry._ID
        };

        //new query type called "query for security reasons"
        /**Cursor cursor = db.query(PetDataEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
         */
        Cursor cursor = getContentResolver().query(
                PetDataEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );


        try {
            //count of the table rows
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
            //declare top columns of each row data displaying
            displayView.append("\n"+PetDataEntry._ID+" - "
                    + PetDataEntry.COLUMN_PET_NAME+ " - "
                    +PetDataEntry.COLUMN_PET_BREED+" - "
                    +PetDataEntry.COLUMN_PET_GENDER+" - "
                    +PetDataEntry.COLUMN_PET_WEIGHT);

            //figureout the index of each column
            int idColumnIndex = cursor.getColumnIndex(PetDataEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetDataEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetDataEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetDataEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetDataEntry.COLUMN_PET_WEIGHT);

            //iterate the cursor included data to display
            while (cursor.moveToNext()){

                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);

                //gender trasnfering number to text according to previous declarations
                String currentGenderDisplay;
                if(currentGender == 1){
                    currentGenderDisplay = "MALE";
                }else if(currentGender == 2){
                    currentGenderDisplay = "FEMALE";
                }else{
                    currentGenderDisplay = "UNKNOWN";
                }
                //display view of the cursor
                displayView.append(
                        ("\n"+ currentId + " - "
                        + currentName + " - "
                        + currentBreed + " - "
                        + currentGenderDisplay + " - "
                        + currentWeight + "Kg")
                );

            }

            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).


        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }


    private void InsertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetDataEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetDataEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetDataEntry.COLUMN_PET_GENDER, PetDataEntry.GENDER_MALE);
        values.put(PetDataEntry.COLUMN_PET_WEIGHT, 7);


        long newRowId = db.insert(PetDataEntry.TABLE_NAME, null, values);
        Log.i("CatalogActivity", "New Row Id" + newRowId);
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
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

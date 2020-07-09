package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {

    public static abstract class PetDataEntry implements BaseColumns{

    public static final String TABLE_NAME = "pets";

        public static final String _ID = "_petId";

        public static final String COLUMN_PET_NAME ="name";

        public static final String COLUMN_PET_BREED ="breed";

        public static final String COLUMN_PET_GENDER ="gender";

        public static final String COLUMN_PET_WEIGHT ="weight";

        //initialize gender

        public static final int GENDER_MALE = 1;

        public static final int GENDER_FEMALE = 2;

        public static final int GENDER_UNKNOWN = 3;

        //validation gender
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;



            /*
    Lastly, inside each of the Entry classes in the contract,
    we create a full URI for the class as a constant called CONTENT_URI.
     The Uri.withAppendedPath() method appends the BASE_CONTENT_URI
    (which contains the scheme and the content authority) to the path segment.
     */

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

    }
    //Content Authority
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    //To make this a usable URI, we use the parse method which takes in a URI string and returns a Uri.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PETS = PetDataEntry.TABLE_NAME;

    //Complete CONTENT_URI

}

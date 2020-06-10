package com.example.android.pets.data;

import android.provider.BaseColumns;

public final class PetContract {

    public static abstract class PetDataEntry implements BaseColumns{
    public static final String TABLE_NAME = "pets";

        public static final String COLUMN_ID = "_petId";

        public static final String COLUMN_Name = "name";

        public static final String COLUMN_BREED = "breed";

        public static final String COLUMN_GENDER = "gender";

        public static final String COLUMN_WEIGHT = "weight";

        //initialize gender

        public static final int GENDER_MALE = 1;

        public static final int GENDER_FEMALE = 2;

        public static final int GENDER_UNKNOWN = 3;

    }

}

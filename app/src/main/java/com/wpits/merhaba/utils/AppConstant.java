// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.wpits.merhaba.utils;

public class AppConstant {
    public static final String COUNTRY = "COUNTRY";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String PhoneCode = "PhoneCode";
    public static final String PLUS = "+";
    public static final String NAVIGATED_FROM_INT = "navigated_from" ;
    public static final String SONG_DATA = "song_data" ;
    public interface DataKey{
        String NAVIGATED_FROM_INT = "navigated_from";
        String CATEGORY_ID = "category_id";
        String SONG_DATA = "song_data";

    }
    public interface Navigated{
        int FROM_CATEGORY = 1;
        int FROM_FAVOURITES= 2;

    }
    public interface Language{
        String ENGLISH = "en";
        String ARABIC= "ar";

    }

   public interface Prefrences {
        String USER_MOBILE = "USER_MOBILE";
        String USER_IS_LOGGED_IN = "USER_IS_LOGGED_IN";
       String USER_LANGUAGE = "USER_LANGUAGE";

       String ALL_CATEGORIES = "ALL_CATEGORIES";

       String IS_FIRST_RUN = "isFirstRun";
       String IS_FIREBASE_ID_UPDATED = "FirebaseIdUpdated";
       String USER_DATA = "user_data";

   }
    public interface Tabs {
        int SETTINGS = 2;
        int HOME = 0;
        int FAVOURITE = 1;
        int MORE = 3;

    }

}

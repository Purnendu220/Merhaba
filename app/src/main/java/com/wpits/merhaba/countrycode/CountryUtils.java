// Copyright (C) 2018 INTUZ.

// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
// the following conditions:

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
// ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
// THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.wpits.merhaba.countrycode;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;

import com.wpits.merhaba.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CountryUtils {
    private static final String TAG = CountryUtils.class.getSimpleName();

    private static List<Country> countries;
    private static Map<String, List<String>> timeZoneAndCountryISOs;

    /**
     * Returns image res based on country name code
     *
     * @param country selected country
     * @return drawable resource id of country flag.
     */
    @DrawableRes
    public static int getFlagDrawableResId(String country) {
        switch (country) {

            case "ly": //libya
                return R.drawable.flag_libya;

            default:
                return R.drawable.flag_libya;
        }
    }

    /**
     * Get all countries
     *
     * @param context caller context
     * @return List of Country
     */
    public static List<Country> getAllCountries(Context context) {
        if (countries != null) {
            return countries;
        }

        countries = new ArrayList<>();
        //TODO New Added


        countries.add(new Country(context.getString(R.string.country_libya_code),
                context.getString(R.string.country_libya_number),
                context.getString(R.string.country_libya_name)));


        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareToIgnoreCase(country2.getName());
            }
        });

        return countries;
    }

    /**
     * Finds country code by matching substring from left to right from full number.
     * For example. if full number is +819017901357
     * function will ignore "+" and try to find match for first character "8"
     * if any country found for code "8", will return that country. If not, then it will
     * try to find country for "81". and so on till first 3 characters ( maximum number of characters
     * in country code is 3).
     *
     * @param preferredCountries countries of preference
     * @param fullNumber         full number ( "+" (optional)+ country code + carrier number) i.e.
     *                           +819017901357 / 819017901357 / 918866667722
     * @return Country JP +81(Japan) for +819017901357 or 819017901357
     * Country IN +91(India) for  918866667722
     * null for 2956635321 ( as neither of "2", "29" and "295" matches any country code)
     */
    static Country getByNumber(Context context, List<Country> preferredCountries, String fullNumber) {
        int firstDigit;
        if (fullNumber.length() != 0) {
            if (fullNumber.charAt(0) == '+') {
                firstDigit = 1;
            } else {
                firstDigit = 0;
            }
            Country country;
            for (int i = firstDigit; i < firstDigit + 4; i++) {
                String code = fullNumber.substring(firstDigit, i);
                country = getByCode(context, preferredCountries, code);
                if (country != null) {
                    return country;
                }
            }
        }
        return null;
    }

    /**
     * Search a country which matches @param code.
     *
     * @param preferredCountries list of country with priority,
     * @param code               phone code. i.e 91 or 1
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     */
    static Country getByCode(Context context, List<Country> preferredCountries, int code) {
        return getByCode(context, preferredCountries, code + "");
    }

    /**
     * Search a country which matches @param code.
     *
     * @param preferredCountries is list of preference countries.
     * @param code               phone code. i.e "91" or "1"
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     * if same code (e.g. +1) available for more than one country ( US, canada) , this function will
     * return preferred country.
     */
    private static Country getByCode(Context context, List<Country> preferredCountries, String code) {

        //check in preferred countries first
        if (preferredCountries != null && !preferredCountries.isEmpty()) {
            for (Country country : preferredCountries) {
                if (country.getPhoneCode().equals(code)) {
                    return country;
                }
            }
        }

        for (Country country : CountryUtils.getAllCountries(context)) {
            if (country.getPhoneCode().equals(code)) {
                return country;
            }
        }
        return null;
    }


    /**
     * Search a country which matches @param nameCode.
     *
     * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     */
    static Country getByNameCodeFromCustomCountries(Activity activity,
                                                    List<Country> customCountries,
                                                    String nameCode) {
        if (customCountries == null || customCountries.size() == 0) {
            return getByNameCodeFromAllCountries(activity, nameCode);
        } else {
            for (Country country : customCountries) {
                if (country.getIso().equalsIgnoreCase(nameCode)) {
                    return country;
                }
            }
        }
        return null;
    }

    /**
     * Search a country which matches @param nameCode.
     *
     * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
     * @return Country that has phone code as @param code.
     * or returns null if no country matches given code.
     */
    public static Country getByNameCodeFromAllCountries(Activity activity, String nameCode) {
        List<Country> countries = CountryUtils.getAllCountries(activity);
        for (Country country : countries) {
            if (country.getIso().equalsIgnoreCase(nameCode)) {
                return country;
            }
        }
        return null;
    }




    /**
     * Return list of Map for timezone and iso country.
     *
     * @param context
     * @return List of timezone and country.
     */

}

package it.unimib.buildyourholiday.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

public class MapUtil {
    public static List<String> getAllCountries(List<Travel> travels) {
        List<String> countries = new ArrayList<>();
        for (Travel t: travels) {
            if(!countries.contains(t.getCountry())) {
                countries.add(t.getCountry());
                Log.d("MapFragment","added value for: "+t.getCountry());
            }
        }
        return countries;
    }

    public static String getAllCountriesFormatted(List<Travel> travels) {
        List<String> countries = getAllCountries(travels);
        String result = "";
        for (String s: countries) {
            if(countries.indexOf(s)==0)
                result += "\"" + s + "\"";
            else
                result += ", \"" + s + "\"";
        }
        return result;
    }
}

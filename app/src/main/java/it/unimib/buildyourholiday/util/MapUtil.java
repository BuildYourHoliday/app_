package it.unimib.buildyourholiday.util;

import static it.unimib.buildyourholiday.util.Constants.BOOKED_COUNTRIES_LAYER;
import static it.unimib.buildyourholiday.util.Constants.SAVED_COUNTRIES_LAYER;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.mapbox.bindgen.Expected;
import com.mapbox.bindgen.Value;
import com.mapbox.common.ValueConverter;
import com.mapbox.maps.MapView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.MapFragment;
import it.unimib.buildyourholiday.TravelViewModel;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;

public class MapUtil {
    public static List<String> getAllCountries(List<Travel> travels) {
        List<String> countries = new ArrayList<>();
        for (Travel t: travels) {
            if(!countries.contains(t.getCountry()) && t.getCountry()!=null) {
                countries.add(t.getCountry());
                Log.d("MapFragment","added value for: "+t.getCountry());
            } else
                Log.d("MapFragment","refused value for: "+t.getCountry());
        }
        Log.d("MapFragment","total ids: " + countries.size());
        return countries;
    }

    public static String getAllCountriesFormatted(List<Travel> travels) {
        List<String> countries = getAllCountries(travels);

        String result = "";
        for (String s: countries) {
            Log.d("MapFragment","analyzing " + s);
            if(countries.indexOf(s)==0)
                result += "\"" + s + "\"";
            else
                result += ", \"" + s + "\"";
        }
        Log.d("MapFragment","built string: "+result);
        return result;
    }

    public static void resetCountries(String countryLayer, @NonNull MapView mapView) {
        Expected<String, Value> defaultConv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                "[\"get\", \"iso_3166_1\"],\n" + "[\"\"],\n" + "true,\n" +
                "false\n" + "]");
        Log.d("MapFragment",defaultConv.getValue().toString());
        mapView.getMapboxMap().getStyle().
                setStyleLayerProperty(countryLayer,"filter",defaultConv.getValue());
        Log.d("MapFragment","property default: "+mapView.getMapboxMap().getStyle().
                getStyleLayerProperty(countryLayer,"filter"));
    }

    public static void fetchSavedCountriesFromDB(@NonNull MapView mapView, @NonNull TravelViewModel model, @NonNull LifecycleOwner lifecycleOwner) {
        model.fetchAllSavedTravels();
        model.getSavedTravelsLiveData(false).observe(lifecycleOwner,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        String countries = getAllCountriesFormatted(((Result.TravelResponseSuccess)result).getData().getTravelList());
                        Log.d("MapFragment","received "+((Result.TravelResponseSuccess)result).getData().getTravelList().size()+" results");
                        Log.d("MapFragment","saved ids: "+countries);
                        Expected<String,Value> conv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                                "[\"get\", \"iso_3166_1\"],\n" + "["+ (countries) +"],\n" + "true,\n" +
                                "false\n" + "]");
                        Log.d("MapFragment",conv.getValue().toString());
                        mapView.getMapboxMap().getStyle().setStyleLayerProperty(SAVED_COUNTRIES_LAYER,
                                "filter",conv.getValue());
                        Log.d("MapFragment","property: "+mapView.getMapboxMap().getStyle().getStyleLayerProperty(SAVED_COUNTRIES_LAYER,"filter"));
                        model.getSavedTravelsLiveData(false).removeObserver(this);
                    }
                });
    }

    public static void fetchBookedCountriesFromDB(@NonNull MapView mapView, @NonNull TravelViewModel model, @NonNull LifecycleOwner lifecycleOwner) {
        model.fetchAllBookedTravels();
        model.getBookedTravelsLiveData(false).observe(lifecycleOwner,
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        String countries = getAllCountriesFormatted(((Result.TravelResponseSuccess)result).getData().getTravelList());
                        Log.d("MapFragment","booked ids: "+countries);
                        Expected<String,Value> conv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                                "[\"get\", \"iso_3166_1\"],\n" + "["+ (countries) +"],\n" + "true,\n" +
                                "false\n" + "]");
                        Log.d("MapFragment",conv.getValue().toString());
                        mapView.getMapboxMap().getStyle().setStyleLayerProperty(BOOKED_COUNTRIES_LAYER,
                                "filter",conv.getValue());
                        Log.d("MapFragment","property: "+mapView.getMapboxMap().getStyle().getStyleLayerProperty(BOOKED_COUNTRIES_LAYER,"filter"));
                        model.getBookedTravelsLiveData(false).removeObserver(this);
                    }
                });
    }

    public static void initMap(@NonNull MapView mapView,@NonNull TravelViewModel model,@NonNull LifecycleOwner lifecycleOwner) {
        if(isMapEmpty(mapView)) {
            fetchSavedCountriesFromDB(mapView, model, lifecycleOwner);
            fetchBookedCountriesFromDB(mapView, model, lifecycleOwner);
        }
    }

    public static void refreshMap(@NonNull MapView mapView,@NonNull TravelViewModel model,@NonNull LifecycleOwner lifecycleOwner) {
        resetCountries(SAVED_COUNTRIES_LAYER, mapView);
        resetCountries(BOOKED_COUNTRIES_LAYER, mapView);
        fetchSavedCountriesFromDB(mapView, model, lifecycleOwner);
        fetchBookedCountriesFromDB(mapView, model, lifecycleOwner);
    }

    private static boolean isMapEmpty(MapView mapView) {
        if(mapView.getMapboxMap().getStyle().getStyleLayerProperty(SAVED_COUNTRIES_LAYER,"filter").toString().contains("BBBB")
        && mapView.getMapboxMap().getStyle().getStyleLayerProperty(BOOKED_COUNTRIES_LAYER,"filter").toString().contains("GGGG"))
            return true;

        return false;
    }
}

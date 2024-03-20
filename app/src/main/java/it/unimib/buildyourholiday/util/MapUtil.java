package it.unimib.buildyourholiday.util;

import static it.unimib.buildyourholiday.util.Constants.BOOKED_COUNTRIES_LAYER;
import static it.unimib.buildyourholiday.util.Constants.SAVED_COUNTRIES_LAYER;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.mapbox.bindgen.Expected;
import com.mapbox.bindgen.Value;
import com.mapbox.common.ValueConverter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.QueriedFeature;
import com.mapbox.maps.QueryFeaturesCallback;
import com.mapbox.maps.RenderedQueryGeometry;
import com.mapbox.maps.RenderedQueryOptions;
import com.mapbox.maps.extension.observable.eventdata.StyleLoadedEventData;
import com.mapbox.maps.plugin.delegates.listeners.OnStyleLoadedListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.MapFragment;
import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.TravelViewModel;
import it.unimib.buildyourholiday.TravelViewModelFactory;
import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;

public class MapUtil {
    private static TravelViewModel travelViewModel;

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
        if(mapView.getMapboxMap().getStyle()==null)
            return true;
        if(mapView.getMapboxMap().getStyle().getStyleLayerProperty(SAVED_COUNTRIES_LAYER,"filter").toString().contains("BBBB")
        && mapView.getMapboxMap().getStyle().getStyleLayerProperty(BOOKED_COUNTRIES_LAYER,"filter").toString().contains("GGGG"))
            return true;

        return false;
    }

    public static void selectCountry(@NonNull MapView mapView, boolean refreshTriggered, @NonNull Point point, FragmentActivity activity,
                                     LifecycleOwner lifecycleOwner,TextView selectedCountry, RecyclerView recyclerView, TravelListAdapter.OnItemClickListener listener) {
        if(travelViewModel==null) {
            ITravelRepository travelRepository = ServiceLocator.getInstance()
                    .getTravelRepository(activity.getApplication());
            travelViewModel = new ViewModelProvider(
                    activity,new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);
        }

        ArrayList<String> layers = new ArrayList<>();
        layers.add("country-non-visited");

        mapView.getMapboxMap().queryRenderedFeatures(
                new RenderedQueryGeometry(mapView.getMapboxMap().pixelForCoordinate(point)),
                new RenderedQueryOptions(layers, null),
                new QueryFeaturesCallback() {
                    @Override
                    public void run(@NonNull Expected<String, List<QueriedFeature>> features) {
                        Log.d("PRINT-FROM-CALLBACK", "response received");
                        List<QueriedFeature> queriedFeatures = features.getValue();
                        Log.d("PRINT-FROM-CALLBACK", "data fetched");

                        String countryName = "", countryCode = "";
                        if (queriedFeatures != null) {
                            Log.d("PRINT-FROM-CALLBACK", "not null");
                            for (QueriedFeature queriedFeature : queriedFeatures) {
                                Feature feature = queriedFeature.getFeature();
                                Log.d("PRINT-FROM-CALLBACK", "got feature");
                                JsonObject properties = feature.properties();
                                Log.d("PRINT-FROM-CALLBACK", "got properties");

                                if (properties != null) {
                                    Log.d("PRINT-FROM-CALLBACK", properties.toString());

                                    countryName = properties.get("name_en").toString();
                                    countryCode = properties.get("iso_3166_1").toString();
                                    Log.d("PRINT-FROM-CALLBACK", "name: " + countryName +
                                            " code: " + countryCode);
                                    selectedCountry.setText(countryName);

                                    String searchCode = countryCode.substring(1,countryCode.length()-1);
                                    Log.d("MapFragment", "search: "+searchCode+" vs. ");
                                    travelViewModel.fetchSavedTravels(searchCode);

                                    travelViewModel.getTravelResponseLiveData().observe(lifecycleOwner, new Observer<Result>() {
                                        @Override
                                        public void onChanged(Result result) {
                                            if(((Result.TravelResponseSuccess)result).getData().getTravelList()!=null) {
                                                Log.d("MapFragment","risultati: "+((Result.TravelResponseSuccess)result).getData().getTravelList().size());
                                            }

                                            if(!refreshTriggered) {
                                                List<Travel> travelList = ((Result.TravelResponseSuccess)result).getData().getTravelList();

                                                TravelListAdapter travelListAdapter = new TravelListAdapter(travelList,listener,false);
                                                recyclerView.setAdapter(travelListAdapter);
                                            }
                                        }
                                    });

                                    selectCountry(mapView,point,countryCode);

                                    //break;
                                }
                            }
                        }

                        Value stringValueExpected =
                                mapView.getMapboxMap().getStyle().getStyleLayerProperty("country-selected","filter").getValue();

                        Log.d("STYLE-SET", "dopo getStyle " + stringValueExpected.toString());

                        Log.d("PRINT-FROM-CALLBACK","ended");
                    }
                }

        );
    }

    private static void selectCountry(MapView mapView, Point point, String countryCode) {
        // set border on selected
        Expected<String,Value> conv = ValueConverter.fromJson(
                "[\"match\", [\"get\", \"iso_3166_1\"], ["+countryCode+"], true, false]");
        mapView.getMapboxMap().getStyle().setStyleLayerProperty(
                "country-selected","filter",conv.getValue());

        // set zoom on selected
        CameraOptions cameraOptions =
                new CameraOptions.Builder().center(point).zoom(2.0).build();
        mapView.getMapboxMap().setCamera(cameraOptions);
    }

    public static void setMapTheme(MapView mapView, SharedPreferences sharedPreferences) {
        mapView.getMapboxMap().addOnStyleLoadedListener(new OnStyleLoadedListener() {
            @Override
            public void onStyleLoaded(@NonNull StyleLoadedEventData styleLoadedEventData) {
                if(sharedPreferences.getBoolean("darkMode", true)) {
                    Expected<String,Value> conv = ValueConverter.fromJson("\"hsl(0, 0%, 0%)\"");
                    mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","background-color",conv.getValue());
                    Log.d("STYLE-SET", conv.getValue().toString());
                }
                else {
                    Expected<String,Value> conv = ValueConverter.fromJson("\"hsl(0, 0%, 100%)\"");
                    mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","background-color",conv.getValue());
                }
            }
        });
    }
}

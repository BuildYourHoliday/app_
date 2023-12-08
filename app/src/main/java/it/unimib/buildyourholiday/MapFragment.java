package it.unimib.buildyourholiday;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.bindgen.Expected;
import com.mapbox.bindgen.Value;
import com.mapbox.common.ResourceLoadOptions;
import com.mapbox.common.TileStore;
import com.mapbox.common.ValueConverter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.maps.*;
import com.mapbox.maps.ResourceOptions;
import com.mapbox.maps.ResourceOptionsManager;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.observable.eventdata.StyleLoadedEventData;
import com.mapbox.maps.loader.MapboxMaps;
import com.mapbox.maps.loader.MapboxMapsInitializer;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.delegates.listeners.OnStyleLoadedListener;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import it.unimib.buildyourholiday.util.JsonFileReader;
import kotlin.jvm.JvmOverloads;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SharedPreferences sharedPreferences;
    String mapStyle = "map_style.json";
    JsonFileReader jsonFileReader;

    private MapView mapView = null;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // GeoJSONObject geoJSONObject = GeoJSON.parse();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ResourceOptionsManager.Companion.getDefault(getContext().getApplicationContext(),
                getString(R.string.mapbox_access_token));
        Log.d("MapFragment","dopo GETINSTANCE() + "+getString(R.string.mapbox_access_token));

        jsonFileReader = new JsonFileReader();



        View view = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = view.findViewById(R.id.map_view);
        //mapView.getMapboxMap().loadStyleUri(Style.LIGHT);

        mapView.getMapboxMap().loadStyleJson(jsonFileReader.readJsonFromAssets(getContext(), mapStyle));

        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        //if(sharedPreferences.getBoolean("darkMode", false)) {
        //mapView.getMapboxMap().getStyle().getStyleJSON();
        mapView.getMapboxMap().addOnStyleLoadedListener(new OnStyleLoadedListener() {
            @Override
            public void onStyleLoaded(@NonNull StyleLoadedEventData styleLoadedEventData) {
                /*Expected<String,Value> styleLayerProperties =
                mapView.getMapboxMap().getStyle().getStyleLayerProperties("background");

                Log.d("STYLE-SET", "dopo getStyle " + styleLayerProperties.getValue().toString());

                Value stringValueExpected =
                mapView.getMapboxMap().getStyle().getStyleLayerProperty("background","background-color").getValue();

                Log.d("STYLE-SET", "dopo getStyle " + stringValueExpected.toString());

                Expected<String,Value> conv = ValueConverter.fromJson("\"hsl(0, 0%, 0%)\"");
                if(conv != null)
                Log.d("STYLE-SET","DAJEEE");
                if(conv.getValue()!=null){
                    Log.d("STYLE-SET", "prima setStyle ");
                mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","background-color",conv.getValue());
                Log.d("STYLE-SET", "dopo setStyle ");}*/
                if(sharedPreferences.getBoolean("darkMode", true)) {
                    Expected<String,Value> conv = ValueConverter.fromJson("\"hsl(0, 0%, 0%)\"");
                    mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","background-color",conv.getValue());
                }
            }
        });

//        mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","paint",new Property<>("hsl(0, 0%, 0%"));

//            mapView.getMapboxMap().getStyle().getStyleLayerProperties("background").toString());
        // }


        TextView debugText = view.findViewById(R.id.debug_country);

        //mapView.getMapboxMap().loadStyleUri("asset://map_style.json");
        //mapView.getMapboxMap().loadStyleJson("asset://map_style.json");
        //mapView.getMapboxMap().loadStyleUri("mapbox://styles/choppadebug/clplsfl1a00ty01qt1pjsfphc");
        //mapView.getMapboxMap().loadStyleUri("mapbox://styles/choppadebug/clppp4jot013501o9bowr9776/draft");

        /*try {
            InputStream is = getActivity().getAssets().open("map_style.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String styleJson = new String(buffer, "UTF-8");
            mapView.getMapboxMap().loadStyleJson(styleJson);
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/



        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(new OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull Point point) {

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
                                if (queriedFeatures != null) {
                                    Log.d("PRINT-FROM-CALLBACK", "not null");
                                    for (QueriedFeature queriedFeature : queriedFeatures) {
                                        Feature feature = queriedFeature.getFeature();
                                        Log.d("PRINT-FROM-CALLBACK", "got feature");
                                        JsonObject properties = feature.properties();
                                        Log.d("PRINT-FROM-CALLBACK", "got properties");

                                        if (properties != null) {
                                            Log.d("PRINT-FROM-CALLBACK", properties.toString());

                                            String countryName = properties.get("name_en").toString();
                                            String countryCode = properties.get("iso_3166_1").toString();
                                            if (countryName != null && countryCode != null) {
                                                Log.d("PRINT-FROM-CALLBACK", "name: " + countryName +
                                                        " code: " + countryCode);
                                                debugText.setText("Country name: " + countryName +
                                                        "\ncountry code: " + countryCode);
                                                break;
                                            }
                                        }
                                    }
                                }

                                Log.d("PRINT-FROM-CALLBACK","ended");
                            }
                            }

                );

                return false;

            }
        });

        // Inflate the layout for this fragment
        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }
}
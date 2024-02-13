package it.unimib.buildyourholiday;

import static it.unimib.buildyourholiday.util.MapUtil.getAllCountriesFormatted;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Response;
import com.google.android.material.snackbar.Snackbar;
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

import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.TravelResponse;
import it.unimib.buildyourholiday.util.JsonFileReader;
import it.unimib.buildyourholiday.util.ServiceLocator;
import it.unimib.buildyourholiday.util.MapUtil;
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
 //   JsonFileReader jsonFileReader;
    private RecyclerView recyclerView;
    private TravelsRoomDatabase database;
    TravelListAdapter travelListAdapter;
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

      //  jsonFileReader = new JsonFileReader();



        View view = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = view.findViewById(R.id.map_view);
        //mapView.getMapboxMap().loadStyleUri(Style.LIGHT);

        //////////////////////////////////////////////
        ITravelRepository travelRepository = ServiceLocator.getInstance()
                .getTravelRepository(requireActivity().getApplication());
        TravelViewModel travelViewModel = new ViewModelProvider(
                requireActivity(),
                new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);



        mapView.getMapboxMap().loadStyleJson(JsonFileReader.readJsonFromAssets(getContext(), mapStyle));

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
                    Log.d("STYLE-SET", conv.getValue().toString());
                }
                else {
                    Expected<String,Value> conv = ValueConverter.fromJson("\"hsl(0, 0%, 100%)\"");
                    mapView.getMapboxMap().getStyle().setStyleLayerProperty("background","background-color",conv.getValue());
                }
                // reset all blue countries
                Expected<String,Value> defaultConv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                        "[\"get\", \"iso_3166_1\"],\n" + "[\"\"],\n" + "true,\n" +
                        "false\n" + "]");
                Log.d("MapFragment",defaultConv.getValue().toString());
                mapView.getMapboxMap().getStyle().setStyleLayerProperty("country-blue","filter",defaultConv.getValue());
                Log.d("MapFragment","property blue default: "+mapView.getMapboxMap().getStyle().getStyleLayerProperty("country-blue","filter"));

                // reset all green countries
                mapView.getMapboxMap().getStyle().setStyleLayerProperty("country-green","filter",defaultConv.getValue());

                // set all blue countries
                travelViewModel.fetchAllSavedTravels();
                travelViewModel.getSavedTravelsLiveData(false).observe(getViewLifecycleOwner(),
                        new Observer<Result>() {
                            @Override
                            public void onChanged(Result result) {
                                String countries = getAllCountriesFormatted(((Result.TravelResponseSuccess)result).getData().getTravelList());
                                Log.d("MapFragment","ids: "+countries);
                                Expected<String,Value> conv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                                        "[\"get\", \"iso_3166_1\"],\n" + "["+ (countries) +"],\n" + "true,\n" +
                                        "false\n" + "]");
                                Log.d("MapFragment",conv.getValue().toString());
                                mapView.getMapboxMap().getStyle().setStyleLayerProperty("country-blue",
                                        "filter",conv.getValue());
                                Log.d("MapFragment","property blue: "+mapView.getMapboxMap().getStyle().getStyleLayerProperty("country-blue","filter"));

                            }
                        });
                // set all green countries
                travelViewModel.fetchAllBookedTravels();
                travelViewModel.getBookedTravelsLiveData(false).observe(getViewLifecycleOwner(),
                        new Observer<Result>() {
                            @Override
                            public void onChanged(Result result) {
                                String countries = getAllCountriesFormatted(((Result.TravelResponseSuccess)result).getData().getTravelList());
                                Log.d("MapFragment","ids: "+countries);
                                Expected<String,Value> conv = ValueConverter.fromJson("[\n" +"\"match\",\n" +
                                        "[\"get\", \"iso_3166_1\"],\n" + "["+ (countries) +"],\n" + "true,\n" +
                                        "false\n" + "]");
                                Log.d("MapFragment",conv.getValue().toString());
                                mapView.getMapboxMap().getStyle().setStyleLayerProperty("country-green",
                                        "filter",conv.getValue());
                                Log.d("MapFragment","property green: "+mapView.getMapboxMap().getStyle().getStyleLayerProperty("country-blue","filter"));

                            }
                        });

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

       // database = TravelsRoomDatabase.getDatabase(requireContext());


        recyclerView = view.findViewById(R.id.recyclerview_travels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                                            if (countryName != null && countryCode != null) {
                                                Log.d("PRINT-FROM-CALLBACK", "name: " + countryName +
                                                        " code: " + countryCode);
                                                debugText.setText("Country name: " + countryName +
                                                        "\ncountry code: " + countryCode);



                                                // previous results
                                                if(travelViewModel.getTravelResponseLiveData()!=null) {
                                                   /* TravelResponse response = new TravelResponse();
                                                    response.setTravelList(null);
                                                    travelViewModel.getTravelResponseLiveData().postValue(new Result.TravelResponseSuccess(response));
                                                    */
                                                  //  travelViewModel.setTravelListLiveData(null);
                                                   // recyclerView.setAdapter(null);
                                                }

                                                String searchCode = countryCode.substring(1,countryCode.length()-1);
                                                Log.d("MapFragment", "search: "+searchCode+" vs. ");
                                                travelViewModel.fetchSavedTravels(searchCode);

                                                travelViewModel.getTravelResponseLiveData().observe(getViewLifecycleOwner(), new Observer<Result>() {
                                                    @Override
                                                    public void onChanged(Result result) {
                                                        if(((Result.TravelResponseSuccess)result).getData().getTravelList()!=null) {
                                                            Log.d("MapFragment","risultati: "+((Result.TravelResponseSuccess)result).getData().getTravelList().size());
                                                        //    Log.d("MapFragment","results: "+((Result.TravelResponseSuccess)result).getData().getTravelList().get(0).getCountry());
                                                        }
                                                        List<Travel> travelList = ((Result.TravelResponseSuccess)result).getData().getTravelList();

                                                        travelListAdapter = new TravelListAdapter(travelList,new TravelListAdapter.OnItemClickListener(){
                                                            public void onTravelItemClick(Travel travel){
                                                                Snackbar.make(view, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                                                            }

                                                            public void onDeleteButtonPressed(int position){
                                                                Snackbar.make(view, getString(R.string.list_size_message) + travelList.size(), Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        recyclerView.setAdapter(travelListAdapter);
                                                    }
                                                });


                                                // set border on selected
                                                Expected<String,Value> conv = ValueConverter.fromJson(
                                                        "[\"match\", [\"get\", \"iso_3166_1\"], ["+countryCode+"], true, false]");
                                                mapView.getMapboxMap().getStyle().setStyleLayerProperty(
                                                        "country-selected","filter",conv.getValue());

                                                // set zoom on selected
                                                CameraOptions cameraOptions =
                                                        new CameraOptions.Builder().center(point).zoom(2.0).build();
                                                mapView.getMapboxMap().setCamera(cameraOptions);


                                                break;
                                            }
                                        }
                                    }
                                }
                                Value stringValueExpected =
                                        mapView.getMapboxMap().getStyle().getStyleLayerProperty("country-selected","filter").getValue();

                                Log.d("STYLE-SET", "dopo getStyle " + stringValueExpected.toString());
                               /* Expected<String,Value> conv = ValueConverter.fromJson(
                                        "[\"match\", [\"get\", \"iso_3166_1\"], [\""+countryCode+"\"], true, false]");
                                        "[\"match\", [\"get\", \"iso_3166_1\"], [\"IT\"], true, false]");
                                mapView.getMapboxMap().getStyle().setStyleLayerProperty(
                                        "country-selected","filter",conv.getValue());*/

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
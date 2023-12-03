package it.unimib.buildyourholiday;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.common.ResourceLoadOptions;
import com.mapbox.maps.*;
import com.mapbox.maps.ResourceOptions;
import com.mapbox.maps.ResourceOptionsManager;
import com.mapbox.maps.Style;
import com.mapbox.maps.loader.MapboxMaps;
import com.mapbox.maps.loader.MapboxMapsInitializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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

        View view = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = view.findViewById(R.id.map_view);
        //mapView.getMapboxMap().loadStyleUri(Style.LIGHT);

        //mapView.getMapboxMap().loadStyleUri("asset://map_style.json");
        //mapView.getMapboxMap().loadStyleJson("asset://map_style.json");
        //mapView.getMapboxMap().loadStyleUri("mapbox://styles/choppadebug/clplsfl1a00ty01qt1pjsfphc");
        //mapView.getMapboxMap().loadStyleUri("mapbox://styles/choppadebug/clppp4jot013501o9bowr9776/draft");

        try {
            InputStream is = getActivity().getAssets().open("map_style.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String styleJson = new String(buffer, "UTF-8");
            mapView.getMapboxMap().loadStyleJson(styleJson);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
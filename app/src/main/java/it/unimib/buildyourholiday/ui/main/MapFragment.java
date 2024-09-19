package it.unimib.buildyourholiday.ui.main;

import static it.unimib.buildyourholiday.util.Constants.MAP_STYLE_FILE;
import static it.unimib.buildyourholiday.util.MapUtil.initMap;
import static it.unimib.buildyourholiday.util.MapUtil.refreshMap;
import static it.unimib.buildyourholiday.util.MapUtil.setMapTheme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mapbox.geojson.Point;
import com.mapbox.maps.*;
import com.mapbox.maps.ResourceOptionsManager;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;


import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.welcome.UserViewModel;
import it.unimib.buildyourholiday.ui.welcome.UserViewModelFactory;
import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.JsonFileReader;
import it.unimib.buildyourholiday.util.MapUtil;
import it.unimib.buildyourholiday.util.ServiceLocator;

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

    private RecyclerView recyclerView;
    private MapView mapView = null;
    private Button refreshMapButton = null;
    private boolean refreshTriggered = false;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ResourceOptionsManager.Companion.getDefault(getContext().getApplicationContext(),
                getString(it.unimib.buildyourholiday.R.string.mapbox_access_token));
        Log.d("MapFragment","dopo GETINSTANCE() + "+getString(it.unimib.buildyourholiday.R.string.mapbox_access_token));

        View view = inflater.inflate(it.unimib.buildyourholiday.R.layout.fragment_map, container, false);

        mapView = view.findViewById(it.unimib.buildyourholiday.R.id.map_view);

        refreshMapButton = view.findViewById(it.unimib.buildyourholiday.R.id.refresh_button);
        //////////////////////////////////////////////
        ITravelRepository travelRepository = ServiceLocator.getInstance()
                .getTravelRepository(requireActivity().getApplication());
        TravelViewModel travelViewModel = new ViewModelProvider(
                requireActivity(),
                new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);

        IUserRepository userRepository = ServiceLocator.getInstance()
                .getUserRepository(requireActivity().getApplication());
        UserViewModel userViewModel= new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        mapView.getMapboxMap().loadStyleJson(JsonFileReader.readJsonFromAssets(getContext(), MAP_STYLE_FILE));

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);

        setMapTheme(mapView, sharedPreferences);

        if(userRepository.getLoggedUser() != null)
            initMap(mapView, travelViewModel, getViewLifecycleOwner());

        refreshMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshTriggered = true;
                refreshMap(mapView, travelViewModel, getViewLifecycleOwner());
            }
        });

        TextView selectedCountry = view.findViewById(it.unimib.buildyourholiday.R.id.textView_selectedCountry);

        recyclerView = view.findViewById(R.id.recyclerview_travels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(new OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull Point point) {
                refreshTriggered = false;

                // listener to be set on created adapter for recyclerview results
                TravelListAdapter.OnItemClickListener listener = new TravelListAdapter.OnItemClickListener(){
                    public void onTravelItemClick(Travel travel){
                        Snackbar.make(view, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                    }
                    public void onDeleteButtonPressed(Travel position){ }
                };

                MapUtil.selectCountry(mapView,refreshTriggered,point,requireActivity(),getViewLifecycleOwner(),
                        selectedCountry,recyclerView,listener);

                return true;

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
package it.unimib.buildyourholiday.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

//import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.adapter.TravelSavedAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private static final String TAG = SavedFragment.class.getSimpleName();
    //    private TravelListAdapter travelListAdapter;
    //private TravelViewModel travelViewModel;

    private RecyclerView recyclerView;

    public SavedFragment() {
        // Required empty public constructor
    }

    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        initData();

        ITravelRepository travelRepository = ServiceLocator.getInstance()
                .getTravelRepository(requireActivity().getApplication());
        TravelViewModel travelViewModel = new ViewModelProvider(
                requireActivity(),
                new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerview_saved_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        travelViewModel.fetchAllSavedTravels();
        travelViewModel.getSavedTravelsLiveData(false).observe(getViewLifecycleOwner(),
                result -> {
                    List<Travel> travelList = ((Result.TravelResponseSuccess)result).getData().getTravelList();
                    TravelSavedAdapter travelSavedAdapter = new TravelSavedAdapter(travelList,
                            new TravelSavedAdapter.OnItemClickListener(){
                               /* public void onTravelItemClick(Travel travel){
                                    Snackbar.make(view, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                                }*/
                                @Override
                                public void onSearchButtonPressed(String destination, String departure, String startDate, String endDate, String adults) {

                                    Fragment newFragment = new HomeFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("destination", destination);
                                    bundle.putString("departure", departure);
                                    bundle.putString("startDate", startDate);
                                    bundle.putString("endDate", endDate);
                                    bundle.putString("adults", adults);

                                    newFragment.setArguments(bundle);
                                }

                                public void onDeleteButtonPressed(Travel deletedTravel) {
                                    for (int i=0;i<travelList.size();i++) {
                                        Log.d(TAG,"travel "+i+": "+travelList.get(i).getCity());
                                    }
                                    Log.d(TAG,"size: "+travelList.size());

                                    travelViewModel.deleteTravel(deletedTravel);

                                    Snackbar.make(view, R.string.action_deleted, Snackbar.LENGTH_SHORT).show();
                                }
                            });

                    recyclerView.setAdapter(travelSavedAdapter);
                });


        return view;

        /*
        recyclerView = view.findViewById(R.id.recyclerview_saved_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TravelListAdapter travelListAdapter = new TravelListAdapter(newList,
                new TravelListAdapter.OnItemClickListener(){
                    public void onTravelItemClick(Travel travel){
                        Snackbar.make(view, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                    }

                    public void onDeleteButtonPressed(int position){
                        Snackbar.make(view, getString(R.string.list_size_message) + newList.size(), Snackbar.LENGTH_SHORT).show();
                    }
                });
        recyclerView.setAdapter(travelListAdapter);

        return view;

         */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*TravelViewModel model = new ViewModelProvider(this).get(TravelViewModel.class);
        model.getAllTravel().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                // hotelResults è una textview
                newList = travels;
            }
        });*/
    }

    private void initData(){

        List<Travel> newList = new ArrayList<>();
        Flight flight = new Flight("COD","12-02-25", "16:00 - 17:30", "Roma", "24-02-24", "9:00 - 10:00", "Barcelona", 1000.00);

        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));

    }

}
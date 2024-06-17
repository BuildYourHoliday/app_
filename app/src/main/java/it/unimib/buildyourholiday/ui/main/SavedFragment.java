package it.unimib.buildyourholiday.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
    private List<Travel> newList;
//    private TravelListAdapter travelListAdapter;
    private ProgressBar progressBar;
    //private TravelViewModel travelViewModel;

    private RecyclerView recyclerView;

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        List<Travel> travelList = ((Result.TravelResponseSuccess)result).getData().getTravelList();
                        TravelSavedAdapter travelSavedAdapter = new TravelSavedAdapter(travelList,
                                new TravelSavedAdapter.OnItemClickListener(){
                                    public void onTravelItemClick(Travel travel){
                                        Snackbar.make(view, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSearchButtonPressed(String destination, String departure, String startDate, String endDate, String adults) {
                                        //prova implementando delete
                                        Fragment newFragment = new HomeFragment();

                                        Bundle bundle = new Bundle();
                                        bundle.putString("destination", destination);
                                        bundle.putString("departure", departure);
                                        bundle.putString("startDate", startDate);
                                        bundle.putString("endDate", endDate);
                                        bundle.putString("adults", adults);

                                        //getParentFragmentManager().setFragmentResult("destination", destination);

                                        newFragment.setArguments(bundle);

                                        // Navigate to the new fragment (assuming you're using the Navigation component)
                                        /*getParentFragmentManager().beginTransaction()
                                                .replace(R.id.homeFragment, newFragment)
                                                .addToBackStack(null)
                                                .commit();*/
                                    }


                                    public void onDeleteButtonPressed(int position) {
                                        for (int i=0;i<travelList.size();i++) {
                                            Log.d(TAG,"travel "+i+": "+travelList.get(i).getCity());
                                        }
                                        Log.d(TAG,"size: "+travelList.size());
                                        Log.d(TAG,"position: " + position);
                                        
                                        /* TODO: test once saving is possible */
                                        Travel delete = travelList.get(position);
                                        Log.d(TAG,"pos: "+travelList.get(position).getFlight().getDepartureAirport());
                                        travelViewModel.deleteTravel(delete);


                                        Snackbar.make(view, getString(R.string.list_size_message) + travelList.size(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                        recyclerView.setAdapter(travelSavedAdapter);
                    }
                });


        return view;
        //////////////////////////

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

        newList = new ArrayList<>();
        Flight flight = new Flight("COD","12-02-25", "16:00 - 17:30", "Roma", "24-02-24", "9:00 - 10:00", "Barcelona", 1000.00);

        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
        newList.add(new Travel(flight));
       /* newList = new ArrayList<>();

        newList.add(new Travel("Milano", "12-02-24", "24-02-24", 120.00));
        newList.add(new Travel("Parigi", "15-04-24", "24-04-24", 620.50));
        newList.add(new Travel("Londra", "14-05-24", "23-05-24", 150.50));
        newList.add(new Travel("Mosca", "19-06-24", "26-06-24", 1200.00));
        newList.add(new Travel("NY", "11-07-24", "22-07-24", 1320.50));*/
    }

}
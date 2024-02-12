package it.unimib.buildyourholiday;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.User;
import it.unimib.buildyourholiday.util.DataEncryptionUtil;
import it.unimib.buildyourholiday.util.ServiceLocator;

public class BookedFragment extends Fragment {

    private List<Travel> bookedList;
    //    private TravelListAdapter travelListAdapter;
    private ProgressBar progressBar;
    //private TravelViewModel travelViewModel;

    private RecyclerView recyclerView;

    public BookedFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booked, container, false);
        initData();

       /* TravelViewModel model = new ViewModelProvider(this).get(TravelViewModel.class);
        model.getAllTravel().observe(getViewLifecycleOwner(), new Observer<List<Travel>>() {
            @Override
            public void onChanged(List<Travel> travels) {
                // hotelResults è una textview
                bookedList = travels;
            }
        });*/

        ITravelRepository travelRepository = ServiceLocator.getInstance()
                .getTravelRepository(requireActivity().getApplication());
        TravelViewModel travelViewModel = new ViewModelProvider(
                requireActivity(),
                new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);

        recyclerView = v.findViewById(R.id.recyclerview_booked);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        travelViewModel.fetchAllBookedTravels();
        travelViewModel.getBookedTravelsLiveData(false).observe(getViewLifecycleOwner(),
                new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        List<Travel> travelList = ((Result.TravelResponseSuccess)result).getData().getTravelList();
                        TravelListAdapter travelListAdapter = new TravelListAdapter(travelList,
                                new TravelListAdapter.OnItemClickListener(){
                                    public void onTravelItemClick(Travel travel){
                                        Snackbar.make(v, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                                    }

                                    public void onDeleteButtonPressed(int position){
                                        Snackbar.make(v, getString(R.string.list_size_message) + travelList.size(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                        recyclerView.setAdapter(travelListAdapter);
                    }
                });


        return v;
        /*

        recyclerView = v.findViewById(R.id.recyclerview_booked);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(layoutManager);

        //bookedList = getTravelListWithGSon();
// for button to implement
        TravelListAdapter travelListAdapter = new TravelListAdapter(bookedList,
                new TravelListAdapter.OnItemClickListener(){
                    public void onTravelItemClick(Travel travel){
                        Snackbar.make(v, travel.getCity(), Snackbar.LENGTH_SHORT).show();
                    }

                    public void onDeleteButtonPressed(int position){
                        Snackbar.make(v, getString(R.string.list_size_message) + bookedList.size(), Snackbar.LENGTH_SHORT).show();
                    }
                });


        //recyclerView.setHasFixedSize(true);
        //TravelListAdapter travelListAdapter = new TravelListAdapter(bookedList);
        recyclerView.setAdapter(travelListAdapter);

        return v;

         */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData(){
        bookedList = new ArrayList<>();

        bookedList.add(new Travel("Roma", "12-02-25", "24-02-24", 120.00));
        bookedList.add(new Travel("Parigi", "15-04-24", "24-04-24", 620.50));
        bookedList.add(new Travel("Londra", "14-05-24", "23-05-24", 150.50));
        bookedList.add(new Travel("Mosca", "19-06-24", "26-06-24", 1200.00));
        bookedList.add(new Travel("Los Angeles", "17-07-24", "30-07-24", 1820.50));
        bookedList.add(new Travel("Tokio", "15-08-26", "30-08-26", 2020.50));
    }
}
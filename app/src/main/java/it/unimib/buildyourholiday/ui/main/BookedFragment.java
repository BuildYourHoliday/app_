package it.unimib.buildyourholiday.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.ServiceLocator;

public class BookedFragment extends Fragment {

    private List<Travel> bookedList;

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
                result -> {
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
                });


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData(){
        bookedList = new ArrayList<>();
        Flight flight = new Flight("COD","12-02-25", "16:00 - 17:30", "Roma", "24-02-24", "9:00 - 10:00", "Barcelona", 1000.00);

        bookedList.add(new Travel(flight));
        bookedList.add(new Travel(flight));
        bookedList.add(new Travel(flight));
        bookedList.add(new Travel(flight));
        bookedList.add(new Travel(flight));
        bookedList.add(new Travel(flight));
    }
}
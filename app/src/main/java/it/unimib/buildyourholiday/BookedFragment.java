package it.unimib.buildyourholiday;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.data.TravelListAdapter;
import it.unimib.buildyourholiday.model.Travel;

public class BookedFragment extends Fragment {

    private List<Travel> bookedList;
    //    private TravelListAdapter travelListAdapter;
    private ProgressBar progressBar;
    //private TravelViewModel travelViewModel;

    private RecyclerView recyclerView;

    public BookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booked, container, false);
        initData();

        recyclerView = v.findViewById(R.id.recyclerview_booked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setHasFixedSize(true);
        TravelListAdapter travelListAdapter = new TravelListAdapter(bookedList);
        recyclerView.setAdapter(travelListAdapter);

        return v;
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
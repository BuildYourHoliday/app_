package it.unimib.buildyourholiday;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

//import it.unimib.buildyourholiday.data.TravelListAdapter;
import it.unimib.buildyourholiday.data.TravelListAdapter;
import it.unimib.buildyourholiday.model.Travel;

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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        initData();

        recyclerView = view.findViewById(R.id.recyclerview_saved_trip);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setHasFixedSize(true);
        TravelListAdapter travelListAdapter = new TravelListAdapter(newList);
        recyclerView.setAdapter(travelListAdapter);

        return view;
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

        newList.add(new Travel("Milano", "12-02-24", "24-02-24", 120.00));
        newList.add(new Travel("Parigi", "15-04-24", "24-04-24", 620.50));
        newList.add(new Travel("Londra", "14-05-24", "23-05-24", 150.50));
        newList.add(new Travel("Mosca", "19-06-24", "26-06-24", 1200.00));
        newList.add(new Travel("NY", "11-07-24", "22-07-24", 1320.50));
    }

}
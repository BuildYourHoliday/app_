package it.unimib.buildyourholiday;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amadeus.resources.FlightOfferSearch;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import it.unimib.buildyourholiday.adapter.FlightListAdapter;
import it.unimib.buildyourholiday.model.Flight;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlightResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private double price;
    private static final String TAG = FlightResultsFragment.class.getSimpleName();
    private FlightListViewModel flightListViewModel;
    private FlightListAdapter flightListAdapter; private List<FlightOfferSearch> flightOffers;
    private List<Flight> flights; private List<String> durations; private List<Boolean> directed;
    private Flight flight; private FlightOfferSearch flightOffer;
    private RecyclerView flightsRecyclerView;
    private TextView resultPriceError;
    private TextView flightLabel;
    private LinearLayout loadingBar;
    private double flightPrice = -1;
    private Button proceedButton; private ImageButton backButton;

    public FlightResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlightResultsFragment newInstance(String param1, String param2) {
        FlightResultsFragment fragment = new FlightResultsFragment();
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
            Bundle bundle = getArguments();
            price = bundle.getDouble("price");
            Log.d(TAG, "price received");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"on create view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_results, container, false);

        loadingBar = view.findViewById(R.id.loading_layout);
            loadingBar.setVisibility(View.VISIBLE);

        resultPriceError = view.findViewById(R.id.error_match_text);
            resultPriceError.setVisibility(View.GONE);
        proceedButton = view.findViewById(R.id.proceed_button);
        backButton = view.findViewById(R.id.back_button);
        flightLabel = view.findViewById(R.id.flight_text);
        flightsRecyclerView = view.findViewById(R.id.recyclerview_flight_offers);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        flightListViewModel = new ViewModelProvider(requireActivity()).get(FlightListViewModel.class);

        // observe results from async call
        flightListViewModel.getFlightListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Flight>>() {
            @Override
            public void onChanged(List<Flight> flightList) {
                flights = flightList;
                Log.d(TAG, "flightList obtained");
            }
        });

        flightListViewModel.getDurationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                durations = strings;
                Log.d(TAG, "durations obtained");
            }
        });

        flightListViewModel.getDirectFlightLiveData().observe(getViewLifecycleOwner(), new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> booleans) {
                directed = booleans;
                Log.d(TAG, "directed obtained");
            }
        });

        flightListViewModel.getFlightOffersMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<FlightOfferSearch>>() {
            @Override
            public void onChanged(List<FlightOfferSearch> flightOfferSearches) {
                flightOffers = flightOfferSearches;
                Log.d(TAG,"offers received");
                onResultReceived();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.homeFragment2);
                // Ottieni il FragmentManager
               // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


                // Inizia una transazione del fragment
               // FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Rimuovi il fragment corrente
                //transaction.remove(FlightResultsFragment.this);

                // Esegui il commit della transazione
                //transaction.commit();
                // Torna alla schermata precedente
                //fragmentManager.popBackStack();

                navController.popBackStack(R.id.homeFragment,false);
                //  navController.navigate(R.id.homeFragment2);
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flightPrice > 0) {
                    double finalPrice = -1;
                    if(price>=0)    // price==-1 if research is without price parameter
                        finalPrice = price - flightPrice;


                    HotelListViewModel hotelListViewModel = new ViewModelProvider(requireActivity()).get(HotelListViewModel.class);
                    AmadeusRepository.hotelSearch(flight.getDepartureDate(), finalPrice, hotelListViewModel);
                    flightListViewModel.setSelectedFlightOffer(flightOffer);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("flight",flight);
                    // pass to execute SearchResultsFragment with flight to build Travel instance
                    navController.navigate(R.id.hotelResultsFragment, bundle);
                } else {
                    Snackbar.make(v, getString(R.string.select_flight_err), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void onResultReceived() {
        loadingBar.setVisibility(View.GONE);

        flightLabel.setVisibility(View.VISIBLE);

        if(flights.size()==0) {
            resultPriceError.setVisibility(View.VISIBLE);
        } else
            proceedButton.setVisibility(View.VISIBLE);
        Log.d(TAG, "progress bar gone");

        flightListAdapter = new FlightListAdapter(flights, durations, directed, flightOffers, new FlightListAdapter.OnItemClickListener() {
            @Override
            public void onFlightItemClick(Flight selectedFlight,FlightOfferSearch selectedFlightOffer) {
                flight = selectedFlight;
                flightOffer = selectedFlightOffer;
                flightPrice = selectedFlight.getPrice();
                Log.d(TAG, "flight set to travel");
            }
        });
        Log.d(TAG, "adapter created");

        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        flightsRecyclerView.setAdapter(flightListAdapter);
        Log.d(TAG, "recyclerview setup");
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().invalidate();
        Log.d(TAG,"on resume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"on destroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"on destroy view");
    }
}
package it.unimib.buildyourholiday;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.unimib.buildyourholiday.adapter.HotelListAdapter;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.data.repository.user.UserRepository;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = HotelResultsFragment.class.getSimpleName();
    private HotelListViewModel hotelListViewModel; private Flight flight;
    private HotelListAdapter hotelListAdapter;
    private List<Hotel> hotels; private List<String> descriptions; private List<String> links;
    private LinearLayout loadingBar;
    private TextView hotelLabel;
    private RecyclerView hotelsRecyclerView;
    private Hotel hotel;
    private boolean noMatchingResults = false;
    private TextView resultPriceError;
    private Travel builtTravel = null;
    private Button saveButton; private Button purchaseButton;
    private UserViewModel userViewModel = null; private TravelViewModel travelViewModel = null;


    public HotelResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotelResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelResultsFragment newInstance(String param1, String param2) {
        HotelResultsFragment fragment = new HotelResultsFragment();
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
            flight = bundle.getParcelable("flight");
            Log.d(TAG, "flight received");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_results, container, false);

        loadingBar = view.findViewById(R.id.loading_layout);
        loadingBar.setVisibility(View.VISIBLE);

        hotelLabel = view.findViewById(R.id.hotel_text);
        hotelsRecyclerView = view.findViewById(R.id.recyclerview_hotel_offers);
        resultPriceError = view.findViewById(R.id.error_match_text);
        saveButton = view.findViewById(R.id.save_button);
        purchaseButton = view.findViewById(R.id.purchase_button);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        hotelListViewModel = new ViewModelProvider(requireActivity()).get(HotelListViewModel.class);

        // observe results from async call
        hotelListViewModel.getHotelListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Hotel>>() {
            @Override
            public void onChanged(List<Hotel> hotelList) {
                hotels = hotelList;
                Log.d(TAG, "hotelList obtained");
            }
        });

        hotelListViewModel.getHotelDescriptionLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> descriptionList) {
                descriptions = descriptionList;
                Log.d(TAG, "descriptionList obtained");
            }
        });

        hotelListViewModel.getResultsPriceError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                noMatchingResults = aBoolean;
                Log.d(TAG, "ALERT: no results with selected price");
            }
        });

        hotelListViewModel.getHotelLinksLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> linkList) {
                links = linkList;
                Log.d(TAG, "linkList obtained");
                onResultReceived();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(builtTravel != null) {
                    if(saveTravel(builtTravel)) {
                        Toast.makeText(requireContext(), requireContext().getString(R.string.saved_travel_success), Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.profileFragment);
                    }
                    else {
                        navController.navigate(R.id.homeFragment);
                        navController.navigate(R.id.loginActivity);
                        Toast.makeText(requireContext(), requireContext().getString(R.string.login_required), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(builtTravel != null) {
                    // todo purchase fragment & model
                }
            }
        });

        return view;
    }

    private void onResultReceived() {
        loadingBar.setVisibility(View.GONE);

        hotelLabel.setVisibility(View.VISIBLE);
        Log.d(TAG, "progress bar gone");

        if (noMatchingResults) {
            resultPriceError.setVisibility(View.VISIBLE);
        }

        hotelListAdapter = new HotelListAdapter(hotels, descriptions, links, new HotelListAdapter.OnItemClickListener() {
            @Override
            public void onHotelItemClick(Hotel selectedHotel) {
                hotel = selectedHotel;
                buildTravelInstance();
                Log.d(TAG, "hotel set to travel");
            }
        }, requireContext());
        Log.d(TAG, "adapter created");

        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelsRecyclerView.setAdapter(hotelListAdapter);
        Log.d(TAG, "recyclerview setup");
    }

    private void buildTravelInstance() {
        builtTravel = new Travel(flight,hotel);
        Log.d(TAG,"travel created successfully");
    }

    private boolean saveTravel(Travel builtTravel) {
        if(userViewModel == null) {
            IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
            userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        }
        if(travelViewModel == null) {
            ITravelRepository travelRepository = ServiceLocator.getInstance().getTravelRepository(requireActivity().getApplication());
            travelViewModel = new ViewModelProvider(this,new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);
        }
        if(userViewModel.getLoggedUser() != null) {
            travelViewModel.saveTravel(builtTravel);
            Log.d(TAG,"user is logged, proceeded to save");
            return true;
        } else {
            Log.d(TAG,"user must be logged in order to save a travel!");
            return false;
        }
    }
}
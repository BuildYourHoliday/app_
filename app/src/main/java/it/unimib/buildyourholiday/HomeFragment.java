package it.unimib.buildyourholiday;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private AutoCompleteTextView originAutoCompleteTextView;
    private AutoCompleteTextView destinationAutoCompleteTextView;
    private NavController navController;
    private Button searchButton; private ImageButton resetButton;
    private EditText departDateEditText;
    private EditText returnDateEditText;
    private EditText adultsEditText;
    private EditText budgetEditText;
    private HomeFragmentViewModel searchViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton buttonSettings = view.findViewById(R.id.settings);
        buttonSettings.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileActivity);
        });

        originAutoCompleteTextView = view.findViewById(R.id.depart_place);
        destinationAutoCompleteTextView = view.findViewById(R.id.destination_place);

        searchButton = view.findViewById(R.id.search_button);
        resetButton = view.findViewById(R.id.bin_button);

        departDateEditText = view.findViewById(R.id.depart_date_input);
        returnDateEditText = view.findViewById(R.id.return_date_input);
        adultsEditText = view.findViewById(R.id.people_number);
        budgetEditText = view.findViewById(R.id.budget_import);

        searchViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        searchViewModel.setupAutoCompleteTextViews(originAutoCompleteTextView,destinationAutoCompleteTextView,requireContext(),requireActivity());

        departDateEditText.setOnClickListener(v -> searchViewModel.showDatePickerDialog(requireContext(),departDateEditText));
        returnDateEditText.setOnClickListener(v -> searchViewModel.showDatePickerDialog(requireContext(),returnDateEditText));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retrieveSearchParameters()) {
                    searchViewModel.searchFlights(requireActivity(),navController);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        return view;
    }

    public boolean retrieveSearchParameters() {
        String departDate = departDateEditText.getText().toString();
        String returnDate = returnDateEditText.getText().toString();
        String adults = adultsEditText.getText().toString();
        String budget = budgetEditText.getText().toString();
        String origin = originAutoCompleteTextView.getText().toString();
        String destination = destinationAutoCompleteTextView.getText().toString();

        return searchViewModel.retrieveSearchParameters(requireContext(),requireView(),departDate,returnDate,adults,budget,origin,destination);
    }

    private void resetFields() {
        originAutoCompleteTextView.setText("");
        destinationAutoCompleteTextView.setText("");
        departDateEditText.setText("");
        returnDateEditText.setText("");
        adultsEditText.setText("");
        budgetEditText.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"on resume");
        Fragment f = getParentFragmentManager().findFragmentById(R.id.flightResultsFragment);
        Log.d(TAG,"flightresults is null: "+(f==null));
    }
}
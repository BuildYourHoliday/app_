package it.unimib.buildyourholiday;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.DatePicker;


import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

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
    private String originCityCode = "MIL";
    private String originCity;
    private String destinationCityCode = "PAR";
    private String destinationCity;
    private String destinationCountryCode;
    private String departDate;
    private String returnDate;
    private int adults;
    private double price;

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

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // dropdown results for origin city
        LuoghiAdapter originLuoghiAdapter = new LuoghiAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item);
        AmadeusRepository.setupBarSearch(getActivity(), originAutoCompleteTextView, originLuoghiAdapter);

        originAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                originCityCode = originLuoghiAdapter.codeForSelected(position);
                originCity = originLuoghiAdapter.nameForSelected(position);
            }
        });

        // dropdown results for destination city
        LuoghiAdapter destinationLuoghiAdapter = new LuoghiAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item);
        AmadeusRepository.setupBarSearch(getActivity(), destinationAutoCompleteTextView, destinationLuoghiAdapter);

        destinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationCityCode = destinationLuoghiAdapter.codeForSelected(position);
                destinationCity = destinationLuoghiAdapter.nameForSelected(position);
                destinationCountryCode = destinationLuoghiAdapter.countryForSelected(position);
            }
        });

        departDateEditText.setOnClickListener(v -> showDatePickerDialog(departDateEditText));
        returnDateEditText.setOnClickListener(v -> showDatePickerDialog(returnDateEditText));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retrieveSearchParameters()) {
                    FlightListViewModel flightListViewModel = new ViewModelProvider(requireActivity()).get(FlightListViewModel.class);
                    AmadeusRepository.flightSearch(originCityCode, destinationCityCode, departDate, returnDate, adults, price, flightListViewModel);

                    AmadeusRepository.holdParameters(destinationCityCode, destinationCity, adults, returnDate);

                    // pass to execute SearchResultsFragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("price",price);
                    //launchFragmentFlightResults(price);
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.flightResultsFragment,true).build();

                    navController.navigate(R.id.action_homeFragment_to_flightResultsFragment, bundle, navOptions);
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

    private void launchFragmentFlightResults(double price) {
        FlightResultsFragment flightResultsFragment = FlightResultsFragment.newInstance("","");

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, flightResultsFragment);
        transaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public boolean retrieveSearchParameters() {
        departDate = departDateEditText.getText().toString();
        if(departDate.isEmpty() || !isDateValid(departDate)) {
            Snackbar.make(requireView(), "Please check format for departure date", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        returnDate = returnDateEditText.getText().toString();
        if(!returnDate.isEmpty() && !isDateValid(returnDate)) {
            Snackbar.make(requireView(), "Please check format for return date", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            if(returnDate.isEmpty()) {
                returnDate = null;
            }
        }

        if(adultsEditText.getText().toString().isEmpty()) {
            Snackbar.make(requireView(), "Please insert number of people", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        adults = Integer.parseInt(adultsEditText.getText().toString());
        if(adults <= 0 || adults>9) {
            Snackbar.make(requireView(), "Please check input for number of people to be between 1-9", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(budgetEditText.getText().toString().isEmpty()) {
            price = -1;
        } else {
            price = Double.parseDouble(budgetEditText.getText().toString());
            if(price <= 0.0) {
                Snackbar.make(requireView(), "Please check input for budget sum", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }


        if(originAutoCompleteTextView.getText().toString().isEmpty()) {
            Snackbar.make(requireView(), "Please check input for your departure city", Snackbar.LENGTH_SHORT).show();
            return false;
        } else
        // activates in case autocomplete doesn't show up
        if(originCityCode == null || originCityCode.length() != 3) {
            MutableLiveData<String> cityCode = new MutableLiveData<>();
            AmadeusRepository.findCityId(originAutoCompleteTextView.getText().toString(), cityCode);
            cityCode.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    originCityCode = s;
                }
            });
        }

        if(destinationAutoCompleteTextView.getText().toString().isEmpty()) {
            Snackbar.make(requireView(), "Please check input for your destination city", Snackbar.LENGTH_SHORT).show();
            return false;
        } else
        if(destinationCityCode == null || destinationCityCode.length() != 3) {
            MutableLiveData<String> cityCode = new MutableLiveData<>();
            AmadeusRepository.findCityId(destinationAutoCompleteTextView.getText().toString(), cityCode);
            cityCode.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    destinationCityCode = s;
                }
            });
        }

        return true;
    }

    private void showDatePickerDialog(EditText dateEditText) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-";

                month += 1;
                if (month<10)   selectedDate += "0"+month+"-";
                else            selectedDate += month+"-";

                if(dayOfMonth<10)   selectedDate += "0"+dayOfMonth;
                else                selectedDate += dayOfMonth;

                dateEditText.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // TODO: implementation and refactoring?
    private boolean isDateValid(String date) {
        if (date.length() != 10)
            return false;

        return true;
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
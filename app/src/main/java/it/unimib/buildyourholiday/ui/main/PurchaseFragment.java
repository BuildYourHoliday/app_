package it.unimib.buildyourholiday.ui.main;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.HotelOfferSearch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.welcome.UserViewModel;
import it.unimib.buildyourholiday.ui.welcome.UserViewModelFactory;
import it.unimib.buildyourholiday.data.repository.travel.AmadeusRepository;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PurchaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = PurchaseFragment.class.getSimpleName();
    private Travel travel;
    private NavController navController;
    private TextInputEditText name; private TextInputEditText lastName;
    private TextInputEditText email; private TextInputEditText phone;
    private UserViewModel userViewModel; private TravelViewModel travelViewModel;
    private FlightOfferSearch flightOffer; private HotelOfferSearch hotelOffer;
    private LinearLayout cardForm;
    private TextInputEditText cardNumber;
    private TextInputEditText expiration;
    private TextInputEditText cvv;
    private Button purchase; private ImageButton backButton;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PurchaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseFragment newInstance(String param1, String param2) {
        PurchaseFragment fragment = new PurchaseFragment();
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
            travel = getArguments().getParcelable("travel");
            Log.d(TAG,"travel received");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        name = view.findViewById(R.id.first_name_editText);
        lastName = view.findViewById(R.id.last_name_editText);
        email = view.findViewById(R.id.email_editText);
        phone = view.findViewById(R.id.phone_editText);

        cardForm = view.findViewById(R.id.card_form);
        cardNumber = view.findViewById(R.id.card_number_editText);
        expiration = view.findViewById(R.id.card_expiration_editText);
        cvv = view.findViewById(R.id.card_cvv_editText);

        purchase = view.findViewById(R.id.confirm_button);
        backButton = view.findViewById(R.id.back_button);


        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        email.setText(userViewModel.getLoggedUser().getEmail());
        cardTesting(view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.hotelResultsFragment);
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel != null) {
                    if(checkInputData()) {
                        FlightOrder.Traveler[] travelers = buildTraveler();
                        processPayment(travel,flightOffer,travelers,email.getText().toString(),hotelOffer);
                    }
                }
            }
        });

        return view;
    }

    private boolean checkInputData() {
        if(name.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()) {
            Snackbar.make(requireView(),"Please check your vital information before",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString().isEmpty()) {
            Snackbar.make(requireView(),"Please check your email before",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if(phone.getText().toString().isEmpty()) {
            Snackbar.make(requireView(),"Please check your phone number before",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        // NOTE: non-testing version shall check card values
        FlightListViewModel flightListViewModel = new ViewModelProvider(requireActivity()).get(FlightListViewModel.class);
        HotelListViewModel hotelListViewModel = new ViewModelProvider(requireActivity()).get(HotelListViewModel.class);

        flightOffer = flightListViewModel.getSelectedFlightOfferMutableLiveData().getValue();
        hotelOffer = hotelListViewModel.getSelectedHotelOfferLiveData().getValue();

        return true;
    }

    private void cardTesting(View view) {
        //cardForm.setBackgroundColor(requireActivity().getColor(R.color.grey));

        cardForm.setClickable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cardForm.setTooltipText("Testing mode enabled");
        }
        cardNumber.setText("4111111111111111");
        cardNumber.setClickable(false);
        expiration.setText("2025-01");
        expiration.setClickable(false);
        cvv.setText("123");
        cvv.setClickable(false);
    }

    private void processPayment(Travel travel, FlightOfferSearch flightOfferSearch, FlightOrder.Traveler[] travelers, String email, HotelOfferSearch hotelOfferSearch) {
        if(userViewModel == null) {
            IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
            userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        }
        if(travelViewModel == null) {
            ITravelRepository travelRepository = ServiceLocator.getInstance().getTravelRepository(requireActivity().getApplication());
            travelViewModel = new ViewModelProvider(this,new TravelViewModelFactory(travelRepository)).get(TravelViewModel.class);
        }
        if(userViewModel.getLoggedUser() != null) {
            AmadeusRepository.bookTravel(flightOfferSearch,travelers,email,hotelOfferSearch)
                    .thenAccept(result -> {
                        travel.setBooked(true);
                        travelViewModel.saveTravel(travel);
                        Log.d(TAG,"user is logged, proceeded to save");

                        Toast.makeText(requireContext(), requireContext().getString(R.string.purchased_travel_success), Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.profileFragment);
                        return;
                    })
                    .exceptionally(error -> {
                        Log.d(TAG,"user is logged, error in booking: "+error.getMessage());

                        // NOTE: since booking not possible, we assume it's done to save stuff
                        travel.setBooked(true);
                        travelViewModel.saveTravel(travel);

                        Toast.makeText(requireContext(), requireContext().getString(R.string.purchased_travel_success), Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.profileFragment);
                        return null;
                    });
        } else {
            Log.d(TAG,"user must be logged in order to save a travel!");
            navController.navigate(R.id.homeFragment);
            navController.navigate(R.id.loginActivity);
            Toast.makeText(requireContext(), requireContext().getString(R.string.login_required), Toast.LENGTH_LONG).show();
        }
    }

    private FlightOrder.Traveler[] buildTraveler() {
        FlightOrder.Traveler[] travelers = new FlightOrder.Traveler[1];
        FlightOrder.Traveler traveler = new FlightOrder.Traveler();
        FlightOrder.Phone[] phones = new FlightOrder.Phone[1];
        phones[0] = new FlightOrder.Phone(); phones[0].setNumber(phone.getText().toString());
        phones[0].setCountryCallingCode("39"); phones[0].setDeviceType(FlightOrder.Phone.DeviceType.MOBILE);
        FlightOrder.Contact contact = new FlightOrder.Contact();
        contact.setPhones(phones);
        traveler.setContact(contact);
        traveler.setName(new FlightOrder.Name(name.getText().toString(),lastName.getText().toString()));
        traveler.setDateOfBirth("2000-01-01");
        FlightOrder.Document[] document = new FlightOrder.Document[1];
        document[0] = new FlightOrder.Document();
        document[0].setDocumentType(FlightOrder.Document.DocumentType.PASSPORT);
        document[0].setNumber("480080076");
        document[0].setExpiryDate("2025-10-11");
        document[0].setIssuanceCountry("IT");
        document[0].setNationality("IT");
        document[0].setHolder(true);
        traveler.setDocuments(document);
        traveler.setId("1");

        for(int i=0;i<travelers.length;i++) {
            // NOTE: assumed for testing simplicity
            travelers[i] = traveler;
        }

        return travelers;
    }
}
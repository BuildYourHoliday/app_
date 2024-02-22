package it.unimib.buildyourholiday;

import static android.app.PendingIntent.getActivity;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ID_TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.Location;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.unimib.buildyourholiday.adapter.FlightListAdapter;
import it.unimib.buildyourholiday.adapter.HotelListAdapter;
import it.unimib.buildyourholiday.adapter.TravelListAdapter;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.data.repository.travel.TravelRepository;
import it.unimib.buildyourholiday.data.source.travel.SavedTravelDataSource;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.DataEncryptionUtil;

public class AmadeusAsync extends AppCompatActivity {

    private AmadeusService service = new AmadeusService();
    private TextView searchCity;
    private EditText searchHotel;
    private Button submitHotel;
    private TextView hotelResults;
    private RecyclerView flightsRecyclerView;
    private RecyclerView hotelsRecyclerView;
    private AutoCompleteTextView multiAutoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private Button saveButton;
    private String originCityCode = "MIL"; //= "BKK";
    private String originCity = "Milan";
    private String destinationCityCode = "PAR"; // = "SYD";
    private String destinationCity = "Paris";
    private String departureDate = "2024-03-01";
    private String returnDate = "2024-03-08";
    private int adults = 2;
    private Flight flight = null;
    private Hotel hotel = null;
    private Travel checkout = null;
    private SavedTravelDataSource source = null;
    private TravelsRoomDatabase db;
    private DataEncryptionUtil dataEncryptionUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amadeus_testing);
        // searchCity = findViewById(R.id.debugging);
        // searchHotel = findViewById(R.id.hotelsFor);
        //     searchHotel.setText("PAR");
        submitHotel = findViewById(R.id.buttonHotel);
        //hotelResults = findViewById(R.id.hotelResults);
        saveButton = findViewById(R.id.buttonSave);
        // setup flights recycler view
        flightsRecyclerView = findViewById(R.id.recyclerview_flight_offers);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db = TravelsRoomDatabase.getDatabase(getApplicationContext());
        dataEncryptionUtil = new DataEncryptionUtil(getApplicationContext());


        // WORKING ORIGIN CITY
        multiAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        String[] DEFAULT = new String[] {"foo","example","foolio"};
        //ArrayAdapter<String> luoghiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, DEFAULT);
        LuoghiAdapter luoghiAdapter = new LuoghiAdapter(this,android.R.layout.simple_spinner_dropdown_item);
        // luoghiAdapter.setNotifyOnChange(true);

        multiAutoCompleteTextView.setAdapter(luoghiAdapter);
        //luoghiAdapter.notifyDataSetChanged();

        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 3) {
                    service.fetchLocationAsync(s.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    result -> {
                                        Location[] response = result;
                                        runOnUiThread(() -> luoghiAdapter.setList(response));
                                        Log.d("RxJava", "Risultato scritto: " + response[0].getName() + "...");
                                        multiAutoCompleteTextView.invalidate();
                                    },
                                    error -> {
                                        // Gestisci gli errori qui
                                        Log.e("RxJava", "Errore: " + error.getMessage());
                                    }
                            );
                }
                //multiAutoCompleteTextView.showDropDown();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing to do
            }
        });

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                originCityCode = luoghiAdapter.codeForSelected(position);
                originCity = luoghiAdapter.nameForSelected(position);

                Log.d("LUOGO"," "+luoghiAdapter.getItemId(position));
                Log.d("LUOGO"," id "+ id);
                Log.d("LUOGO"," position "+position);
            }
        });


        // WORKING
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete2);
        LuoghiAdapter luoghiAdapterDestination = new LuoghiAdapter(this,android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(luoghiAdapterDestination);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 3) {
                    service.fetchLocationAsync(s.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    result -> {
                                        Location[] response = result;
                                        runOnUiThread(() -> luoghiAdapterDestination.setList(response));
                                        Log.d("RxJava", "Risultato scritto: " + response[0].getName() + "...");
                                        autoCompleteTextView.invalidate();
                                    },
                                    error -> {
                                        // Gestisci gli errori qui
                                        Log.e("RxJava", "Errore: " + error.getMessage());
                                    }
                            );
                }
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing to do
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationCityCode = luoghiAdapterDestination.codeForSelected(position);
                destinationCity = luoghiAdapterDestination.nameForSelected(position);

                Log.d("LUOGO"," "+luoghiAdapterDestination.getItemId(position));
                Log.d("LUOGO"," id "+ id);
                Log.d("LUOGO"," position "+position);
            }
        });

        // WORKING
        submitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightSearch();
                completeAsyncCalls();
                Log.d("RxJava","flight instance: "+(flight!=null)+", hotel instance: "+(hotel!=null));
                checkout = new Travel();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkout != null && checkout.getHotel() != null && checkout.getFlight() != null) {
                    Log.d("VOLO","travel to be saved");
                    // TODO addToSaved(checkout);
                } else {
                    //TODO: case null
                    Log.d("VOLO","can't save travel, travel null: "+(checkout==null));
                    if(checkout!=null) Log.d("VOLO","flight null: "+(checkout.getFlight()==null)+"; hotel null: "+(checkout.getHotel()==null));
                }
            }
        });
    }

    public boolean addToSaved(Travel travel) {
        if(travel != null) {
            try {
                //TODO: use viewmodel to save, not working either
                source = new SavedTravelDataSource(dataEncryptionUtil
                        .readSecretDataWithEncryptedSharedPreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN));
                db.travelDao().insertTravel(travel);
                source.addTravel(travel);
                return true;
            } catch (GeneralSecurityException | IOException e) {
                return false;
            }
        }
        return false;
    }

    public Disposable flightSearch() {
        return service.fetchFlightsAsync(originCityCode, destinationCityCode, departureDate, returnDate, adults)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.d("RxJava","result");
                            //int choiceId = 0;
                            List<Flight> flights = new ArrayList<>();
                            String out = "";
                            for (int i=0; i<result.length; i++) {       // TODO: gestire caso returnal null

                                Log.d("RxJava","flights pos: "+i);
                                String flightCode = result[i].getItineraries()[0].getSegments()[0].getCarrierCode()
                                        + result[i].getItineraries()[0].getSegments()[0].getNumber();
                                flight = new Flight(flightCode,departureDate,"time","depAirport",
                                        returnDate,"rTime","arrivAirport",Double.valueOf(result[i].getPrice().getGrandTotal()));
                                FlightOfferSearch.Itinerary[] itineraries = result[i].getItineraries();
                                flight.setDepartureTime(itineraries[0].getSegments()[0].getDeparture().getAt().substring(10));
                                flight.setDepartureAirport(itineraries[0].getSegments()[0].getDeparture().getIataCode());
                                int arrivalIndex = itineraries[0].getSegments().length;
                                flight.setArrivalAirport(itineraries[0].getSegments()[arrivalIndex-1].getArrival().getIataCode());
                                int lastIndex = itineraries[itineraries.length-1].getSegments().length;
                                flight.setReturnalTime(itineraries[itineraries.length-1].getSegments()[lastIndex-1].getDeparture().getAt());

                                flights.add(flight);

                                FlightOfferSearch f = result[i];
                                out += "Offerta " + f.getId() + "\n"
                                        + "Posti disponibili " + f.getNumberOfBookableSeats() + "\n"
                                        + "Prezzo " + f.getPrice().getGrandTotal() + "\n";

                                Log.d("VOLO", out);
                               // hotelResults.append(out);
                            }
                         //   hotelResults.append("\n\n\n");
                            Log.d("RxJava","flights instance: "+(flights!=null)+"flights size: " + flights.size());

                            FlightListAdapter flightListAdapter = new FlightListAdapter(flights,
                                    new FlightListAdapter.OnItemClickListener(){
                                        @Override
                                        public void onFlightItemClick(Flight flight) {
                                            checkout.setFlight(flight);
                                            Log.d("VOLO","flight set to travel");
                                        }
                                    });
                            Log.d("RxJava","nh1");
                            flightsRecyclerView = findViewById(R.id.recyclerview_flight_offers);
                            Log.d("RxJava","nh2");
                            flightsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            Log.d("RxJava","nh3");
                            flightsRecyclerView.setAdapter(flightListAdapter);
                            Log.d("RxJava","nh4");
                        }
                );
    }

    public Disposable completeAsyncCalls() {
        Log.d("RxJava","before search: "+originCityCode+"->"+destinationCityCode);
        Log.d("RxJava","Before search: "+departureDate+" - "+returnDate);

        return service.fetchHotelAsync(destinationCityCode)
                .flatMap(hotelResult -> {
                    List<String> listaId = new ArrayList<>();
                    String[] ids = new String[30];
                    for (int k = 0; k < ids.length && k < hotelResult.length; k++) {
                        Log.d("VOLO", "Hotel: " + hotelResult[k].toString() + "\n\n");
                        ids[k] = hotelResult[k].getHotelId();
                        listaId.add(hotelResult[k].getHotelId());
                        Log.d("VOLO", "ID salvato: " + ids[k]);
                    }
                    listaId.add("HNPARKGU");

                    return service.fetchRoomsAsync(listaId, adults, departureDate, returnDate);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            roomsResult -> {
                                Log.d("RxJava","Rooms result");
                                List<Hotel> hotels = null;
                                List<String> descriptions = null, links = null;

                                if(roomsResult!=null && roomsResult[0]!=null) {
                                    Log.d("RxJava","Not null rooms result");
                                    hotels = new ArrayList<>();
                                    descriptions = new ArrayList<>(); links = new ArrayList<>();

                                    for (int i=0; i<roomsResult.length; i++) {
                                        Hotel h = new Hotel(roomsResult[i].getHotel().getName(),roomsResult[i].getHotel().getHotelId(),
                                                roomsResult[i].getHotel().getCityCode(),destinationCity,roomsResult[i].getOffers()[0].getCheckInDate(),
                                                roomsResult[i].getOffers()[0].getCheckOutDate(),adults,Double.valueOf(roomsResult[i].getOffers()[0].getPrice().getTotal()));
                                        hotels.add(h);
                                        links.add("geo:"+roomsResult[i].getHotel().getLatitude()+","+roomsResult[i].getHotel().getLongitude());
                                        descriptions.add(roomsResult[i].getOffers()[0].getRoom().getDescription().getText());
                                    }
                                }
                                HotelListAdapter hotelListAdapter = new HotelListAdapter(hotels, descriptions, links, new HotelListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onHotelItemClick(Hotel hotel) {
                                        checkout.setHotel(hotel);
                                        Log.d("VOLO","hotel set to travel");
                                    }
                                }, getApplicationContext());
                                Log.d("RxJava","nh1");
                                hotelsRecyclerView = findViewById(R.id.recyclerview_hotel_offers);
                                Log.d("RxJava","nh2");
                                hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                Log.d("RxJava","nh3");
                                hotelsRecyclerView.setAdapter(hotelListAdapter);
                                Log.d("RxJava","nh4");

                                //checkout = new Travel(flight,hotel);
                            },
                            throwable -> {
                                // Gestisci gli errori qui
                                Log.e("RxJava", "Errore durante l'esecuzione asincrona: " + throwable.getMessage());
                            }
                    );
    }





}
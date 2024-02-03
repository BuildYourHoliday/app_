package it.unimib.buildyourholiday;

import static it.unimib.buildyourholiday.util.Constants.RATE_LIMIT_TIME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.unimib.buildyourholiday.model.Travel;

public class AmadeusAsync extends AppCompatActivity {

    private AmadeusService service = new AmadeusService();
    private TextView searchCity;
    private EditText searchHotel;
    private Button submitHotel;
    private TextView hotelResults;
    private AutoCompleteTextView multiAutoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private String originCityCode = "MIL"; //= "BKK";
    private String destinationCityCode = "PAR"; // = "SYD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amadeus_testing);
        // searchCity = findViewById(R.id.debugging);
        // searchHotel = findViewById(R.id.hotelsFor);
        //     searchHotel.setText("PAR");
        submitHotel = findViewById(R.id.buttonHotel);
        hotelResults = findViewById(R.id.hotelResults);


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
                if(s.toString().length() >= 3) {
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
                if(s.toString().length() >= 3) {
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

                Log.d("LUOGO"," "+luoghiAdapterDestination.getItemId(position));
                Log.d("LUOGO"," id "+ id);
                Log.d("LUOGO"," position "+position);
            }
        });

        // WORKING
        submitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAsyncCalls();
            }
        });


    }

    public Disposable completeAsyncCalls() {
        Log.d("RxJava","before search: "+originCityCode+"->"+destinationCityCode);


        return service.fetchFlightsAsync(originCityCode, destinationCityCode, "2024-03-01", "2024-03-08", 2)
                .flatMap(result -> {
                        String out = "";
                        for (FlightOfferSearch f : result) {
                            out += "Offerta " + f.getId() + "\n"
                                    + "Posti disponibili " + f.getNumberOfBookableSeats() + "\n"
                                    + "Prezzo " + f.getPrice().getGrandTotal() + "\n";
                            FlightOfferSearch.Itinerary itineraries[] = f.getItineraries();
                            for (FlightOfferSearch.Itinerary i : itineraries) {
                                FlightOfferSearch.SearchSegment segments[] = i.getSegments();
                                for (FlightOfferSearch.SearchSegment s : segments) {
                                    out += "volo num. " + s.getId() + " di " + s.getAircraft().getCode() + " scalo da: " + s.getDeparture().getIataCode() + " a " + s.getArrival().getIataCode()
                                            + " durata: " + s.getDuration();
                                }
                            }
                            Log.d("VOLO", out);
                            hotelResults.append(out);
                        }
                        hotelResults.append("\n\n\n");

                        return service.fetchHotelAsync(destinationCityCode);
                    })
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

                        return service.fetchRoomsAsync(listaId, 2, "2024-03-01", "2024-03-08");
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            roomsResult -> {
                                Log.d("VOLO", "RISULTATO APERTO, totale risultati: " + roomsResult.length);
                                for (int i = 0; i < 5 && i < roomsResult.length; i++) {
                                    Log.d("VOLO", "Offerta " + i + ": ");
                                    HotelOfferSearch.Offer[] offers = roomsResult[i].getOffers();
                                    for (int j = 0; j < 5 && j < offers.length; j++) {
                                        Log.d("VOLO", offers[j].getRoom().getDescription().toString());
                                        Log.d("VOLO", "Prezzo: " + offers[j].getPrice());
                                        Log.d("VOLO", "Descrizione" + offers[j].getDescription());
                                        hotelResults.append("Prezzo: " + offers[j].getPrice());
                                        hotelResults.append("Descrizione" + offers[j].getDescription());
                                        hotelResults.append(offers[j].getRoom().getDescription().toString());
                                    }
                                }
                            },
                            throwable -> {
                                // Gestisci gli errori qui
                                Log.e("RxJava", "Errore durante l'esecuzione asincrona: " + throwable.getMessage());
                            }
                    );
    }

}
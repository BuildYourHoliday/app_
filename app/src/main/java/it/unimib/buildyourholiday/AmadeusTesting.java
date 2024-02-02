package it.unimib.buildyourholiday;

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
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.unimib.buildyourholiday.model.Travel;

public class AmadeusTesting extends AppCompatActivity {

    private AmadeusService service = new AmadeusService();
    private TextView searchCity;
    private EditText searchHotel;
    private Button submitHotel;
    private TextView hotelResults;
    private AutoCompleteTextView multiAutoCompleteTextView;
    private AutoCompleteTextView autoCompleteTextView;
    private String originCityCode = "BKK";
    private String destinationCityCode = "SYD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amadeus_testing);
       // searchCity = findViewById(R.id.debugging);
       // searchHotel = findViewById(R.id.hotelsFor);
       //     searchHotel.setText("PAR");
        submitHotel = findViewById(R.id.buttonHotel);
        hotelResults = findViewById(R.id.hotelResults);


        // <---------------- QUESTO
        TravelViewModel model = new ViewModelProvider(this).get(TravelViewModel.class);
        model.getAllTravel().observe(this, new Observer<List<Travel>>() {
                    @Override
                    public void onChanged(List<Travel> travels) {
                        // hotelResults è una textview
                        hotelResults.setText(travels.get(0).toString());
                    }
                });

         //



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
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        LuoghiAdapter luoghiAdapterDestination = new LuoghiAdapter(this,android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(luoghiAdapter);

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


        // WORKING, giving back city names for an input
        /*service.fetchLocationAsync("Mila")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            String response = result[0].getName();
                            searchCity.setText(response);
                            if(result.length>1) {
                                for(int i=1; i<result.length; i++) {
                                    searchCity.append("\n"+result[i].getName());
                                }
                            }
                            Log.d("RxJava", "Risultato: " + result);
                        },
                        error -> {
                            // Gestisci gli errori qui
                            Log.e("RxJava", "Errore: " + error.getMessage());
                        }
                );*/

        // WORKING, retrieves hotels from a given city name
        submitHotel.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
                  service.fetchFlightsAsync(originCityCode, destinationCityCode, "2024-03-01", "2024-03-08", 2)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        result -> {
                                            String out ="";
                                            for (FlightOfferSearch f : result) {
                                               /* hotelResults.append("Flight: "+"\n");
                                                hotelResults.append(f.toString()+"\n");
                                                */
                                                out += "Offerta "+f.getId()+ "\n"
                                                            + "Posti disponibili "+f.getNumberOfBookableSeats() + "\n"
                                                            + "Prezzo "+f.getPrice().getGrandTotal() + "\n";
                                                FlightOfferSearch.Itinerary itineraries []= f.getItineraries();
                                                for (FlightOfferSearch.Itinerary i: itineraries) {
                                                    FlightOfferSearch.SearchSegment segments[] = i.getSegments();
                                                    for (FlightOfferSearch.SearchSegment s : segments) {
                                                        out += "volo num. "+s.getId()+ " di "+s.getAircraft().getCode() +" scalo da: "+s.getDeparture().getIataCode()+" a "+s.getArrival().getIataCode()
                                                            + " durata: "+s.getDuration();
                                                    }
                                                }
                                                Log.d("VOLO",out);
                                                hotelResults.append(out);
                                            }
                                            hotelResults.append("\n\n\n");
                                        }
                        );

              List<String> listaId = new ArrayList<>();
                service.fetchHotelAsync(destinationCityCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {

                                    String[] ids = new String[10];
                                    for(int k=0;k<ids.length && k<result.length;k++) {
                                        Log.d("VOLO", "Hotel: " + result[k].toString() + "\n\n");
                                        ids[k] = result[k].getHotelId();
                                        listaId.add(result[k].getHotelId());
                                        Log.d("VOLO","ID salvato: "+ids[k]);
                                    }
                                  //  for (Hotel h : result) {

                                 //   }
                                },
                                error -> {
                                    // Gestisci gli errori qui
                                    Log.e("RxJava", "Errore: " + error.getMessage());
                                }
                        );
                listaId.add("HNPARKGU");
             // try {

                  service.fetchRoomsAsync(listaId,2,"2024-03-01","2024-03-08")
                          .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                          .subscribe(
                                  roomsResult -> {
                                      Log.d("VOLO","RISULTATO APERTO, totale risultati: " + roomsResult.length);
                                      for(int i=0;i<5 && i<roomsResult.length;i++) {
                                          Log.d("VOLO","Offerta "+i+": ");
                                          HotelOfferSearch.Offer[] offers = roomsResult[i].getOffers();
                                          for(int j=0;i<5 && j<offers.length;j++) {
                                              Log.d("VOLO",offers[j].getRoom().getDescription().toString());
                                              Log.d("VOLO", "Prezzo: " + offers[j].getPrice());
                                              Log.d("VOLO","Descrizione"+offers[j].getDescription());
                                              hotelResults.append("Prezzo: " + offers[j].getPrice());
                                              hotelResults.append("Descrizione"+offers[j].getDescription());
                                              hotelResults.append(offers[j].getRoom().getDescription().toString());
                                          }
                                      }
                                                            /*
                                                            for (HotelOfferSearch room : roomsResult) {
                                                                HotelOfferSearch.Offer[] offers = room.getOffers();
                                                                for (HotelOfferSearch.Offer o : offers) {
                                                                    Log.d("VOLO", "Prezzo: " + o.getPrice());
                                                                    Log.d("VOLO","Descrizione"+o.getDescription());
                                                                    hotelResults.append("Prezzo: " + o.getPrice());
                                                                    hotelResults.append("Descrizione"+o.getDescription());
                                                                }
                                                            }

                                                             */



                                  }, error -> {
                                      Log.e("RxJava", "Errore: " + error.getMessage());
                                  });
       /*       } catch (Exception e) {

              }

        */



            }
        });

        // WORKING, retrieves hotels from a given city name
        /* same as sopra
        submitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.fetchHotelAsync(originCityCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    for (Hotel h : result) {
                                        hotelResults.append(h.toString() + "\n\n");
                                    }
                                },
                                error -> {
                                    // Gestisci gli errori qui
                                    Log.e("RxJava", "Errore: " + error.getMessage());
                                }
                        );
            }
        });

         */

    }
}
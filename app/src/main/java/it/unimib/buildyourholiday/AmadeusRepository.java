package it.unimib.buildyourholiday;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.unimib.buildyourholiday.adapter.FlightListAdapter;
import it.unimib.buildyourholiday.adapter.HotelListAdapter;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;
import static it.unimib.buildyourholiday.util.Constants.TOTAL_HOTEL_RESULTS;

/**
  * Class to combine AmadeusService' methods in order to perform calls for travel researches.
 */
public class AmadeusRepository {
    private static final AmadeusService service = new AmadeusService();
   // private static FlightListViewModel flightListViewModel;       posso evitare di passare vm come parametro????
    private FlightListAdapter flightListAdapter = null;
    private HotelListAdapter hotelListAdapter = null;
    private static String cityCodeHolder; private static String cityNameHolder;
    private static int adultsHolder; private static String checkoutDateHolder;

    // invoco tramite parametri e passando il listener creato dal fragment, così posso gesitre l'onclick sugli item
    // dopo l'invocazione, assegno l'adapter ritornato alla recycler view
    public static void flightSearch(String originCityCode, String destinationCityCode, String departureDate,
                                    String returnDate, int adults, FlightListViewModel viewModel) {
         service.fetchFlightsAsync(originCityCode, destinationCityCode, departureDate, returnDate, adults)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        result -> {
                            Log.d("RxJava", "result");

                            if (result != null && result.length > 0) {       // evita caso return sia null
                                List<Flight> flights = new ArrayList<>();
                                List<String> durations = new ArrayList<>();
                                List<Boolean> directFlight = new ArrayList<>();
                                String out = "";
                                for (int i = 0; i < result.length; i++) {

                                    Log.d("RxJava", "flights pos: " + i);
                                    String flightCode = result[i].getItineraries()[0].getSegments()[0].getCarrierCode()
                                            + result[i].getItineraries()[0].getSegments()[0].getNumber();
                                    Flight flight = new Flight(flightCode, departureDate, "time", "depAirport",
                                            returnDate, "rTime", "arrivAirport", Double.valueOf(result[i].getPrice().getGrandTotal()));
                                    FlightOfferSearch.Itinerary[] itineraries = result[i].getItineraries();
                                    flight.setDepartureTime(itineraries[0].getSegments()[0].getDeparture().getAt().substring(10));
                                    flight.setDepartureAirport(itineraries[0].getSegments()[0].getDeparture().getIataCode());
                                    int arrivalIndex = itineraries[0].getSegments().length;
                                    flight.setArrivalAirport(itineraries[0].getSegments()[arrivalIndex - 1].getArrival().getIataCode());
                                    int lastIndex = itineraries[itineraries.length - 1].getSegments().length;
                                    flight.setReturnalTime(itineraries[itineraries.length - 1].getSegments()[lastIndex - 1].getDeparture().getAt());

                                    flights.add(flight);

                                    durations.add(result[i].getItineraries()[0].getDuration().substring(2));
                                    directFlight.add(result[i].getItineraries()[0].getSegments().length == 1);
                                }
                                Log.d("RxJava", "flights instance: " + (flights != null) + ", flights size: " + flights.size());

                                viewModel.setFlightList(flights);
                                viewModel.setDurations(durations);
                                viewModel.setDirectFlight(directFlight);
                            }

                        }
                );
    }

    public static void holdParameters(String cityCode, String cityName, int adults, String checkoutDate) {
        cityCodeHolder = cityCode;
        cityNameHolder = cityName;
        adultsHolder = adults;
        checkoutDateHolder = checkoutDate;
    }

    public static void hotelSearch(String checkinDate, double price, HotelListViewModel viewModel) {
        try {
            hotelSearch(cityCodeHolder, cityNameHolder, adultsHolder, checkinDate, checkoutDateHolder, price, viewModel);
        } catch (NoHotelsForPriceException e) {
            viewModel.setResultsPriceError(true);
        }
    }

    public static Disposable hotelSearch(String cityCode, String cityName, int adults, String checkinDate, String checkoutDate,
                                         double price, HotelListViewModel viewModel) throws NoHotelsForPriceException{
        return service.fetchHotelAsync(cityCode)
                .flatMap(hotelResult -> {
                    List<String> listaId = new ArrayList<>();
                    String[] ids = new String[TOTAL_HOTEL_RESULTS];
                    for (int k = 0; k < ids.length && k < hotelResult.length; k++) {
                        ids[k] = hotelResult[k].getHotelId();
                        listaId.add(hotelResult[k].getHotelId());
                    }
                    // testing hotel
                    listaId.add("HNPARKGU");

                    return service.fetchRoomsAsync(listaId, adults, checkinDate, checkoutDate, price);
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
                                            roomsResult[i].getHotel().getCityCode(),cityName,roomsResult[i].getOffers()[0].getCheckInDate(),
                                            roomsResult[i].getOffers()[0].getCheckOutDate(),adults,Double.valueOf(roomsResult[i].getOffers()[0].getPrice().getTotal()));
                                    hotels.add(h);
                                    links.add("geo:"+roomsResult[i].getHotel().getLatitude()+","+roomsResult[i].getHotel().getLongitude());
                                    descriptions.add(roomsResult[i].getOffers()[0].getRoom().getDescription().getText());
                                }

                                viewModel.setHotelListLiveData(hotels);
                                viewModel.setHotelDescriptionLiveData(descriptions);
                                viewModel.setHotelLinksLiveData(links);
                            }
                        },
                        throwable -> {
                            // Gestisci gli errori qui
                            Log.e("RxJava", "Errore durante l'esecuzione asincrona: " + throwable.getMessage());
                        }
                );
    }

    public static void setupBarSearch(Activity activity, AutoCompleteTextView autoCompleteTextView, LuoghiAdapter luoghiAdapter) {
        autoCompleteTextView.setAdapter(luoghiAdapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // todo: if statement might be unnecessary
                if(s.toString().length() > 3) {
                    service.fetchLocationAsync(s.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    result -> {
                                        Location[] response = result;
                                        // todo: getActivity(). working?
                                        activity.runOnUiThread(() -> luoghiAdapter.setList(response));
                                        Log.d("RxJava", "Risultato scritto: " + response[0].getName() + "...");
                                        autoCompleteTextView.invalidate();
                                    },
                                    error -> {
                                        // Gestisci gli errori qui
                                        Log.e("RxJava", "Errore: " + error.getMessage());
                                    }
                            );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing to do
            }
        });
    }

    public static void findCityId(String searchQuery, MutableLiveData<String> resultCityCode) {
        // WORKING, giving back city code for an input
        service.fetchLocationAsync(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            String response = result[0].getIataCode();
                            resultCityCode.postValue(response);
                            Log.d("RxJava", "Risultato: " + response);
                        },
                        error -> {
                            // Gestisci gli errori qui
                            Log.e("RxJava", "Errore: " + error.getMessage());
                        }
                );
    }
}

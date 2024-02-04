package it.unimib.buildyourholiday;

import static android.graphics.Insets.add;
import static it.unimib.buildyourholiday.util.Constants.RATE_LIMIT_TIME;
import static it.unimib.buildyourholiday.util.Constants.TOTAL_FLIGHTS_RESULTS;

import androidx.annotation.Nullable;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.Location;
import com.amadeus.resources.Hotel;
import com.amadeus.shopping.FlightOffersSearch;


import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelRemoteDataSource;

public class AmadeusService extends BaseTravelRemoteDataSource {
    private Amadeus amadeus = Amadeus
            .builder("UWAFKYZec0REMjNs6agWEur2IpEnZ78H","D9AZ2ZHEW8CkgYBH")
            .build();

    public Observable<Location[]> fetchLocationAsync(String hint) {
        return Observable.create((ObservableOnSubscribe<Location[]>) emitter -> {
            // Effettua la tua chiamata in background qui
            Location[] result = getLocation(hint);

            // Invia il risultato all'emitter
            emitter.onNext(result);

            // Completa l'observable
            emitter.onComplete();
        });
    }

    public Location[] getLocation(String hint) throws ResponseException {
        // Get a specific city or airport based on its id
        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", hint)
                .and("subType", Locations.CITY));

        if(locations[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + locations[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (locations);
    }

    public Observable<Hotel[]> fetchHotelAsync(String cityCode) {
        return Observable.create((ObservableOnSubscribe<Hotel[]>) emitter -> {
            // to not exceed api rate-limit
            Thread.sleep(RATE_LIMIT_TIME);
            // Effettua la tua chiamata in background qui
            Hotel[] result = getHotels(cityCode);

            // Invia il risultato all'emitter
            emitter.onNext(result);

            // Completa l'observable
            emitter.onComplete();
        });
    }

    public Hotel[] getHotels(String cityCode) throws ResponseException {
        //get a list of hotels in a given city
        Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", "PAR").and("ratings",1).and("radius",3));

        if (hotels[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + hotels[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (hotels);
    }

    public Observable<HotelOfferSearch[]> fetchRoomsAsync(List<String> hotelCodes, int adults, String checkIn, String checkOut) {
        return Observable.create((ObservableOnSubscribe<HotelOfferSearch[]>) emitter -> {
            // to not exceed api rate-limit
            Thread.sleep(RATE_LIMIT_TIME);
            // Effettua la tua chiamata in background qui
            HotelOfferSearch[] result = getRooms(hotelCodes, adults, checkIn, checkOut);

            // Invia il risultato all'emitter
            emitter.onNext(result);

            // Completa l'observable
            emitter.onComplete();

           // emitter.onError(new Exception("No rooms found"));
        });
    }

    public HotelOfferSearch[] getRooms(List<String> hotelCodes, int adults, String checkIn, String checkOut) throws ResponseException {
        //get a list of hotels in a given city
        HotelOfferSearch[] rooms = amadeus.shopping.hotelOffersSearch.get(
                Params.with("hotelIds", hotelCodes).and("adults",adults).and("checkInDate", checkIn)
                        .and("checkOutDate", checkOut).and("bestRateOnly",true));

        if (rooms[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + rooms[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (rooms);
    }

    public Observable<FlightOfferSearch[]> fetchFlightsAsync(String originCityCode, String destinationCityCode, String departureDate, @Nullable String returnDate, int adults) {
        return Observable.create((ObservableOnSubscribe<FlightOfferSearch[]>) emitter -> {
            // to not exceed api rate-limit
            Thread.sleep(RATE_LIMIT_TIME);
            // Effettua la tua chiamata in background qui
            FlightOfferSearch[] result = getFlights(originCityCode, destinationCityCode, departureDate,
                    returnDate, adults);

            // Invia il risultato all'emitter
            emitter.onNext(result);

            // Completa l'observable
            emitter.onComplete();
        });
    }

    public FlightOfferSearch[] getFlights(String originCityCode, String destinationCityCode, String departureDate, @Nullable String returnDate, int adults) throws ResponseException {
        FlightOfferSearch[] flights = null;

        //get a list of hotels in a given city
        if(returnDate==null || returnDate.isEmpty()) {
             flights = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", originCityCode).and("destinationLocationCode", destinationCityCode)
                            .and("departureDate", departureDate).and("adults", adults)
                            .and("max", TOTAL_FLIGHTS_RESULTS));
        } else {
            flights = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", originCityCode).and("destinationLocationCode", destinationCityCode)
                            .and("departureDate", departureDate).and("returnDate", returnDate).and("adults", adults)
                            .and("max", TOTAL_FLIGHTS_RESULTS));
        }

        if (flights[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + flights[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (flights);
    }


}

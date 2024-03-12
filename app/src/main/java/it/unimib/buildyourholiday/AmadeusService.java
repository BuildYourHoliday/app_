package it.unimib.buildyourholiday;

import static android.graphics.Insets.add;
import static it.unimib.buildyourholiday.util.Constants.RATE_LIMIT_TIME;
import static it.unimib.buildyourholiday.util.Constants.TOTAL_FLIGHTS_RESULTS;

import android.service.credentials.CreateEntry;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.FlightPayment;
import com.amadeus.resources.FlightPrice;
import com.amadeus.resources.HotelBooking;
import com.amadeus.resources.HotelOfferSearch;
import com.amadeus.resources.Location;
import com.amadeus.resources.Hotel;
import com.amadeus.shopping.FlightOffers;
import com.amadeus.shopping.FlightOffersSearch;
import com.google.firebase.database.collection.BuildConfig;

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
      /*  HotelOfferSearch[] hotelOfferSearches = amadeus.shopping.hotelOffersSearch.get(
                Params.with("cityCode", "PAR").and("checkInDate","2024-03-01")
                        .and("checkOutDate","2024-03-08")
        );

       */

        Hotel[] hotels = amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", "PAR").and("ratings",1).and("radius",3));



        // hotelOfferSearches[0].getOffers()[0].getPrice().getTotal();
       // if (hotelOfferSearches[0].getResponse().getStatusCode() != 200 ) {
       //     System.out.println("Wrong status code: " + hotelOfferSearches[0].getResponse().getStatusCode());
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
        HotelOfferSearch[] rooms;
        if(checkOut!=null && !checkOut.isEmpty()) {
            Log.d("RxJava","checkout: "+checkOut);

            rooms = amadeus.shopping.hotelOffersSearch.get(
                    Params.with("hotelIds", hotelCodes).and("adults",adults).and("checkInDate", checkIn)
                            .and("checkOutDate", checkOut).and("bestRateOnly",true).and("currency","EUR"));
        } else {
            rooms = amadeus.shopping.hotelOffersSearch.get(
                    Params.with("hotelIds", hotelCodes).and("adults",adults).and("checkInDate", checkIn)
                            .and("bestRateOnly",true).and("currency","EUR"));
        }


        if (rooms[0].getResponse().getStatusCode() != 200 && rooms[0].getResponse().getStatusCode() != 424) {
            System.out.println("Wrong status code: " + rooms[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (rooms);
    }

    // SEARCH WITH PRICE PARAMETER
    public Observable<HotelOfferSearch[]> fetchRoomsAsync(List<String> hotelCodes, int adults, String checkIn, String checkOut, double price) throws NoHotelsForPriceException {
        return Observable.create((ObservableOnSubscribe<HotelOfferSearch[]>) emitter -> {
            // to not exceed api rate-limit
            Thread.sleep(RATE_LIMIT_TIME);
            // Effettua la tua chiamata in background qui
            HotelOfferSearch[] result;
            if(price>0) {
                Log.d("AmadeusRepository","going for case price>0");
                result = getRooms(hotelCodes, adults, checkIn, checkOut, price);
            } else {
                Log.d("AmadeusRepository","going for case price <= 0");
                result = getRooms(hotelCodes, adults, checkIn, checkOut);
            }

            // Invia il risultato all'emitter
            if(result[0].getResponse().getStatusCode() == 424) {
                result = getRooms(hotelCodes, adults, checkIn, checkOut);
                emitter.onNext(result);
                emitter.onComplete();
                throw new NoHotelsForPriceException();

            } else {
                emitter.onNext(result);

                // Completa l'observable
                emitter.onComplete();
            }


            // emitter.onError(new Exception("No rooms found"));
        });
    }

    public HotelOfferSearch[] getRooms(List<String> hotelCodes, int adults, String checkIn, String checkOut, double price) throws ResponseException {
        Log.d("RxJava","hotel codes size: "+hotelCodes.size());
        Log.d("RxJava","adults: "+adults);
        Log.d("RxJava","checkin: "+checkIn);
        Log.d("RxJava","price: "+(int)price);

        HotelOfferSearch[] rooms;
        if(checkOut!=null && !checkOut.isEmpty()) {
            Log.d("RxJava","checkout: "+checkOut);

            rooms = amadeus.shopping.hotelOffersSearch.get(
                    Params.with("hotelIds", hotelCodes).and("adults",adults).and("checkInDate", checkIn)
                            .and("checkOutDate", checkOut).and("bestRateOnly",true).and("currency","EUR").and("priceRange",price));
        } else {
            rooms = amadeus.shopping.hotelOffersSearch.get(
                    Params.with("hotelIds", hotelCodes).and("adults",adults).and("checkInDate", checkIn)
                            .and("bestRateOnly",true).and("currency","EUR").and("priceRange","-"+((int)price)));
        }

        if (rooms[0].getResponse().getStatusCode() != 200 && rooms[0].getResponse().getStatusCode() != 424) {
            System.out.println("Wrong status code: " + rooms[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (rooms);
    }

    public Observable<FlightOfferSearch[]> fetchFlightsAsync(String originCityCode, String destinationCityCode, String departureDate, @Nullable String returnDate, int adults) {
        return Observable.create((ObservableOnSubscribe<FlightOfferSearch[]>) emitter -> {
            // to not exceed api rate-limit
            // Thread.sleep(RATE_LIMIT_TIME);
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

        // .and("maxPrice",intero a persona)

        //get a list of hotels in a given city
        if(returnDate==null || returnDate.isEmpty()) {
             flights = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", originCityCode).and("destinationLocationCode", destinationCityCode)
                            .and("departureDate", departureDate).and("adults", adults)
                            .and("max", TOTAL_FLIGHTS_RESULTS).and("currencyCode","EUR"));
        } else {
            flights = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", originCityCode).and("destinationLocationCode", destinationCityCode)
                            .and("departureDate", departureDate).and("returnDate", returnDate).and("adults", adults)
                            .and("max", TOTAL_FLIGHTS_RESULTS).and("currencyCode","EUR"));
        }

        if (flights==null || flights.length==0) {
            System.out.println("No result obtained");
            System.exit(-1);
        }
        if (flights!=null && flights[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + flights[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (flights);
    }

    public Observable<Boolean> bookTravelAsync(FlightOfferSearch flightOffer, FlightOrder.Traveler travelers[], String email, HotelOfferSearch hotelOffer) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {

            travelers[0].getContact().getPhones()[0].setCountryCallingCode("33");
            travelers[0].getContact().getPhones()[0].setNumber("675426222");

            // to not exceed api rate-limit
            // Thread.sleep(RATE_LIMIT_TIME);
            // Effettua la tua chiamata in background qui
           // Boolean flightResult = bookFlight(flightOffer, travelers);
           // Log.d("PurchaseFragment","flight booking: "+flightResult);

            String body = "{\"data\""
                    + ":{\"offerId\":\""+hotelOffer.getOffers()[0].getId()+"\"" + ",\"guests\":[";
            String phoneComplete = "+" + travelers[0].getContact().getPhones()[0].getCountryCallingCode() +
                    travelers[0].getContact().getPhones()[0].getNumber();

            for (int i=0; i<travelers.length; i++) {
                body += "{\"id\":"+(i+1)+",\"name\":{\"title\":\"MR\",\"firstName\":\""+travelers[i].getName().getFirstName()+"\","
                        + "\"lastName\" :\""+travelers[i].getName().getLastName()+"\"},\"contact\":{\"phone\":\""+phoneComplete+"\",\""
                        + "email\":\""+email+"\"}}";
                if(i == travelers.length-1) {
                    body += "],\"";
                } else {
                    body += ",";
                }
            }

            // VISA card for testing
            body += "payments\":[{\"id\":1,\"method\":\"creditCard\",\""
                    + "card\":{\""+"vendorCode\":\"VI\",\"cardNumber\""
                    + ":\"4111111111111111\",\"expiryDate\":\"2025-01\"}}]}}";
            Log.d("PurchaseFragment",body);

            Boolean hotelResult = bookHotel(body);
            Boolean result =  hotelResult;
            Log.d("PurchaseFragment","hotel booking: "+hotelResult);

            // Invia il risultato all'emitter
            emitter.onNext(result);

            // Completa l'observable
            emitter.onComplete();
        });
    }

    public boolean bookFlight(FlightOfferSearch flightOffer, FlightOrder.Traveler traveler[]) throws ResponseException {
        //TODO: null check
        FlightPrice flightPrice = amadeus.shopping.flightOffersSearch.pricing.post(flightOffer);
        FlightOrder order = amadeus.booking.flightOrders.post(flightPrice, traveler);

        if(order.getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + order.getResponse().getStatusCode());
            return false;
        }

        return true;
    }

    public boolean bookHotel(String body) throws ResponseException {
        //TODO: null check
        HotelBooking[] hotel = amadeus.booking.hotelBookings.post(body);

        if(hotel[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + hotel[0].getResponse().getStatusCode());
            return false;
        }

        return true;
    }

}

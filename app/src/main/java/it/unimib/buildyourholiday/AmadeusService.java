package it.unimib.buildyourholiday;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.Location;
import com.amadeus.resources.Hotel;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class AmadeusService {
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
                Params.with("cityCode", "PAR"));

        if (hotels[0].getResponse().getStatusCode() != 200) {
            System.out.println("Wrong status code: " + hotels[0].getResponse().getStatusCode());
            System.exit(-1);
        }

        return (hotels);
    }

}

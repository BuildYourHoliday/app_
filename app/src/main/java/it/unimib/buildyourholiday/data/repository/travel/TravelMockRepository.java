package it.unimib.buildyourholiday.data.repository.travel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.data.database.TravelDao;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;
import it.unimib.buildyourholiday.model.Travel;

public class TravelMockRepository {
    private TravelDao travelDao;
    private final MutableLiveData<List<Travel>> travelLiveData = new MutableLiveData<>();

    public TravelMockRepository(Application application) {
        Hotel hotel = new Hotel("HOTEL BEAUVOIR", "FGPARVOI", "PAR", "Paris", "2024-03-01", "2024-03-08", 2, 322.00);
        Flight flight = new Flight("738", "2024-03-01", "", "BKK", "PAR", 868.04);

        Travel travel = new Travel(flight, hotel);
        travel.setCountry("FR");

        List<Travel> currentTravels = travelLiveData.getValue();
        if (currentTravels == null) {
            currentTravels = new ArrayList<>();
        }
        currentTravels.add(travel);

        TravelsRoomDatabase db = TravelsRoomDatabase.getDatabase(application);
        travelDao = db.travelDao();

        travelLiveData.setValue(currentTravels);
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            travelDao.insertTravel(travel);

        });
    }

    public LiveData<List<Travel>> getALlTravel() {
        return travelLiveData;
    }

    public List<Travel> getAllTravel() {
        return travelLiveData.getValue();
    }

}

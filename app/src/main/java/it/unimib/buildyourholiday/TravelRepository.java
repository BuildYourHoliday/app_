package it.unimib.buildyourholiday;

import android.content.Context;
import android.util.Log;

import it.unimib.buildyourholiday.data.database.TravelDao;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.model.Travel;

public class TravelRepository {
    private TravelDao travelDao;

    public TravelRepository(Context context) {
        TravelsRoomDatabase db = TravelsRoomDatabase.getDatabase(context);
        travelDao = db.travelDao();
    }

    public Travel getTravelById(int travelId) {
        return travelDao.getTravel(travelId);
    }

    public void insertTravel(Travel travel) {
        Log.d("TravelRepo","booked: "+travel.isBooked());
        travelDao.insertTravel(travel);
    }

    public void deleteTrave(Travel travel) {
        travelDao.delete(travel);
    }
}

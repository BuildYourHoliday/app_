package it.unimib.buildyourholiday.data.source.travel;

import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.GENERIC_ERROR;
import static it.unimib.buildyourholiday.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.data.database.TravelDao;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.util.DataEncryptionUtil;
import it.unimib.buildyourholiday.util.SharedPreferencesUtil;

/**
 * Class to get news from local database using Room.
 */
public class TravelLocalDataSource extends BaseTravelLocalDataSource {

    private final TravelDao travelDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;
    private final DataEncryptionUtil dataEncryptionUtil;

    public TravelLocalDataSource(TravelsRoomDatabase travelsRoomDatabase,
                                 SharedPreferencesUtil sharedPreferencesUtil,
                                 DataEncryptionUtil dataEncryptionUtil
                               ) {
        this.travelDao = travelsRoomDatabase.travelDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
        this.dataEncryptionUtil = dataEncryptionUtil;
    }

    @Override
    public void getSavedTravels(String country) {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d("MapFragment","eseguo ricerca per: "+country);
            List<Travel> debugAll = travelDao.getAllSaved();
            Log.d("MapFragment","presente nel db: "+debugAll.get(0).getCountry()+" ...");
            //Travel tDebug = travelDao.getTravel(1);
            //Log.d("MapFragment","id test: "+tDebug.toString());
            List<Travel> savedTravels = travelDao.getTravels(country);
            Log.d("MapFragment","in TravelLocalDataSource, dopo la query: "+ (savedTravels.size())+"");
            travelCallback.onTravelSavedStatusChanged(savedTravels);
        });
    }

    @Override
    public void getSavedTravels() {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Travel> savedTravels = travelDao.getAllSaved();
            Log.d("MapFragment","TRVLLCLDS: "+savedTravels.size());
            travelCallback.onTravelSavedStatusChanged(savedTravels);
        });
    }

    @Override
    public void updateTravel(Travel travel) {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (travel != null) {
                int rowUpdatedCounter = travelDao.updateSingleSavedTravel(travel);
                // It means that the update succeeded because only one row had to be updated
                if (rowUpdatedCounter == 1) {
                    Travel updatedTravel = travelDao.getTravel(travel.getId());
                    travelCallback.onTravelSavedStatusChanged(updatedTravel, travelDao.getAllSaved());
                } else {
                    travelCallback.onFailureFromLocal(new Exception(GENERIC_ERROR));
                }
            } else {
                // When the user deleted all favorite news from remote
                List<Travel> allTravels = travelDao.getAllSaved();
                for (Travel t : allTravels) {
                    t.setSynchronized(false);
                    travelDao.updateSingleSavedTravel(t);
                }
            }
        });
    }

    @Override
    public void deleteSavedTravels() {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Travel> savedTravels = travelDao.getAllSaved();
            for (Travel travel : savedTravels) {
                travelDao.delete(travel);      //TODO va bene???
            }

            if (travelDao.getAllSaved().size()==0) {
                travelCallback.onDeleteFavoriteTravelSuccess(savedTravels);
            } else {
                travelCallback.onFailureFromLocal(new Exception(GENERIC_ERROR));
            }
        });
    }

    /**
     * Saves the news in the local database.
     * The method is executed with an ExecutorService defined in TravelsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param travelsList the list of travels to be written in the local database.
     */
    @Override
    public void insertTravels(List<Travel> travelsList) {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Travel> allTravels = travelDao.getAll();

            if (travelsList != null) {

                // Writes the news in the database and gets the associated primary keys
                List<Long> insertedIds = travelDao.insertTravelList(travelsList);
                for (int i = 0; i < travelsList.size(); i++) {
                    // Adds the primary key to the corresponding object News just downloaded so that
                    // if the user marks the news as favorite (and vice-versa), we can use its id
                    // to know which news in the database must be marked as favorite/not favorite
                    travelsList.get(i).setId(insertedIds.get(i));
                }

                sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                        "last_update", String.valueOf(System.currentTimeMillis()));

                travelCallback.onSuccessFromLocal(travelsList);
            }
        });
    }

    @Override
    public void insertTravel(Travel travel) {
        Log.d("TravelRepo","booked: "+travel.isBooked());
        List<Travel> singleTravel = new ArrayList<>();
        singleTravel.add(travel);
        insertTravels(singleTravel);
    }

    @Override
    public void deleteAll() {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            int travelsCounter = travelDao.getAllSaved().size();
            int travelsDeleted = travelDao.deleteAll();
            Log.d("TravelLocalDataSource","deleted from local");

            // It means that everything has been deleted
            if (travelsCounter == travelsDeleted) {
                sharedPreferencesUtil.deleteAll(SHARED_PREFERENCES_FILE_NAME);
                dataEncryptionUtil.deleteAll(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ENCRYPTED_DATA_FILE_NAME);
                travelCallback.onSuccessDeletion();
            }
        });
    }

    @Override
    public void deleteSavedTravel(Travel travel) {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            int travelsCounter = travelDao.getAllSaved().size();
            travelDao.delete(travel);
            List<Travel> travelList = travelDao.getAllSaved();
            int travelsDeleted = travelList.size();

            if (travelsCounter != travelsDeleted) {
                travelCallback.onDeleteFavoriteTravelSuccess(travelList, travel);
            }

        });
    }

    @Override
    public void getBookedTravels() {
        TravelsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Travel> bookedTravels = travelDao.getAllBooked();
            travelCallback.onTravelSavedStatusChanged(bookedTravels);
        });
    }
}

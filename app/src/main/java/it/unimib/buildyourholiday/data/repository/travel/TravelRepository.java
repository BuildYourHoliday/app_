package it.unimib.buildyourholiday.data.repository.travel;

import static it.unimib.buildyourholiday.util.Constants.FRESH_TIMEOUT;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.data.source.travel.BaseSavedTravelDataSource;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelLocalDataSource;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelRemoteDataSource;
import it.unimib.buildyourholiday.data.source.travel.TravelCallback;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.TravelResponse;

/**
 * Repository class to get the news from local or from a remote source.
 */
public class TravelRepository implements ITravelRepository, TravelCallback {

    private static final String TAG = TravelRepository.class.getSimpleName();

    private final MutableLiveData<Result> savedTravelsMutableLiveData;
    private final MutableLiveData<Result> bookedTravelsMutableLiveData;
    private final BaseTravelRemoteDataSource travelRemoteDataSource;
    private final BaseTravelLocalDataSource travelLocalDataSource;
    private final BaseSavedTravelDataSource backupDataSource;

    public TravelRepository(BaseTravelRemoteDataSource travelRemoteDataSource,
                            BaseTravelLocalDataSource travelLocalDataSource,
                            BaseSavedTravelDataSource savedTravelDataSource) {

        savedTravelsMutableLiveData = new MutableLiveData<>();
        bookedTravelsMutableLiveData = new MutableLiveData<>();
        this.travelRemoteDataSource = travelRemoteDataSource;
        this.travelLocalDataSource = travelLocalDataSource;
        this.backupDataSource = savedTravelDataSource;
        this.travelRemoteDataSource.setTravelCallback(this);
        this.travelLocalDataSource.setTravelCallback(this);
        this.backupDataSource.setTravelCallback(this);
    }

    @Override
    public MutableLiveData<Result> getSavedTravels(boolean isFirstLoading) {
        // The first time the user launches the app, check if she
        // has previously saved favorite travel on the cloud
        if (isFirstLoading) {
            backupDataSource.getSavedTravels();
        } else {
            travelLocalDataSource.getSavedTravels();
        }
        return savedTravelsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getSavedTravels(String country, int page, long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            travelRemoteDataSource.getTravels(country, page);
        } else {
            travelLocalDataSource.getSavedTravels(country);
        }
        return savedTravelsMutableLiveData;
    }

    @Override
    public void updateSavedTravel(Travel travel) {
        travelLocalDataSource.updateTravel(travel);
        backupDataSource.addTravel(travel);
    }

    @Override
    public void deleteSavedTravels() {
        travelLocalDataSource.deleteSavedTravels();
    }

    @Override
    public MutableLiveData<Result> fetchSavedTravels(String country) {
        travelLocalDataSource.getSavedTravels(country);
        return savedTravelsMutableLiveData;
    }

    @Override
    public void deleteSavedTravel(Travel travel) {
        travelLocalDataSource.deleteSavedTravel(travel);
    }

    @Override
    public void saveSavedTravel(Travel travel) {
        travelLocalDataSource.insertTravel(travel);
        //backupDataSource.addTravel(travel);
    }

    @Override
    public MutableLiveData<Result> fetchAllSavedTravels() {
        travelLocalDataSource.getSavedTravels();
        return savedTravelsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> fetchAllBookedTravels() {
        travelLocalDataSource.getBookedTravels();
        return bookedTravelsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getBookedTravels(boolean firstLoading) {
        // The first time the user launches the app, check if she
        // has previously saved favorite news on the cloud
        if (firstLoading) {
            backupDataSource.getBookedTravels();
        } else {
            travelLocalDataSource.getBookedTravels();
        }
        return bookedTravelsMutableLiveData;
    }


    @Override
    public void onSuccessFromRemote(List<Travel> travelList, long lastUpdate) {
        travelLocalDataSource.insertTravels(travelList);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        savedTravelsMutableLiveData.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onSuccessFromLocal(List<Travel> travelList) {
        Log.d("SaveID","associated id: "+travelList.get(0).getId());
        backupDataSource.addTravel(travelList.get(0));
        if(savedTravelsMutableLiveData.getValue()!=null && savedTravelsMutableLiveData.getValue().isSuccess()) {
            List<Travel> travels = ((Result.TravelResponseSuccess)savedTravelsMutableLiveData.getValue()).getData().getTravelList();
            travels.addAll(travelList);
            ((Result.TravelResponseSuccess) savedTravelsMutableLiveData.getValue()).getData().setTravelList(travels);
        } else {
            //savedTravelsMutableLiveData.postValue(new Result.Error(exception.getMessage()));
        }
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        savedTravelsMutableLiveData.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onTravelSavedStatusChanged(Travel travel, List<Travel> savedTravels) {
        Result allTravels = savedTravelsMutableLiveData.getValue();
        if(allTravels!=null && allTravels.isSuccess()) {
            List<Travel> oldAllTravels = ((Result.TravelResponseSuccess)allTravels).getData().getTravelList();
            if(oldAllTravels.contains(travel)) {
                oldAllTravels.set(oldAllTravels.indexOf(travel), travel);
                savedTravelsMutableLiveData.postValue(allTravels);
            }
        }
        savedTravelsMutableLiveData.postValue(new Result.TravelResponseSuccess(new TravelResponse(savedTravels)));
    }

    @Override
    public void onTravelSavedStatusChanged(List<Travel> travelList) {
       List<Travel> notSynchronized = new ArrayList<>();
        for (Travel t:travelList) {
            if(!t.isSynchronized())
                notSynchronized.add(t);
        }

        if(!notSynchronized.isEmpty()) {
            backupDataSource.synchronizeSavedTravels(notSynchronized);
        }

        savedTravelsMutableLiveData.postValue(new Result.TravelResponseSuccess(new TravelResponse(travelList)));

    }

    @Override
    public void onDeleteFavoriteNewsSuccess(List<Travel> travelList) {
        Result allTravels = savedTravelsMutableLiveData.getValue();

        if (allTravels != null && allTravels.isSuccess()) {
            List<Travel> oldTravels = ((Result.TravelResponseSuccess)allTravels).getData().getTravelList();
            for (Travel t: travelList) {
                if (oldTravels.contains(t)) {
                    oldTravels.set(oldTravels.indexOf(t), t);
                }
            }
            savedTravelsMutableLiveData.postValue(allTravels);
        }

        if (savedTravelsMutableLiveData.getValue() != null &&
                savedTravelsMutableLiveData.getValue().isSuccess()) {
            travelList.clear();
            Result.TravelResponseSuccess result = new Result.TravelResponseSuccess(new TravelResponse(travelList));
            savedTravelsMutableLiveData.postValue(result);
        }

        backupDataSource.deleteAllSavedTravels();
    }

    @Override
    public void onSuccessFromCloudReading(List<Travel> travelList) {
        // Saved travel got from Realtime Database the first time
        if (travelList != null) {
            for (Travel t : travelList) {
                t.setSynchronized(true);
            }
            //travelLocalDataSource.insertTravels(travelList);
            savedTravelsMutableLiveData.postValue(new Result.TravelResponseSuccess(new TravelResponse(travelList)));
        }
    }

    @Override
    public void onSuccessFromCloudWriting(Travel travel) {
        backupDataSource.getSavedTravels();
    }

    @Override
    public void onFailureFromCloud(Exception exception) {}

    @Override
    public void onSuccessSynchronization() {
        Log.d(TAG, "News synchronized from remote");
    }

    @Override
    public void onSuccessDeletion() {}

    @Override
    public void onSuccessFromBookedCloudReading(List<Travel> travelList) {
        // Booked travel got from Realtime Database the first time
        if (travelList != null) {
            for (Travel t : travelList) {
                t.setSynchronized(true);
            }
            travelLocalDataSource.insertTravels(travelList);
            bookedTravelsMutableLiveData.postValue(new Result.TravelResponseSuccess(new TravelResponse(travelList)));
        }
    }

    @Override
    public void onDeleteFavoriteNewsSuccess(List<Travel> travelList, Travel deletedTravel) {
        Result allTravels = savedTravelsMutableLiveData.getValue();

        if (allTravels != null && allTravels.isSuccess()) {
            List<Travel> oldTravels = ((Result.TravelResponseSuccess)allTravels).getData().getTravelList();
            for (Travel t: travelList) {
                if (oldTravels.contains(t)) {
                    oldTravels.set(oldTravels.indexOf(t), t);
                }
            }
            savedTravelsMutableLiveData.postValue(allTravels);
        }

        if (savedTravelsMutableLiveData.getValue() != null &&
                savedTravelsMutableLiveData.getValue().isSuccess()) {
            travelList.clear();
            Result.TravelResponseSuccess result = new Result.TravelResponseSuccess(new TravelResponse(travelList));
            savedTravelsMutableLiveData.postValue(result);
        }

        // ??
        backupDataSource.deleteSavedTravel(deletedTravel);
    }
}

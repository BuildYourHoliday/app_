package it.unimib.buildyourholiday.data.source.travel;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

/**
 * Interface to send data from DataSource to Repositories
 */
public interface TravelCallback {
    void onSuccessFromRemote(List<Travel> travelList, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Travel> travelList);
    void onFailureFromLocal(Exception exception);
    void onTravelSavedStatusChanged(Travel travel, List<Travel> savedTravels);
    void onTravelSavedStatusChanged(List<Travel> travelList);
    void onDeleteFavoriteTravelSuccess(List<Travel> travelList);
    void onSuccessFromCloudReading(List<Travel> travelList);
    void onSuccessFromCloudWriting(Travel travel);
    void onFailureFromCloud(Exception exception);
    void onSuccessSynchronization();
    void onSuccessDeletion();

    void onSuccessFromBookedCloudReading(List<Travel> travelList);

    void onDeleteFavoriteTravelSuccess(List<Travel> travelList, Travel deletedTravel);
}

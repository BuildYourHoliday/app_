package it.unimib.buildyourholiday.data.repository.travel;

import androidx.lifecycle.MutableLiveData;

import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;

public interface ITravelRepository {

    MutableLiveData<Result> getSavedTravels(boolean firstLoading);
    MutableLiveData<Result> getSavedTravels(String country, int page, long lastUpdate);
    void updateSavedTravel(Travel travel);

    void deleteSavedTravels();

    MutableLiveData<Result> fetchSavedTravels(String country);

    void deleteSavedTravel(Travel travel);

    void saveSavedTravel(Travel travel);

    MutableLiveData<Result> fetchAllSavedTravels();

    MutableLiveData<Result> fetchAllBookedTravels();

    MutableLiveData<Result> getBookedTravels(boolean firstLoading);
}

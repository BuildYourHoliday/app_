package it.unimib.buildyourholiday.data.repository.travel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;

public interface ITravelRepository {

    MutableLiveData<Result> getSavedTravels(boolean firstLoading);
    MutableLiveData<Result> getSavedTravels(String country, int page, long lastUpdate);
    void updateSavedTravel(Travel travel);

    void deleteSavedTravels();

    void fetchSavedTravels(String country, int page);

    void deleteSavedTravel(Travel travel);

    void saveSavedTravel(Travel travel);
}

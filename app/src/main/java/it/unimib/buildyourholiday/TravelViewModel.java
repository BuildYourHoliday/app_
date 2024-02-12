package it.unimib.buildyourholiday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;

public class TravelViewModel extends ViewModel {

    private static final String TAG = TravelViewModel.class.getSimpleName();

    private final ITravelRepository travelRepository;
    private int page;
    private int currentResults;
    private int totalResults;
    private boolean isLoading;
    private boolean firstLoading;
    private MutableLiveData<Result> travelListLiveData;
    private MutableLiveData<Result> bookedTravelListLiveData;

    public TravelViewModel(ITravelRepository travelRepository) {
        this.travelRepository = travelRepository;
        this.page = 1;
        this.totalResults = 0;
        this.firstLoading = true;
    }

    /**
     * Returns the LiveData object associated with the
     * news list to the Fragment/Activity.
     * @return The LiveData object associated with the news list.
     */
    public MutableLiveData<Result> getTravels(String country) {
        if (travelListLiveData == null) {
            travelListLiveData = new MutableLiveData<>();

        } fetchSavedTravels(country);
        return travelListLiveData;
    }

    /**
     * Returns the LiveData object associated with the
     * list of favorite news to the Fragment/Activity.
     * @return The LiveData object associated with the list of favorite news.
     */
    public MutableLiveData<Result> getSavedTravelsLiveData(boolean isFirstLoading) {
        if (travelListLiveData == null) {
            travelListLiveData = new MutableLiveData<>();
            getSavedTravels(isFirstLoading);
        }
        return travelListLiveData;
    }

    /**
     * Updates the news status.
     * @param travel The news to be updated.
     */
    public void updateTravel(Travel travel) {
        travelRepository.updateSavedTravel(travel);
    }

    public void fetchSavedTravels(String country) {
        travelListLiveData = travelRepository.fetchSavedTravels(country);
    }

    /**
     * It uses the Repository to download the news list
     * and to associate it with the LiveData object.
     */
    private void fetchTravels(String country, long lastUpdate) {
        travelListLiveData = travelRepository.getSavedTravels(country, page, lastUpdate);
    }



    /**
     * It uses the Repository to get the list of favorite news
     * and to associate it with the LiveData object.
     */
    private void getSavedTravels(boolean firstLoading) {
        travelListLiveData = travelRepository.getSavedTravels(firstLoading);
    }

    /**
     * Removes the news from the list of favorite news.
     * @param travel The travel to be removed from the list of saved ones.
     */
    public void removeFromSaved(Travel travel) {
        travelRepository.updateSavedTravel(travel);
    }

    /**
     * Clears the list of favorite news.
     */
    public void deleteAllSavedTravels() {
        travelRepository.deleteSavedTravels();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }

    public MutableLiveData<Result> getTravelResponseLiveData() {
        if (travelListLiveData == null) {
            travelListLiveData = new MutableLiveData<>();
        }
        return travelListLiveData;
    }

    public void setTravelListLiveData(MutableLiveData<Result> travelListLiveData) {
        this.travelListLiveData = travelListLiveData;
    }

    public void fetchAllSavedTravels() {
        travelListLiveData = travelRepository.fetchAllSavedTravels();
    }

    public void fetchAllBookedTravels() {
        travelListLiveData = travelRepository.fetchAllBookedTravels();
    }

    public LiveData<Result> getBookedTravelsLiveData(boolean isFirstLoading) {
        if (bookedTravelListLiveData == null) {
            bookedTravelListLiveData = new MutableLiveData<>();
            getBookedTravels(isFirstLoading);
        }
        return bookedTravelListLiveData;
    }

    private void getBookedTravels(boolean isFirstLoading) {
        bookedTravelListLiveData = travelRepository.getBookedTravels(firstLoading);
    }
}

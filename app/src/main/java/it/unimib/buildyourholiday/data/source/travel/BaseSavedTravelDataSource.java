package it.unimib.buildyourholiday.data.source.travel;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

public abstract class BaseSavedTravelDataSource {

    protected TravelCallback travelCallback;

    public void setTravelCallback(TravelCallback travelCallback) {
        this.travelCallback = travelCallback;
    }

    public abstract void getSavedTravels();
    public abstract void getSavedTravels(String country);
    public abstract void addTravel(Travel travel);
    public abstract void deleteSavedTravel(Travel travel);
    public abstract void deleteAllSavedTravels();
    public abstract void synchronizeSavedTravels(List<Travel> notSynchronizedTravelsList);

    public abstract void getBookedTravels();
}

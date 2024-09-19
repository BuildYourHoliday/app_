package it.unimib.buildyourholiday.data.source.travel;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

/**
 * Base class to get travels from a local source.
 */
public abstract class BaseTravelLocalDataSource {

    protected TravelCallback travelCallback;

    public void setTravelCallback(TravelCallback travelCallback) {
        this.travelCallback = travelCallback;
    }

    public abstract void getSavedTravels(String country);
    public abstract void getSavedTravels();
    public abstract void updateTravel(Travel travel);
    public abstract void deleteSavedTravels();
    public abstract void insertTravels(List<Travel> travelsList);
    public abstract void insertTravel(Travel travel);
    public abstract void deleteAll();

    public abstract void deleteSavedTravel(Travel travel);

    public abstract void getBookedTravels();
}

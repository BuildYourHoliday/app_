package it.unimib.buildyourholiday.data.source.travel;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

/**
 * Base class to get travels from a remote source.
 */
public abstract class BaseTravelRemoteDataSource {
    protected TravelCallback travelCallback;

    public void setTravelCallback(TravelCallback travelCallback) {
        this.travelCallback = travelCallback;
    }

    public void getTravels(String country, int page) {
    }
}

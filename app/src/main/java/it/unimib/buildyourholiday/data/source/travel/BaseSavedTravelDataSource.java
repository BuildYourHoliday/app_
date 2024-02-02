package it.unimib.buildyourholiday.data.source.travel;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

public abstract class BaseSavedTravelDataSource {

    public abstract void getSavedTravels();
    public abstract void addSavedTravel(Travel travel);
    public abstract void deleteSavedTravel(Travel travel);
    public abstract void deleteAllSavedTravels();
    public abstract void synchronizedFavoriteNews(List<Travel> notSynchronizedTravelsList);
}

package it.unimib.buildyourholiday.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelResponse {
    private boolean isLoading;

    @SerializedName("travels")
    private List<Travel> travelList;

    public TravelResponse() {}

    public TravelResponse(List<Travel> travelList) {
        this.travelList = travelList;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public List<Travel> getTravelList() {
        return travelList;
    }

    public void setTravelList(List<Travel> travelList) {
        this.travelList = travelList;
    }
}

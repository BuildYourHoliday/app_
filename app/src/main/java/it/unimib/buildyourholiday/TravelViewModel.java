package it.unimib.buildyourholiday;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.unimib.buildyourholiday.data.repository.travel.TravelMockRepository;
import it.unimib.buildyourholiday.model.Travel;

public class TravelViewModel extends AndroidViewModel {
    private TravelMockRepository repository;
    private final LiveData<List<Travel>> travels;

    public TravelViewModel(Application application) {
        super(application);
        repository = new TravelMockRepository(application);
        travels = repository.getALlTravel();
    }

    public LiveData<List<Travel>> getAllTravel() {
        return travels;
    }

    public void insertTravel(Travel travel) {
        // todo
    }
}

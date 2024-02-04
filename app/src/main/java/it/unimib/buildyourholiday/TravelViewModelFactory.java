package it.unimib.buildyourholiday;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;

/**
 * Custom ViewModelProvider to be able to have a custom constructor
 * for the NewsViewModel class.
 */
public class TravelViewModelFactory implements ViewModelProvider.Factory {

    private final ITravelRepository iTravelRepository;

    public TravelViewModelFactory(ITravelRepository iTravelRepository) {
        this.iTravelRepository = iTravelRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TravelViewModel(iTravelRepository);
    }
}

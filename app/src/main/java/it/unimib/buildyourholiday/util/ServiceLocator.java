package it.unimib.buildyourholiday.util;

import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ID_TOKEN;

import android.app.Application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.buildyourholiday.data.service.AmadeusService;
import it.unimib.buildyourholiday.data.database.TravelsRoomDatabase;
import it.unimib.buildyourholiday.data.repository.travel.ITravelRepository;
import it.unimib.buildyourholiday.data.repository.travel.TravelRepository;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.data.repository.user.UserRepository;
import it.unimib.buildyourholiday.data.source.travel.BaseSavedTravelDataSource;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelLocalDataSource;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelRemoteDataSource;
import it.unimib.buildyourholiday.data.source.travel.SavedTravelDataSource;
import it.unimib.buildyourholiday.data.source.travel.TravelLocalDataSource;
import it.unimib.buildyourholiday.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.buildyourholiday.data.source.user.BaseUserDataRemoteDataSource;
import it.unimib.buildyourholiday.data.source.user.UserAuthenticationRemoteDataSource;
import it.unimib.buildyourholiday.data.source.user.UserDataRemoteDataSource;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    /**
     * Returns an instance of ServiceLocator class.
     * @return An instance of ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Creates an instance of IUserRepository.
     * @return An instance of IUserRepository.
     */
    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationRemoteDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserDataRemoteDataSource(sharedPreferencesUtil);


        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        BaseTravelLocalDataSource travelLocalDataSource =
                new TravelLocalDataSource(getTravelDao(application),sharedPreferencesUtil,
                        dataEncryptionUtil);
        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource, travelLocalDataSource);
    }

    public TravelsRoomDatabase getTravelDao(Application application) {
        return TravelsRoomDatabase.getDatabase(application);
    }

    public ITravelRepository getTravelRepository(Application application) {
        BaseTravelRemoteDataSource travelRemoteDataSource;
        BaseTravelLocalDataSource travelLocalDataSource;
        BaseSavedTravelDataSource savedTravelDataSource;
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);
        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

       travelRemoteDataSource = new AmadeusService();


        travelLocalDataSource = new TravelLocalDataSource(getTravelDao(application),
                sharedPreferencesUtil, dataEncryptionUtil);

        try {
            savedTravelDataSource = new SavedTravelDataSource(dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN
                    )
            );
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }

        return new TravelRepository(travelRemoteDataSource,
                travelLocalDataSource, savedTravelDataSource);
    }

    /*public AmadeusService getTravelService() {
        return AmadeusService.class;
    }*/
}

package it.unimib.buildyourholiday.util;

import android.app.Application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.buildyourholiday.source.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.source.data.repository.user.UserRepository;
import it.unimib.buildyourholiday.source.data.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.buildyourholiday.source.data.user.BaseUserDataRemoteDataSource;
import it.unimib.buildyourholiday.source.data.user.UserAuthenticationRemoteDataSource;
import it.unimib.buildyourholiday.source.data.user.UserDataRemoteDataSource;

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

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }
}

package it.unimib.buildyourholiday.data.source.user;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.User;
import it.unimib.buildyourholiday.data.repository.user.UserResponseCallback;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
    public abstract void getUserSavedTravels(String idToken);
   // public abstract void saveUserSavedTravels(List<Travel> travelList, String idToken);
  //  public abstract void getUserPreferences(String idToken);
  //  public abstract void saveUserTravel(Travel travel, String idToken);
}

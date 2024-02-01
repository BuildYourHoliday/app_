package it.unimib.buildyourholiday.data.source.user;

import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.User;
import it.unimib.buildyourholiday.data.repository.user.user.UserResponseCallback;

/**
 * Base class to get the user data from a remote source.
 */
public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
  //  public abstract void getUserFavoriteNews(String idToken);
  //  public abstract void getUserPreferences(String idToken);
  //  public abstract void saveUserTravel(Travel travel, String idToken);
}

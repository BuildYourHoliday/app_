package it.unimib.buildyourholiday.source.data.repository.user;

import androidx.lifecycle.MutableLiveData;

import java.util.Set;

import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
  //  MutableLiveData<Result> getUserFavoriteNews(String idToken);
 //   MutableLiveData<Result> getUserPreferences(String idToken);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
  //  void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken);
}

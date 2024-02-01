package it.unimib.buildyourholiday.data.repository.user.user;

import java.util.List;

import it.unimib.buildyourholiday.model.User;

public interface UserResponseCallback {
    void onSuccessFromAuthentication(User user);
    void onFailureFromAuthentication(String message);
    void onSuccessFromRemoteDatabase(User user);
//    void onSuccessFromRemoteDatabase(List<News> newsList);
    void onSuccessFromGettingUserPreferences();
    void onFailureFromRemoteDatabase(String message);
    void onSuccessLogout();
}

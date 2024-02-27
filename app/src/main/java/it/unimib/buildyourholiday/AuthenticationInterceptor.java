package it.unimib.buildyourholiday;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.util.ServiceLocator;

/**
 * Class to redirect unlogged users to LoginFragment since they cannot access map and my section.
 */
public class AuthenticationInterceptor implements NavController.OnDestinationChangedListener {
    private final Context context;
    private final UserViewModel userViewModel;

    public AuthenticationInterceptor(Context context, UserViewModel userViewModel) {
        this.context = context;
        this.userViewModel = userViewModel;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        if(navDestination.getId() == R.id.mapFragment || navDestination.getId() == R.id.profileFragment) {
            if(!isUserLoggedIn()) {
                navController.navigate(R.id.loginFragment);
            }
        }
    }

    private boolean isUserLoggedIn() {
        if(userViewModel.getLoggedUser()!=null)
            return true;
        return false;
    }
}

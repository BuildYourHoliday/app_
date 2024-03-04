package it.unimib.buildyourholiday;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.util.ServiceLocator;

/**
 * Class to redirect unlogged users to LoginFragment since they cannot access 'Map' and 'My' sections.
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
                Log.d("Redirect","user not logged");
                navController.navigate(R.id.homeFragment);
                navController.navigate(R.id.loginActivity);
                Toast.makeText(context, context.getString(R.string.login_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isUserLoggedIn() {
        if(userViewModel.getLoggedUser()!=null)
            return true;
        return false;
    }
}

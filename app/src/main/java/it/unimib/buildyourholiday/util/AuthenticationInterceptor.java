package it.unimib.buildyourholiday.util;

import static com.google.gson.reflect.TypeToken.get;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.welcome.UserViewModel;

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

    public boolean isUserLoggedIn() {
        if(userViewModel.getLoggedUser()!=null)
            return true;
        return false;
    }
}

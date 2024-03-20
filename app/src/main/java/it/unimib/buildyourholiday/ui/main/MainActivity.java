package it.unimib.buildyourholiday.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.welcome.UserViewModel;
import it.unimib.buildyourholiday.ui.welcome.UserViewModelFactory;
import it.unimib.buildyourholiday.data.repository.user.IUserRepository;
import it.unimib.buildyourholiday.util.AuthenticationInterceptor;
import it.unimib.buildyourholiday.util.ServiceLocator;

public class MainActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(userRepository)).get(UserViewModel.class);


        // gestione collegamento fragment menu bar
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.mapFragment, R.id.profileFragment).build();

        navController.addOnDestinationChangedListener(new AuthenticationInterceptor(this,userViewModel));

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
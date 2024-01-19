package it.unimib.buildyourholiday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amadeus.resources.Hotel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AmadeusTesting extends AppCompatActivity {

    private AmadeusService service = new AmadeusService();
    private TextView searchCity;
    private EditText searchHotel;
    private Button submitHotel;
    private TextView hotelResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amadeus_testing);
        searchCity = findViewById(R.id.debugging);
        searchHotel = findViewById(R.id.hotelsFor);
            searchHotel.setText("PAR");
        submitHotel = findViewById(R.id.buttonHotel);
        hotelResults = findViewById(R.id.hotelResults);

        // WORKING, giving back city names for an input
        /*service.fetchLocationAsync("Mila")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            String response = result[0].getName();
                            searchCity.setText(response);
                            if(result.length>1) {
                                for(int i=1; i<result.length; i++) {
                                    searchCity.append("\n"+result[i].getName());
                                }
                            }
                            Log.d("RxJava", "Risultato: " + result);
                        },
                        error -> {
                            // Gestisci gli errori qui
                            Log.e("RxJava", "Errore: " + error.getMessage());
                        }
                );*/

        // WORKING, retrieves hotels from a given city name
        /*submitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.fetchHotelAsync(searchHotel.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    for (Hotel h : result) {
                                        hotelResults.append(h.toString() + "\n");
                                    }
                                },
                                error -> {
                                    // Gestisci gli errori qui
                                    Log.e("RxJava", "Errore: " + error.getMessage());
                                }
                        );
            }
        });*/

    }
}
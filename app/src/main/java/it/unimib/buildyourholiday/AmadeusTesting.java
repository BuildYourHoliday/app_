package it.unimib.buildyourholiday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.amadeus.resources.Hotel;
import com.amadeus.resources.Location;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AmadeusTesting extends AppCompatActivity {

    private AmadeusService service = new AmadeusService();
    private TextView searchCity;
    private EditText searchHotel;
    private Button submitHotel;
    private TextView hotelResults;
    private AutoCompleteTextView multiAutoCompleteTextView;
    private String currentCityCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amadeus_testing);
       // searchCity = findViewById(R.id.debugging);
       // searchHotel = findViewById(R.id.hotelsFor);
       //     searchHotel.setText("PAR");
        submitHotel = findViewById(R.id.buttonHotel);
        hotelResults = findViewById(R.id.hotelResults);



        // WORKING*, autoComplete not showing results tho
        multiAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        String[] DEFAULT = new String[] {"foo","example","foolio"};
        //ArrayAdapter<String> luoghiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, DEFAULT);
            LuoghiAdapter luoghiAdapter = new LuoghiAdapter(this,android.R.layout.simple_spinner_dropdown_item);
           // luoghiAdapter.setNotifyOnChange(true);

        multiAutoCompleteTextView.setAdapter(luoghiAdapter);
        //luoghiAdapter.notifyDataSetChanged();

        multiAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() >= 3) {
                    service.fetchLocationAsync(s.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    result -> {
                                        Location[] response = result;
                                        runOnUiThread(() -> luoghiAdapter.setList(response));
                                        Log.d("RxJava", "Risultato scritto: " + response[0].getName() + "...");
                                        multiAutoCompleteTextView.invalidate();
                                    },
                                    error -> {
                                        // Gestisci gli errori qui
                                        Log.e("RxJava", "Errore: " + error.getMessage());
                                    }
                            );
                }
                //multiAutoCompleteTextView.showDropDown();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // nothing to do
            }
        });

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCityCode = luoghiAdapter.codeForSelected(position);

                Log.d("LUOGO"," "+luoghiAdapter.getItemId(position));
                Log.d("LUOGO"," id "+ id);
                Log.d("LUOGO"," position "+position);
            }
        });

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
        submitHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.fetchHotelAsync(currentCityCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    for (Hotel h : result) {
                                        hotelResults.append(h.toString() + "\n\n");
                                    }
                                },
                                error -> {
                                    // Gestisci gli errori qui
                                    Log.e("RxJava", "Errore: " + error.getMessage());
                                }
                        );
            }
        });

    }
}
package it.unimib.buildyourholiday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

/**
 * Offers methods to HomeFragment, FlightResultsFragment, HotelResultsFragment
 */

public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = HomeFragmentViewModel.class.getSimpleName();
    private String originCityCode;
    private String originCity;
    private String destinationCityCode;
    private String destinationCity;
    private String destinationCountryCode;
    private PlacesAdapter originPlacesAdapter;
    private PlacesAdapter destinationPlacesAdapter;
    private double price;
    private String departDate, returnDate;
    private int adultsNumber;

    public boolean isDateValid(String date) {
        if (date.length() != 10)
            return false;

        return true;
    }

    public boolean isDateRangeValid(String firstDate,String secondDate) {
        int firstYear = Integer.parseInt(firstDate.substring(0,4));
        int secondYear = Integer.parseInt(secondDate.substring(0,4));
        if(firstYear > secondYear)
            return false;
        int firstMonth = Integer.parseInt(firstDate.substring(5,7));
        int secondMonth = Integer.parseInt(secondDate.substring(5,7));
        if(firstYear == secondYear && firstMonth > secondMonth)
            return false;
        int firstDay = Integer.parseInt(firstDate.substring(8));
        int secondDay = Integer.parseInt(secondDate.substring(8));
        if(firstYear == secondYear && firstMonth == secondMonth && firstDay > secondDay)
            return false;

        return true;
    }

    public void showDatePickerDialog(Context context, EditText dateEditText) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(year<calendar.get(Calendar.YEAR) || year==calendar.get(Calendar.YEAR) && month<calendar.get(Calendar.MONTH)
                        || year==calendar.get(Calendar.YEAR) && month==calendar.get(Calendar.MONTH) && dayOfMonth<day) {
                    Snackbar.make(view,context.getString(R.string.date_picker_err),Snackbar.LENGTH_SHORT).show();
                } else {
                    String selectedDate = year + "-";

                    month += 1;
                    if (month<10)   selectedDate += "0"+month+"-";
                    else            selectedDate += month+"-";

                    if(dayOfMonth<10)   selectedDate += "0"+dayOfMonth;
                    else                selectedDate += dayOfMonth;
                    dateEditText.setText(selectedDate);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public boolean retrieveSearchParameters(Context context, View view,
                                            String departDate,String returnDate,String adults,String budget,String origin,String destination) {
        if(departDate.isEmpty() || !isDateValid(departDate)) {
            Snackbar.make(view, context.getString(R.string.depart_date_err), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            this.departDate = departDate;
        }
        if(!returnDate.isEmpty() && !isDateValid(returnDate)) {
            Snackbar.make(view, context.getString(R.string.return_date_err), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            if(returnDate.isEmpty()) {
                returnDate = null;
            } else {
                if(!isDateRangeValid(departDate,returnDate)) {
                    Snackbar.make(view, context.getString(R.string.date_range_err), Snackbar.LENGTH_SHORT).show();
                    return false;
                } else {
                    this.returnDate = returnDate;
                }
            }
        }

        if(adults.isEmpty()) {
            Snackbar.make(view, context.getString(R.string.people_err), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        adultsNumber = Integer.parseInt(adults);
        if(adultsNumber <= 0 || adultsNumber>9) {
            Snackbar.make(view, context.getString(R.string.people_number_err), Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if(budget.isEmpty()) {
            price = -1;
        } else {
            price = Double.parseDouble(budget);
            if(price <= 0.0) {
                Snackbar.make(view, context.getString(R.string.budget_err), Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }


        if(origin.isEmpty()) {
            Snackbar.make(view, context.getString(R.string.origin_city_err), Snackbar.LENGTH_SHORT).show();
            return false;
        } else
            // activates in case autocomplete doesn't show up
            if(originCityCode == null || originCityCode.length() != 3) {
                originCityCode = originPlacesAdapter.codeForSelected(0);
                originCity = originPlacesAdapter.nameForSelected(0);
            }

        if(destination.isEmpty()) {
            Snackbar.make(view, context.getString(R.string.destination_city_err), Snackbar.LENGTH_SHORT).show();
            return false;
        } else
            // activates in case autocomplete doesn't show up
            if(destinationCityCode == null || destinationCityCode.length() != 3) {
                destinationCityCode = destinationPlacesAdapter.codeForSelected(0);
                Log.d(TAG,destinationCityCode);
                destinationCity = destinationPlacesAdapter.nameForSelected(0);
                Log.d(TAG,destinationCity);
                destinationCountryCode = destinationPlacesAdapter.countryForSelected(0);
                Log.d(TAG,destinationCountryCode);
            }

        return true;
    }

    public void setupAutoCompleteTextViews(AutoCompleteTextView originAutoCompleteTextView, AutoCompleteTextView destinationAutoCompleteTextView, Context context, Activity activity) {
        // dropdown results for origin city
        originPlacesAdapter = new PlacesAdapter(context,android.R.layout.simple_spinner_dropdown_item);
        AmadeusRepository.setupBarSearch(activity, originAutoCompleteTextView, originPlacesAdapter);

        originAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                originCityCode = originPlacesAdapter.codeForSelected(position);
                originCity = originPlacesAdapter.nameForSelected(position);
            }
        });

        // dropdown results for destination city
        destinationPlacesAdapter = new PlacesAdapter(context,android.R.layout.simple_spinner_dropdown_item);
        AmadeusRepository.setupBarSearch(activity, destinationAutoCompleteTextView, destinationPlacesAdapter);

        destinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationCityCode = destinationPlacesAdapter.codeForSelected(position);
                Log.d(TAG,"destination code: "+destinationCityCode);
                destinationCity = destinationPlacesAdapter.nameForSelected(position);
                Log.d(TAG,"destination city: "+destinationCity);
                destinationCountryCode = destinationPlacesAdapter.countryForSelected(position);
                Log.d(TAG,"destination country: "+destinationCountryCode);
            }
        });
    }

    // called from HomeFragment -> FlightResultsFragment
    public void searchFlights(FragmentActivity activity, NavController navController) {
        FlightListViewModel flightListViewModel = new ViewModelProvider(activity).get(FlightListViewModel.class);
        AmadeusRepository.flightSearch(originCityCode, destinationCityCode, departDate, returnDate, adultsNumber, price, flightListViewModel);

        AmadeusRepository.holdParameters(destinationCityCode, destinationCity, adultsNumber, returnDate);

        // pass to execute SearchResultsFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("price",price);
        bundle.putString("destinationCountry",destinationCountryCode);
        //launchFragmentFlightResults(price);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.flightResultsFragment,true).build();

        navController.navigate(R.id.flightResultsFragment, bundle);
    }
}

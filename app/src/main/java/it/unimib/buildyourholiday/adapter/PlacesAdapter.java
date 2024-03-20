package it.unimib.buildyourholiday.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amadeus.resources.Location;

public class PlacesAdapter extends ArrayAdapter<String> {

    private Location[] buffer;
    private static final String[] DEFAULT = new String[] {"foo","foolio","example","Rome"};
    public PlacesAdapter(@NonNull Context context, int resource) {
        super(context, resource, DEFAULT);
        notifyDataSetChanged();
    }

    public PlacesAdapter(@NonNull Context context, int resource, String[] stuff) {
        super(context, resource, stuff);
    }

    public void setList(Location[] locations) {
        super.clear();
        this.buffer = locations;
        for (Location l: locations) {
            super.add(l.getName());
        }
        notifyDataSetChanged();
    }

    public String codeForSelected(int position) {
        return buffer[position].getIataCode();
    }

    public String nameForSelected(int position) {
        return buffer[position].getName();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public String countryForSelected(int position) {
        return buffer[position].getAddress().getCountryCode();
    }
}

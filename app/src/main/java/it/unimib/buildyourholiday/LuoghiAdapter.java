package it.unimib.buildyourholiday;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amadeus.resources.Location;

import java.util.ArrayList;
import java.util.List;

public class LuoghiAdapter extends ArrayAdapter<String> {

    private Location[] buffer;
    private static final String[] DEFAULT = new String[] {"foo","foolio","example","Rome"};
    public LuoghiAdapter(@NonNull Context context, int resource) {
        super(context, resource,DEFAULT);
        notifyDataSetChanged();
    }

    public LuoghiAdapter(@NonNull Context context, int resource, String[] stuff) {
        super(context, resource, stuff);
    }

    public void setList(Location[] locations) {
        this.buffer = locations;
        super.clear();
        for (Location l: locations) {
            super.add(l.getName());
        }
        notifyDataSetChanged();
    }

    public String codeForSelected(int position) {
        return buffer[position].getIataCode();
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
}

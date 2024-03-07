package it.unimib.buildyourholiday.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {

    public interface OnItemClickListener{
        void onFlightItemClick(Flight flight);
    }
    private final List<Flight> flightList;
    private List<String> durations = null;
    private List<Boolean> directFlight = null;
    private FlightListAdapter.OnItemClickListener onItemClickListener;
    private int selectedItemPosition = -1;

    public FlightListAdapter(List<Flight> flightList, FlightListAdapter.OnItemClickListener onItemClickListener) {
        this.flightList = flightList;
        this.onItemClickListener = onItemClickListener;
    }

    public FlightListAdapter(List<Flight> flightList, List<String> durations, List<Boolean> directFlight, FlightListAdapter.OnItemClickListener onItemClickListener) {
        this.flightList = flightList;
        this.durations = durations;
        this.directFlight = directFlight;
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FlightListAdapter.FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_offers_parent_item, parent, false);

        return new FlightListAdapter.FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightListAdapter.FlightViewHolder holder, int position) {
        if(durations == null || directFlight == null)
            holder.bind(flightList.get(position), position);
        else
            holder.bind(flightList.get(position), position, durations.get(position), directFlight.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldPosition = selectedItemPosition;
                int position = holder.getAdapterPosition();
                Log.d("RadioButton","click detected on position: "+position);
                Flight flight = flightList.get(position);
                onItemClickListener.onFlightItemClick(flight);
                //notifyItemChanged(position);

                if(position != RecyclerView.NO_POSITION) {
                    Log.d("RadioButton","new position: "+position);
                    selectedItemPosition = position;
                    notifyItemChanged(position);
                    notifyItemChanged(oldPosition);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (flightList != null) {
            return flightList.size();
        }
        return 0;
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {
        private final TextView flightCodeTextView;
        private final TextView departureDate,returnalDate;
        private final TextView price;
        private final TextView origin, destination;
        private final RadioButton radioButton;
        private final ConstraintLayout constraintLayout;
        private final TextView duration;
        private final CheckBox direct;
        private final Button expand;
        private final RelativeLayout expandableLayout;
        private boolean expandable = false;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightCodeTextView = itemView.findViewById(R.id.textview_hotel);
            departureDate = itemView.findViewById(R.id.textview_checkinDate);
            returnalDate = itemView.findViewById(R.id.textview_checkoutDate);
            price = itemView.findViewById(R.id.textview_price);
            origin = itemView.findViewById(R.id.textview_city);
            destination = itemView.findViewById(R.id.textview_destination);
            radioButton = itemView.findViewById(R.id.radioButton);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);

            duration = itemView.findViewById(R.id.textview_duration);
            direct = itemView.findViewById(R.id.checkbox_directFlight);
            expand = itemView.findViewById(R.id.button_expand);
              expand.setVisibility(View.GONE);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setVisibility(View.GONE);
        }

        public void bind(Flight flight, int position) {
            if(flight.getCode() != null)
                flightCodeTextView.setText(flight.getCode());
            else
                flightCodeTextView.setText("errorCode");
            if(flight.getDepartureDate() != null)
                departureDate.setText(flight.getDepartureDate());
            else
                departureDate.setText("errorDate");
            if(flight.getReturnalDate() != null)
                returnalDate.setText(flight.getReturnalDate());
            else
                returnalDate.setText("");
            if(flight.getPrice() > 0)
                price.setText("€" + String.valueOf(flight.getPrice()));
            else
                price.setText("errorPrice");
            if(flight.getDepartureAirport() != null)
                origin.setText(flight.getDepartureAirport());
            else
                origin.setText("errorCity");
            if(flight.getArrivalAirport() != null)
                destination.setText(flight.getArrivalAirport());
            else
                destination.setText("errorCity");

            radioButton.setClickable(false);
            Log.d("RadioButton","position: "+position+"; selected item: "+selectedItemPosition);
            radioButton.setChecked(position == selectedItemPosition);
            Log.d("RadioButton","radio checked: "+radioButton.isChecked());
        }

        public void bind(Flight flight, int position, String duration, Boolean directFlight) {
            bind(flight, position);

            expand.setVisibility(View.VISIBLE);
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RadioButton", "Detected touch on expand element");
                    expandable = !expandable;

                    if(expandable)
                        expandableLayout.setVisibility(View.VISIBLE);
                    else
                        expandableLayout.setVisibility(View.GONE);
                }
            });

            if(duration != null)
                this.duration.setText(duration);
            else
                this.duration.setText("errorDuration");

            if(directFlight)
                this.direct.setChecked(true);
            else
                this.direct.setChecked(false);
        }
    }
}

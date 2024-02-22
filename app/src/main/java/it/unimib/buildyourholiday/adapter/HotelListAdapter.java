package it.unimib.buildyourholiday.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.HotelViewHolder> {

    public interface OnItemClickListener {

        void onHotelItemClick(Hotel hotel);
    }

    private final List<Hotel> hotelList;
    private List<String> hotelsDescription = null;
    private List<String> hotelLinks = null;
    private final HotelListAdapter.OnItemClickListener onItemClickListener;
    private int selectedItemPosition = -1;
    private Context context = null;

    public HotelListAdapter(List<Hotel> hotelList, OnItemClickListener onItemClickListener) {
        this.hotelList = hotelList;
        this.onItemClickListener = onItemClickListener;
    }

    public HotelListAdapter(List<Hotel> hotelList, List<String> hotelsDescription, List<String> hotelLinks, OnItemClickListener onItemClickListener, Context context) {
        this.hotelList = hotelList;
        this.hotelsDescription = hotelsDescription;
        this.hotelLinks = hotelLinks;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelListAdapter.HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_offers_parent_item, parent, false);

        return new HotelListAdapter.HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelListAdapter.HotelViewHolder holder, int position) {
        if(hotelsDescription == null || hotelLinks == null)
            holder.bind(hotelList.get(position), position);
        else
            holder.bind(hotelList.get(position), position, hotelsDescription.get(position), hotelLinks.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldPosition = selectedItemPosition;
                int position = holder.getAdapterPosition();
                Log.d("RadioButton", "click detected on position: " + position);
                Hotel hotel = hotelList.get(position);
                //notifyItemChanged(position);

                if (position != RecyclerView.NO_POSITION) {
                    Log.d("RadioButton", "new position: " + position);
                    selectedItemPosition = position;
                    notifyItemChanged(position);
                    notifyItemChanged(oldPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hotelList != null) {
            return hotelList.size();
        }
        return 0;
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        private final TextView hotelNameTextView;
        private final TextView adults;
        private final TextView checkinDate, checkoutDate;
        private final TextView price;
        private final TextView city;
        private final RadioButton radioButton;
        private final TextView description;
        private final Button expand, viewOnMap;
        private final RelativeLayout expandableLayout;
        private boolean expandable = false;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.textview_hotel);
            adults = itemView.findViewById(R.id.textview_adults);
            checkinDate = itemView.findViewById(R.id.textview_checkinDate);
            checkoutDate = itemView.findViewById(R.id.textview_checkoutDate);
            price = itemView.findViewById(R.id.textview_price);
            city = itemView.findViewById(R.id.textview_city);
            description = itemView.findViewById(R.id.textview_description);
            radioButton = itemView.findViewById(R.id.radioButton);
            expand = itemView.findViewById(R.id.button_expand);
            viewOnMap = itemView.findViewById(R.id.button_viewOnMap);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
        }

        public void bind(Hotel hotel, int position) {
            if (hotel.getHotel() != null)
                hotelNameTextView.setText(hotel.getHotel());
            else
                hotelNameTextView.setText("errorName");
            if (hotel.getAdults() > 0)
                adults.setText(""+hotel.getAdults());
            else
                adults.setText("errorAdults");
            if (hotel.getHotelCity() != null)
                city.setText(hotel.getHotelCity());
            else
                city.setText("errorCity");
            if (hotel.getCheckinDate() != null)
                checkinDate.setText(hotel.getCheckinDate());
            else
                checkinDate.setText("");
            if (hotel.getCheckoutDate() != null)
                checkoutDate.setText(hotel.getCheckoutDate());
            else
                checkoutDate.setText("");
            if (hotel.getTotal() > 0)
                price.setText("€" + String.valueOf(hotel.getTotal()));
            else
                price.setText("errorPrice");

            expand.setVisibility(View.GONE);
            expandableLayout.setVisibility(View.GONE);
            radioButton.setClickable(false);
            Log.d("RadioButton", "position: " + position + "; selected item: " + selectedItemPosition);
            radioButton.setChecked(position == selectedItemPosition);
            Log.d("RadioButton", "radio checked: " + radioButton.isChecked());
        }

        public void bind(Hotel hotel, int position, String description, String link) {
            bind(hotel, position);

            expand.setVisibility(View.VISIBLE);
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandable = !expandable;
                    notifyItemChanged(position);
                }
            });

            if(expandable)
                expandableLayout.setVisibility(View.VISIBLE);
            else
                expandableLayout.setVisibility(View.GONE);

            if(description != null)
                this.description.setText(description);
            else
                this.description.setText("");

            if(link != null)
                this.viewOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: gestire link a mappa esterna
                        Uri gmmIntentUri = Uri.parse(link);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mapIntent.setPackage("com.google.android.apps.maps"); // Specifica l'applicazione di Google Maps
                        if (context != null && mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(mapIntent);
                        } else {
                            // Se l'applicazione di Google Maps non è installata, puoi gestire l'apertura della mappa in un browser web
                            Uri webpage = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + link.substring(4));
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(webIntent);
                            } else {
                                viewOnMap.setVisibility(View.GONE);
                            }
                        }
                    }
                });
        }
    }
}
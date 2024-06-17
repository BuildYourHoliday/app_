package it.unimib.buildyourholiday.adapter;



import android.animation.LayoutTransition;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.ui.main.FragmentCommunication;

public class TravelSavedAdapter extends RecyclerView.Adapter<TravelSavedAdapter.TravelViewHolder> {

    public interface OnItemClickListener{
        void onDeleteButtonPressed(int position);
        void onTravelItemClick(Travel travel);
        void onSearchButtonPressed(String destination, String departure, String startDate, String endDate, String adults);
    }
    private final List<Travel> travelList;
    private final OnItemClickListener onItemClickListener;




/**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */


    /*public TravelListAdapter(List<Travel> travelList, OnItemClickListener onItemClickListener) {
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;

    }*/

    public TravelSavedAdapter(List<Travel> travelList, OnItemClickListener onItemClickListener) {
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TravelViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_travels_item, viewGroup, false);

        return new TravelViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {

        holder.bind(travelList.get(position));

        /*Travel travel = travelList.get(position);
        if(travel.getCity() != null)
            holder.textViewDestination.setText(travel.getCity());
        else
            holder.textViewDestination.setText("errorCity");
        if(travel.getBeginDate() != null)
            holder.textViewDate.setText(travel.getBeginDate());
        else
            holder.textViewDate.setText("errorDate");
        if(travel.getTotalPrice() != 0)
            holder.textViewPrice.setText(String.valueOf(travel.getTotalPrice()));
        else
            holder.textViewPrice.setText(String.valueOf(100.00));
        if(travel.getFinishDate() != null)
            holder.textViewEndDate.setText(travel.getFinishDate());
        else
            holder.textViewEndDate.setText("errorEndDate");


        boolean isExpandable = travelList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable? View.VISIBLE : View.GONE);*/



        /*holder.travelExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }

        }*/
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (travelList != null) {
            return travelList.size();
        }
        return 0;
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewDestination, textViewDeparture, textViewSeparation;
        private final TextView textViewDate, textViewPrice, textViewEndDate, textViewAdults, textViewNights;
        private final Button buttonDelete;

        private final ImageButton buttonSearch;
        //buttonSearch
        private final ConstraintLayout constraintLayout, expandableLayout;
        //private final RelativeLayout expandableLayout;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDestination = itemView.findViewById(R.id.textview_destination);
            textViewDate = itemView.findViewById(R.id.textview_checkinDate);
            textViewPrice = itemView.findViewById(R.id.textview_price);
            textViewEndDate = itemView.findViewById(R.id.textview_checkoutDate);
            textViewDeparture = itemView.findViewById(R.id.textview_departure);
            //textViewDepartureTime = itemView.findViewById(R.id.textView_departureTime);
            textViewSeparation = itemView.findViewById(R.id.textview_separation);
            textViewNights = itemView.findViewById(R.id.textview_nights);
            //toDo
            textViewAdults = itemView.findViewById(R.id.textview_adults);

            buttonDelete = itemView.findViewById(R.id.button_delete);
            buttonSearch = itemView.findViewById(R.id.button_search);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            constraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            itemView.setOnClickListener(this);
            buttonDelete.setOnClickListener(this);
            buttonSearch.setOnClickListener(this);

            constraintLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Travel travel = travelList.get(getAdapterPosition());
                    travel.setExpandable(!travel.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            //da aggiungere se si mette un img nelle informazioni viaggio

//            imageViewSavedTravel = itemView.findViewById(R.id.imageview_saved_travel);
            //itemView.setOnClickListener(this);
            //imageViewFavoriteNews.setOnClickListener(this);
        }


        public void bind(Travel travel) {
            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            }

            // Parse the strings into LocalDate objects
            LocalDate date1, date2;
            long nights;
            date1 = null;
            date2 = null;
            nights = 0;

            //check null date for saved
            if(travel.getFinishDate() != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date1 = LocalDate.parse(travel.getFinishDate(), formatter);
                }
            if(travel.getBeginDate() != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date2 = LocalDate.parse(travel.getBeginDate(), formatter);
                }

            // Calculate the difference in days
            if(date1 != null && date2 != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    nights = Math.abs(ChronoUnit.DAYS.between(date1, date2));
                }

            if(travel.getCity() != null)
                textViewDestination.setText(travel.getCity());
            else
                textViewDestination.setText("errorCity");

            if(travel.getFlight().getDepartureAirport() != null)
                textViewDeparture.setText(travel.getFlight().getDepartureAirport());
            else
                textViewDeparture.setText("errorCity");
            if(travel.getBeginDate() != null)
                textViewDate.setText(travel.getBeginDate());
            else
                textViewDate.setText("errorDate");
            if(travel.getTotalPrice() != 0)
                textViewPrice.setText(String.valueOf(travel.getTotalPrice()));
            else
                textViewPrice.setText(String.valueOf(100.00));
            if(travel.getFinishDate() != null)
                textViewEndDate.setText(travel.getFinishDate());
            else
                textViewEndDate.setText("errorEndDate");

            if(travel.getBeginDate() != null && travel.getFinishDate() != null)
                textViewNights.setText(String.valueOf(nights));
            else
                textViewNights.setText(String.valueOf(0));
            /*if(travel.getHotel().getAdults() != 0)
                textViewEndDate.setText(travel.getHotel().getAdults());
            else
                textViewEndDate.setText("errorAdults");*/

            textViewSeparation.setText("-");

            boolean isExpandable = travel.isExpandable();
            expandableLayout.setVisibility(isExpandable? View.VISIBLE : View.GONE);

            //textViewSeparation.setText('-');
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_delete) {
                int position = getAdapterPosition();
                Log.d("SavedFragment","adapter size: "+ travelList.size());
                Log.d("SavedFragment","adapter: " + travelList.get(position).getFlight().getDepartureAirport());
                for (int i=0;i<travelList.size();i++) {
                    Log.d("SavedFragment","(adapter) travel "+i+": "+travelList.get(i).getCity());
                }
                onItemClickListener.onDeleteButtonPressed(position);
                travelList.remove(position);
                notifyItemRemoved(position);
            } else if (v.getId() == R.id.button_search) {
                int position = getAdapterPosition();
                Travel travel = travelList.get(position);
                String destination = textViewDestination.getText().toString();
                String departure = textViewDeparture.getText().toString();
                String startDate = textViewDate.getText().toString();
                String endDate = textViewEndDate.getText().toString();
                String adults = textViewAdults.getText().toString();
                onItemClickListener.onSearchButtonPressed(destination, departure, startDate, endDate, adults);

                if (v.getContext() instanceof FragmentCommunication) {
                    ((FragmentCommunication) v.getContext()).onSearchButtonPressed(destination, departure, startDate, endDate, adults);
                }
            } else{
                Log.d("SavedFragment","position: "+getAdapterPosition());
                Travel travel = travelList.get(getAdapterPosition());
                travel.setExpandable(!travel.isExpandable());
                TransitionManager.beginDelayedTransition(constraintLayout, new AutoTransition());
                notifyItemChanged(getAdapterPosition());
            }
        }
    }
}



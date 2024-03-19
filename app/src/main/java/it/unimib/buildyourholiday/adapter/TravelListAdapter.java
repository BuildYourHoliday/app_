package it.unimib.buildyourholiday.adapter;



import android.animation.LayoutTransition;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.TravelViewHolder> {

    public interface OnItemClickListener{
        void onDeleteButtonPressed(int position);
        void onTravelItemClick(Travel travel);
    }
    private final List<Travel> travelList;
    private final OnItemClickListener onItemClickListener;
    private boolean optionDelete = true;




/**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */


    /*public TravelListAdapter(List<Travel> travelList, OnItemClickListener onItemClickListener) {
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;

    }*/

    public TravelListAdapter(List<Travel> travelList, OnItemClickListener onItemClickListener) {
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;
    }

    public TravelListAdapter(List<Travel> travelList, OnItemClickListener onItemClickListener, boolean optionDelete) {
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;
        this.optionDelete = optionDelete;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TravelViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.booked_travels_parent_item, viewGroup, false);

        return new TravelViewHolder(view,optionDelete);
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
        private final TextView textViewDate, textViewPrice, textViewEndDate, textViewDepartureTime, textViewAdults, textViewNights;
        private final ImageView bookedImage, savedImage;
        private final Button buttonDelete;
        private final ConstraintLayout constraintLayout, expandableLayout;
        //private final RelativeLayout expandableLayout;

        public TravelViewHolder(@NonNull View itemView, boolean optionDelete) {
            super(itemView);
            textViewDestination = itemView.findViewById(R.id.textview_destination);
            textViewDate = itemView.findViewById(R.id.textview_checkinDate);
            textViewPrice = itemView.findViewById(R.id.textview_price);
            textViewEndDate = itemView.findViewById(R.id.textview_checkoutDate);
            textViewDeparture = itemView.findViewById(R.id.textview_departure);
            textViewDepartureTime = itemView.findViewById(R.id.textView_departureTime);
            textViewSeparation = itemView.findViewById(R.id.textview_separation);
            textViewNights = itemView.findViewById(R.id.textview_nights);
            bookedImage = itemView.findViewById(R.id.booked_imageView);
            savedImage = itemView.findViewById(R.id.saved_imageView);
            //toDo
            textViewAdults = itemView.findViewById(R.id.textview_adults);

            buttonDelete = itemView.findViewById(R.id.button_delete);
            if(!optionDelete)
                buttonDelete.setVisibility(View.GONE);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            constraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            itemView.setOnClickListener(this);
            buttonDelete.setOnClickListener(this);

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
            //dovrebbe funzionare anche con API level 24 nel caso aggiorniamo il minimo
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Parse the strings into LocalDate objects
            LocalDate date1, date2;
            long nights;
            date1 = null;
            date2 = null;
            nights = 0;

            // set icon either on booked or saved
            if(travel.isBooked() && bookedImage != null)
                bookedImage.setVisibility(View.VISIBLE);
            else if(savedImage != null)
                savedImage.setVisibility(View.VISIBLE);

            //check null date for saved
            if(travel.getFinishDate() != null)
                date1 = LocalDate.parse(travel.getFinishDate(), formatter);
            if(travel.getBeginDate() != null)
                date2 = LocalDate.parse(travel.getBeginDate(), formatter);

            // Calculate the difference in days
            if(date1 != null && date2 != null)
                nights = Math.abs(ChronoUnit.DAYS.between(date1, date2));

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
            if(travel.getFlight().getDepartureTime() != null)
                textViewDepartureTime.setText(travel.getFlight().getDepartureTime());
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
                travelList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                onItemClickListener.onDeleteButtonPressed(getAdapterPosition());
            }
            else{
                Travel travel = travelList.get(getAdapterPosition());
                travel.setExpandable(!travel.isExpandable());
                TransitionManager.beginDelayedTransition(constraintLayout, new AutoTransition());
                notifyItemChanged(getAdapterPosition());
            }
        }
    }
}



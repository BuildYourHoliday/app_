package it.unimib.buildyourholiday.data;



import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.model.Travel;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.TravelViewHolder> {

    private List<Travel> travelList;


/**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */


    public TravelListAdapter(List<Travel> travelList) {
        this.travelList = travelList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public TravelViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.booked_travels_parent_item, viewGroup, false);

        return new TravelViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {

        Travel travel = travelList.get(position);
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
        holder.expandableLayout.setVisibility(isExpandable? View.VISIBLE : View.GONE);



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

    public class TravelViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewDestination;
        private final TextView textViewDate, textViewPrice, textViewEndDate;
        private final Button travelExpandButton;

        private final ConstraintLayout constraintLayout;
        private final RelativeLayout expandableLayout;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDestination = itemView.findViewById(R.id.textview_destination);
            textViewDate = itemView.findViewById(R.id.textview_date);
            textViewPrice = itemView.findViewById(R.id.textview_price);
            textViewEndDate = itemView.findViewById(R.id.textview_endDate);

            travelExpandButton = itemView.findViewById(R.id.button_expand);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

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


        /*public void bind(Travel travel) {
            textViewDestination.setText(travel.getFlight().getArrivalAirport());
            textViewDate.setText(DateTimeUtil.getDate(travel.getBeginDate()));
            boolean expanded = travel.isExpanded();
            // Set the visibility based on state
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            title.setText(movie.getTitle());
            genre.setText("Genre: " + movie.getGenre());
            year.setText("Year: " + movie.getYear());
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_expand) {
                onItemClickListener.onExpandButtonPressed(getAdapterPosition());
            }
        }*/
    }
}



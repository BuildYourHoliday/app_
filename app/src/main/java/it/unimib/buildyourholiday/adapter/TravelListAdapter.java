package it.unimib.buildyourholiday.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
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

        private final TextView textViewDestination;
        private final TextView textViewDate, textViewPrice, textViewEndDate;
        private final Button buttonDelete;
        private final ConstraintLayout constraintLayout;
        private final RelativeLayout expandableLayout;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDestination = itemView.findViewById(R.id.textview_destination);
            textViewDate = itemView.findViewById(R.id.textview_checkinDate);
            textViewPrice = itemView.findViewById(R.id.textview_price);
            textViewEndDate = itemView.findViewById(R.id.textview_checkoutDate);

            buttonDelete = itemView.findViewById(R.id.button_delete);

            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

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
            if(travel.getCity() != null)
                textViewDestination.setText(travel.getCity());
            else
                textViewDestination.setText("errorCity");
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

            boolean isExpandable = travel.isExpandable();
            expandableLayout.setVisibility(isExpandable? View.VISIBLE : View.GONE);
        }

        // Expand(better) e Delete da implementare
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
                notifyItemChanged(getAdapterPosition());
            }
        }
    }
}



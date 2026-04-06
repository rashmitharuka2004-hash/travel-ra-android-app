package com.jiat.travelraapp.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.jiat.travelraapp.R;
import com.jiat.travelraapp.model.Destination;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private List<Destination> destinations;

    public DestinationAdapter(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.title.setText(destination.getTitle());
        holder.description.setText(destination.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(destination.getImageUrl())
                .placeholder(R.drawable.logo_travel_ra)
                .error(R.drawable.logo_travel_ra)
                .into(holder.destinationImage);
    }

    // Fixed: variable name changed to 'destinations' to match the class variable
    public void filterList(List<Destination> filteredList) {
        this.destinations = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return destinations != null ? destinations.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView destinationImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvDestinationTitle);
            description = itemView.findViewById(R.id.tvDestinationDesc);
            destinationImage = itemView.findViewById(R.id.ivDestination);
        }
    }
}

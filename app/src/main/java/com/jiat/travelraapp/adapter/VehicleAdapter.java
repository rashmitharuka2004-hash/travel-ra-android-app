package com.jiat.travelraapp.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.jiat.travelraapp.R;
import com.jiat.travelraapp.model.Vehicle;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {
    private List<Vehicle> vehicles;
    private int selectedPosition = -1; // Keep track of selection
    private OnVehicleSelectedListener listener;

    public interface OnVehicleSelectedListener {
        void onVehicleSelected(Vehicle vehicle);
    }

    public VehicleAdapter(List<Vehicle> vehicles, OnVehicleSelectedListener listener) {
        this.vehicles = vehicles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create this layout based on Step 2 XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.name.setText(vehicle.getName());
        holder.pax.setText(vehicle.getCapacity() + " passengers");

        // Glide loads image from Travel Ra website URL
        Glide.with(holder.itemView.getContext()).load(vehicle.getImageUrl()).into(holder.image);

        // Selection Visual Logic matching wireframe border
        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged(); // Redraw with new selection border
            listener.onVehicleSelected(vehicle);
        });
    }

    @Override
    public int getItemCount() { return vehicles.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, pax;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            // Link these to IDs in item_vehicle_select.xml
            name = itemView.findViewById(R.id.tvVehicleName);
            pax = itemView.findViewById(R.id.tvPaxCount);
            image = itemView.findViewById(R.id.ivVehicleImage);
        }
    }
}

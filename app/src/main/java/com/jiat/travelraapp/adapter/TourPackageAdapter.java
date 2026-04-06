package com.jiat.travelraapp.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.jiat.travelraapp.R;
import com.jiat.travelraapp.TourDetailActivity;
import com.jiat.travelraapp.model.TourPackage;
import java.util.List;

public class TourPackageAdapter extends RecyclerView.Adapter<TourPackageAdapter.ViewHolder> {
        private List<TourPackage> tourList;

    public TourPackageAdapter(List<TourPackage> tourList) {
            this.tourList = tourList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour_package, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TourPackage tour = tourList.get(position);
            holder.tvTitle.setText(tour.getTitle());
            holder.tvDuration.setText("Duration: " + tour.getDuration());
            holder.tvPrice.setText(tour.getPrice());

            Glide.with(holder.itemView.getContext()).load(tour.getImageUrl()).into(holder.ivPhoto);

            // Redirect to Detail Screen
            holder.itemView.findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), TourDetailActivity.class);
                intent.putExtra("TOUR_DATA", tour);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return tourList.size(); }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDuration, tvPrice;
            ImageView ivPhoto;
            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTourTitle);
                tvDuration = itemView.findViewById(R.id.tvTourDuration);
                tvPrice = itemView.findViewById(R.id.tvTourPrice);
                ivPhoto = itemView.findViewById(R.id.ivTourPhoto);
            }
        }
    public void filterList(List<TourPackage> filteredList) {
        this.tourList = filteredList;
        notifyDataSetChanged();
    }

}

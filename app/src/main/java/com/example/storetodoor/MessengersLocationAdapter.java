package com.example.storetodoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessengersLocationAdapter extends RecyclerView.Adapter<MessengersLocationAdapter.MessengersViewHolder> {
    private List<MessengerPOJO> messengerPOJOList;
    private Context context;

    public MessengersLocationAdapter(Context context, List<MessengerPOJO> messengerPOJOList) {
        this.context = context;
        this.messengerPOJOList = messengerPOJOList;
    }

    @NonNull
    @Override
    public MessengersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.messengers_location_list_item_layout, parent, false);
        return new MessengersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessengersViewHolder holder, int position) {
        holder.messengerName.setText(messengerPOJOList.get(position).getName());
        holder.locationAddress.setText(messengerPOJOList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return messengerPOJOList.size();
    }

    public class MessengersViewHolder extends RecyclerView.ViewHolder {
        TextView messengerName, locationAddress;

        public MessengersViewHolder(@NonNull View itemView) {
            super(itemView);

            messengerName = itemView.findViewById(R.id.messengerName);
            locationAddress = itemView.findViewById(R.id.locationAddress);
        }
    }
}

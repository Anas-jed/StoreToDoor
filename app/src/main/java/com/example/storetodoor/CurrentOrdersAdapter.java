package com.example.storetodoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrentOrdersAdapter extends RecyclerView.Adapter<CurrentOrdersAdapter.CurrentOrderViewHolder> {

    private List<CurrentOrdersPOJO> currentOrdersPOJOList;
    private Context context;

    public CurrentOrdersAdapter(List<CurrentOrdersPOJO> currentOrdersPOJOList, Context context) {
        this.currentOrdersPOJOList = currentOrdersPOJOList;
        this.context = context;
    }


    @NonNull
    @Override
    public CurrentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.current_orders_list_item_layout, parent, false);
        return new CurrentOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentOrderViewHolder holder, int position) {

        holder.customername.setText(currentOrdersPOJOList.get(position).getC_name());
        holder.customeraddress.setText(currentOrdersPOJOList.get(position).getComplete_address());
        holder.productname.setText(currentOrdersPOJOList.get(position).getP_name());
        holder.productquantity.setText(currentOrdersPOJOList.get(position).getQuantity());
        holder.status.setText(currentOrdersPOJOList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return currentOrdersPOJOList.size();
    }


    public class CurrentOrderViewHolder extends RecyclerView.ViewHolder {
        TextView customername,customeraddress,productname,productquantity,status;
        public CurrentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customername = itemView.findViewById(R.id.customer_name);
            customeraddress = itemView.findViewById(R.id.customer_address);
            productname = itemView.findViewById(R.id.product_name);
            productquantity = itemView.findViewById(R.id.quantity);
            status = itemView.findViewById(R.id.status);

        }
    }
}

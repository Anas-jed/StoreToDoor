package com.example.storetodoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<CurrentOrdersPOJO> currentOrdersPOJOList;
    private Context context;

    public OrderHistoryAdapter(List<CurrentOrdersPOJO> currentOrdersPOJOList, Context context) {
        this.currentOrdersPOJOList = currentOrdersPOJOList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_history_list_item_layout, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {

        holder.customername.setText(currentOrdersPOJOList.get(position).getC_name());
        Timestamp timestamp = currentOrdersPOJOList.get(position).getO_date();

        Date date = timestamp.toDate();
        String tempString = date.toString();
        holder.orderdate.setText(tempString);
        holder.productname.setText(currentOrdersPOJOList.get(position).getP_name());
        holder.productquantity.setText(currentOrdersPOJOList.get(position).getQuantity());
        holder.status.setText(currentOrdersPOJOList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return currentOrdersPOJOList.size();
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView customername,orderdate,productname,productquantity,status;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            customername = itemView.findViewById(R.id.order_list_customer_name);
            orderdate = itemView.findViewById(R.id.order_list_order_date);
            productname = itemView.findViewById(R.id.order_list_product_name);
            productquantity = itemView.findViewById(R.id.order_list_quantity);
            status = itemView.findViewById(R.id.order_list_status);
        }
    }
}

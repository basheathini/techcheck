package com.ikhokha.techcheck.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ikhokha.techcheck.Models.User;
import com.ikhokha.techcheck.R;
import java.util.List;

public class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder> {
    private List<User> productList;
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, price, number;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.number);
            number = itemView.findViewById(R.id.price);
        }
    }
    public OneAdapter(List<User> productList, Context context){
        this.productList = productList;
    }
    @NonNull
    @Override
    public OneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new OneAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OneAdapter.ViewHolder holder, final int position) {
        User user = productList.get(position);
        holder.name.setText(user.getName());
        holder.number.setText(String.valueOf(user.getNumber()));
        holder.price.setText(String.valueOf(user.getPrice()));
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
}

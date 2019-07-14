package com.ikhokha.techcheck.Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ikhokha.techcheck.Models.Product;
import com.ikhokha.techcheck.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    Context context;
    Product product;

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView picture;
        private TextView name, price, number, date;
        ViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            number = itemView.findViewById(R.id.number);
            date = itemView.findViewById(R.id.date);
        }
    }
    public ProductAdapter(List<Product> productList, Context context){
        this.productList = productList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        product = productList.get(position);
        holder.name.setText(product.getDescription());
        holder.number.setText("Quantity : " +  product.getQuantity());
        holder.price.setText("R" + String.valueOf(product.getPrice()));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference storageReference1 = storageReference.child(product.getImage());
        final long ONE_MEGA = 1024 * 1024;

        storageReference1.getBytes(ONE_MEGA).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                holder.date.setText(product.getDate());
                holder.picture.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
}

package com.ikhokha.techcheck.Views;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ikhokha.techcheck.Controllers.OneAdapter;
import com.ikhokha.techcheck.Controllers.ProductAdapter;
import com.ikhokha.techcheck.Models.Product;
import com.ikhokha.techcheck.Models.User;
import com.ikhokha.techcheck.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cart extends Fragment {
    RecyclerView recyclerView;
    List<Product> productList;
    List<User> userList;
    CardView update, checker,checkout_button;
    ArrayList<String> description;
    TextView products_num, total, dates;
    ImageView code;
    double sum = 0.0;
    Map<String, Map<String, Object>> savedHash;
    Map<String, Object> values;
    int _quatity;
    public Cart() {
        userList = new ArrayList<>();
        productList = new ArrayList<>();
        description = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.items);
        products_num = view.findViewById(R.id.products_num);
        total = view.findViewById(R.id.total);
        update = view.findViewById(R.id.update);
        checker = view.findViewById(R.id.checker);
        checkout_button = view.findViewById(R.id.checkout_button);
        dates = view.findViewById(R.id.date);
        savedHash = Helper.getHash("orders", getActivity());
        if (savedHash != null){
            products_num.setText("Products: " + String.valueOf(savedHash.size()));
            recyclerView.setVisibility(View.VISIBLE);
            checker.setVisibility(View.VISIBLE);
            for (Map.Entry<String,  Map<String, Object>> entry : savedHash.entrySet()) {
                values = entry.getValue();
                Double number = (double) values.get("quantity");
                _quatity = number.intValue();
                description.add((String) values.get("description"));
                productList.add(new Product((String) values.get("description"), (double) values.get("price"), (String) values.get("image"), _quatity, (String) values.get("image"), null));
                sum += (double) values.get("price");

            }
            total.setText("R" + String.valueOf(sum));

        }else {
            update.setVisibility(View.VISIBLE);
        }
        checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.clearPref("orders", getActivity());
                LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view   = layoutInflater.inflate(R.layout.receipt, (ViewGroup) v.findViewById(R.id.new_pop));
                TextView sub_total = view.findViewById(R.id.sub_total);
                CardView sign = view.findViewById(R.id.sign);
                RecyclerView recycle = view.findViewById(R.id.recycle);

                for (Map.Entry<String,  Map<String, Object>> entry : savedHash.entrySet()) {
                    values = entry.getValue();
                    Double number = (double) values.get("quantity");
                    _quatity = number.intValue();
                    userList.add(new User((String) values.get("description"), _quatity, (double) values.get("price")));

                }
                OneAdapter oneAdapter = new OneAdapter(userList, getActivity());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
                recycle.setLayoutManager(layoutManager);
                recycle.setAdapter(oneAdapter);
                oneAdapter.notifyDataSetChanged();
                code = view.findViewById(R.id.code);
                sub_total.setText("R" + String.valueOf(sum));
                sign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_TEXT, "Transaction details\n\n" + "Order Date\n" + Helper.getDate().substring(0,16) + "\n\n" + "Items ordered \n" + description.toString() + "\n\n" + "Total\n" + "R" + sum );
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(Intent.createChooser(share, "Share Article Via"));

                    }
                });
                String text= Helper.getDate() + "Order" + sum;
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    code.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                final PopupWindow popupWindow = new PopupWindow(view, DrawerLayout.LayoutParams.MATCH_PARENT,DrawerLayout.LayoutParams.MATCH_PARENT,true);
                popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

            }
        });
        ProductAdapter productAdapter = new ProductAdapter(productList, getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        return view;
    }
}

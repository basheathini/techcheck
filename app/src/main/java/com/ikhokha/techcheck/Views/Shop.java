package com.ikhokha.techcheck.Views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.ResultPoint;
import com.ikhokha.techcheck.Models.Product;
import com.ikhokha.techcheck.R;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends Fragment {
    DecoratedBarcodeView scannerView;
    boolean isScanDone;
    TextView tvCardText,scann;
    CardView scan;
    String code;
    Map<String, Map<String, Object>> productList;
    Product product;
    public Shop() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        productList = new HashMap<>();
        scan = view.findViewById(R.id.scanner);
        scannerView = view.findViewById(R.id.scanners);
        tvCardText = view.findViewById(R.id.tv_code_text);
        scann = view.findViewById(R.id.scann);
        requestPermission();
        scannerView.setStatusText("");
        scannerView.setStatusText("");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.decodeSingle(new BarcodeCallback() {
                    @Override
                    public void barcodeResult(BarcodeResult result) {
                        updateText(result.getText());
                        code = result.getText();
                        scan.setCardBackgroundColor(Color.GREEN);
                        scann.setText(R.string.reading);
                        new GetProduct().execute();
                    }
                    @Override
                    public void possibleResultPoints(List<ResultPoint> resultPoints) {

                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeScanner();
    }

    protected void resumeScanner() {
        isScanDone = false;
        if (!scannerView.isActivated())
            scannerView.resume();
    }


    protected void pauseScanner() {
        scannerView.pause();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseScanner();
    }
    void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length < 1) {
            requestPermission();
        } else {
            scannerView.resume();
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }

    public class GetProduct extends AsyncTask<Void, Void, Boolean> {
        GetProduct() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(final Void... params) {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child(code);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        product = dataSnapshot.getValue(Product.class);
                        assert product != null;
                        if (product != null){
                            Product values = new Product(product.getDescription(), product.getPrice(), product.getImage(), 1, Helper.getDate().substring(0,10), null);
                            Map<String, Object> postValues = values.toMap();

                            Map<String, Map<String, Object>> savedHash = Helper.getHash("orders", getActivity());
                            if (savedHash != null){
                                if (savedHash.containsKey(product.getDescription())){
                                    Map<String, Object> pro = savedHash.get(product.getDescription());
                                    pro.put("quantity", (double) pro.get("quantity") + 1);
                                    pro.put("price", (double) pro.get("price") * 2);
                                    savedHash.put(product.getDescription(), pro);
                                    Helper.saveHash("orders", savedHash, getActivity());
                                }else {
                                    savedHash.put(product.getDescription(), postValues);
                                    Helper.saveHash("orders", savedHash, getActivity());
                                }
                            }else {
                                productList.put(product.getDescription(), postValues);
                                Helper.saveHash("orders", productList, getActivity());
                            }
                            scann.setText(R.string.scan);
                            scan.setCardBackgroundColor(Color.parseColor("#fedd00"));
                        }else {
                            Toast.makeText(getContext(),"Cannot find the item.", Toast.LENGTH_SHORT).show();
                            scann.setText(R.string.scan);
                            scan.setCardBackgroundColor(Color.parseColor("#fedd00"));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
        }
        @Override
        protected void onCancelled() {
        }
    }
}

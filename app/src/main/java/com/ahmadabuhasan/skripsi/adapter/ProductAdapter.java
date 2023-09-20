package com.ahmadabuhasan.skripsi.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.data.EditProductActivity;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan on 02/02/2021
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<ProductModel> productData;

    public ProductAdapter(Context context1, List<ProductModel> productData1) {
        this.context = context1;
        this.productData = productData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String product_id = String.valueOf(this.productData.get(position).getId());

        //Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID); *include Rp

        Double buy = this.productData.get(position).getProduct_cost();
        Double price = this.productData.get(position).getProduct_price();

        String currency = "Ksh";

        holder.textView_ProductName.setText(this.productData.get(position).getProduct_name());
        TextView textView = holder.textView_Buy;
        textView.setText(this.context.getString(R.string.buy) + " : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(buy));
        TextView textView1 = holder.textView_Stock;
        textView1.setText(this.context.getString(R.string.stock) + " : " + this.productData.get(position).getProduct_quantity());
        TextView textView2 = holder.textView_Price;
        textView2.setText(this.context.getString(R.string.price) + " : " + currency + " " + NumberFormat.getInstance(Locale.getDefault()).format(price));

        String imageUrl = this.productData.get(position).getMedia();
        Picasso.get().load(imageUrl).into(holder.prodImage);
    }

    @Override
    public int getItemCount() {
        return this.productData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_ProductName;
        TextView textView_Buy;
        TextView textView_Stock;
        TextView textView_Price;
        ImageView imageView_Delete;
        ImageView prodImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView_ProductName = itemView.findViewById(R.id.tv_product_name);
            this.textView_Buy = itemView.findViewById(R.id.tv_buy);
            this.textView_Stock = itemView.findViewById(R.id.tv_stock);
            this.textView_Price = itemView.findViewById(R.id.tv_price);
            this.imageView_Delete = itemView.findViewById(R.id.img_delete);
            this.prodImage = itemView.findViewById(R.id.product_image);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
//            Intent i = new Intent(ProductAdapter.this.context, EditProductActivity.class);
//            i.putExtra(DatabaseOpenHelper.PRODUCT_ID, ProductAdapter.this.productData.get(getAdapterPosition()).getId());
//
//            ProductAdapter.this.context.startActivity(i);
        }
    }
}
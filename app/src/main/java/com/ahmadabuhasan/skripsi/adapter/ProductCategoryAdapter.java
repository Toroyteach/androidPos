package com.ahmadabuhasan.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.skripsi.R;
import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.database.DatabaseAccess;
import com.ahmadabuhasan.skripsi.database.DatabaseOpenHelper;
import com.ahmadabuhasan.skripsi.settings.categories.EditCategoryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Created by Ahmad Abu Hasan on 22/01/2021
 */

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder> {

    private List<CategoryModel> categoryData;
    private Context context;
    ImageView imgNoProduct;
    MediaPlayer mediaPlayer;
    RecyclerView recyclerView;
    TextView textView_NoProducts;

    private final OnCategoryClickListener onCategoryClickListener;

    public ProductCategoryAdapter(Context context1, List<CategoryModel> categoryData1, RecyclerView recyclerView1, ImageView imgNoProduct1, TextView textView_NoProducts1, OnCategoryClickListener onCategoryClickListener) {
        this.context = context1;
        this.categoryData = categoryData1;
        this.recyclerView = recyclerView1;
        this.mediaPlayer = MediaPlayer.create(context1, (int) R.raw.delete_sound);
        this.imgNoProduct = imgNoProduct1;
        this.textView_NoProducts = textView_NoProducts1;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int category_id = this.categoryData.get(position).getId();
        holder.textView_CategoryName.setText(this.categoryData.get(position).getCategory_name());

        holder.cardCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProductCategoryAdapter.this.mediaPlayer.start();
//                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ProductCategoryAdapter.this.context);
//                databaseAccess.open();
//                List<HashMap<String, String>> productList = databaseAccess.getTabProducts(category_id);
//                if (productList.isEmpty()) {
//                    ProductCategoryAdapter.this.recyclerView.setVisibility(View.INVISIBLE);
//                    ProductCategoryAdapter.this.recyclerView.setVisibility(View.GONE);
//                    ProductCategoryAdapter.this.imgNoProduct.setVisibility(View.VISIBLE);
//                    ProductCategoryAdapter.this.imgNoProduct.setImageResource(R.drawable.not_found);
//                    ProductCategoryAdapter.this.textView_NoProducts.setVisibility(View.VISIBLE);
//                    return;
//                }
//                ProductCategoryAdapter.this.recyclerView.setVisibility(View.VISIBLE);
//                ProductCategoryAdapter.this.imgNoProduct.setVisibility(View.GONE);
//                ProductCategoryAdapter.this.textView_NoProducts.setVisibility(View.GONE);
//                ProductCategoryAdapter.this.recyclerView.setAdapter(new PosProductAdapter(ProductCategoryAdapter.this.context, productList));


//                List<ProductModel> filteredList = new ArrayList<>();
//
//                for (ProductModel item : list) {
//                    if (item.getCategory_id() == category_id) {
//                        filteredList.add(item);
//                    }
//                }
//
//                return filteredList;

                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onCategoryClick(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.categoryData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardCategory;
        TextView textView_CategoryName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardCategory = (CardView) itemView.findViewById(R.id.card_category);
            this.textView_CategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent i = new Intent(ProductCategoryAdapter.this.context, EditCategoryActivity.class);
            i.putExtra("category_id", ProductCategoryAdapter.this.categoryData.get(getAdapterPosition()).getId());
            i.putExtra("category_name", ProductCategoryAdapter.this.categoryData.get(getAdapterPosition()).getCategory_name());
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }
}
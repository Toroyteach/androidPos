package com.ahmadabuhasan.skripsi.data;

import com.ahmadabuhasan.skripsi.connection.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchFunction {
    public interface SearchListener {
        void onSearch(List<ProductModel> resultList);
    }

    public static void search(List<ProductModel> itemList, String searchText, SearchListener listener) {
        List<ProductModel> filteredItemList = new ArrayList<>();

        // If search text is empty, return the original list
        if (searchText.isEmpty()) {
            filteredItemList.addAll(itemList);
        } else {
            // Filter the list based on the search text
            for (ProductModel item : itemList) {
                if (item.getProduct_name().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItemList.add(item);
                }
            }
        }

        listener.onSearch(filteredItemList);
    }
}

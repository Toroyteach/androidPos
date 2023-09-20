package com.ahmadabuhasan.skripsi.cashier;

import com.ahmadabuhasan.skripsi.connection.models.CategoryModel;
import com.ahmadabuhasan.skripsi.connection.models.ProductModel;
import com.ahmadabuhasan.skripsi.data.ProductSearchFunction;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilter {
    public interface SearchListener {
        void onSearch(List<CategoryModel> resultList);
    }

    public static void search(List<CategoryModel> itemList, String searchText, CategoryFilter.SearchListener listener) {
        List<CategoryModel> filteredItemList = new ArrayList<>();

        // If search text is empty, return the original list
        if (searchText.isEmpty()) {
            filteredItemList.addAll(itemList);
        } else {
            // Filter the list based on the search text
            for (CategoryModel item : itemList) {
                if (item.getCategory_name().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItemList.add(item);
                }
            }
        }

        listener.onSearch(filteredItemList);
    }
}

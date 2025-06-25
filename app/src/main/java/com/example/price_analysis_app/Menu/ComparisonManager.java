package com.example.price_analysis_app.Menu;

import com.example.price_analysis_app.Items.Item;

import java.util.ArrayList;
import java.util.List;

public class ComparisonManager {
    private static ComparisonManager instance;
    private final List<Item> selected = new ArrayList<>();

    private ComparisonManager() {}

    public static ComparisonManager getInstance() {
        if (instance == null) {
            instance = new ComparisonManager();
        }
        return instance;
    }

    public boolean addItem(Item item) {
        if (selected.contains(item) || selected.size() >= 2) return false;
        selected.add(item);
        return true;
    }

    public void removeItem(Item item) {
        selected.remove(item);
    }

    public List<Item> getSelectedItems() {
        return new ArrayList<>(selected);
    }

    public void clear() {
        selected.clear();
    }
}

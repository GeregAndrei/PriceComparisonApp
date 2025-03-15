package com.example.price_analysis_app.Items;

import com.example.price_analysis_app.Links.Link;

public interface Icallable {
    public abstract void onItemClicked(int position);
    public abstract void onLinkClicked(Link position);
}

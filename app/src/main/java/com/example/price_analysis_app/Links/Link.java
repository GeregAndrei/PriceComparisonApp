package com.example.price_analysis_app.Links;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;

public class Link implements Parcelable {
    private URL siteLink;
    private String name;
    private double price;


    public Link(URL siteLink, String name, double price) {
        this.siteLink = siteLink;
        this.name = name;
        this.price = price;
    }

    protected Link(Parcel in) {
        String siteLinkString =in.readString();
        System.out.println(siteLinkString);
        try{
            this.siteLink =  new URL(siteLinkString);
            //display link for debugging
            //System.out.println(this.siteLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.siteLink=null;
        }
        name = in.readString();
        price = in.readDouble();

    }

    public static final Creator<Link> CREATOR = new Creator<Link>() {
        @Override
        public Link createFromParcel(Parcel in) {
            return new Link(in);
        }

        @Override
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    public URL getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(URL siteLink) {
        this.siteLink = siteLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(siteLink != null ? siteLink.toString() : null);
        dest.writeString(name);
        dest.writeDouble(price);
    }

    @Override
    public String toString() {
        return "Link{" +
                "siteLink='" + siteLink +'\''+
                ", name='" + name + '\'' +
                ", price='" + price +'\''+
                '}';
    }

}

package com.example.price_analysis_app.Items;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {
    protected String name;
    protected String productCode;
    protected List<Link> linkList=new ArrayList<>();
    protected String imgUrl;
    protected String technicalChar;
    public Item(String name, String productCode, List<Link> linkList,String img,String technicalChar) {
        this.name = name;
        this.productCode = productCode;
        this.linkList=linkList;
        this.imgUrl=img;
        this.technicalChar=technicalChar;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getTechnicalChar() {
        return technicalChar;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String img) {
        this.imgUrl = img;
    }

    public Item() {

    }

    public Item(Parcel in) {
        name = in.readString();
        productCode = in.readString();
        linkList = new ArrayList<>();
        in.readTypedList(linkList, Link.CREATOR);
        imgUrl = in.readString();
        technicalChar=in.readString();
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public List<Link> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
    }
        @Override
    public int describeContents() {
        return 0;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(productCode);
        dest.writeTypedList(linkList);
        dest.writeString(imgUrl);
        dest.writeString(technicalChar);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", productCode='" + productCode + '\'' +
                ", linkList=" + linkList +
                ", imgUrl='" + imgUrl + '\'' +
                ", technicalChar='" + technicalChar + '\'' +
                '}';
    }

}

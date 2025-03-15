package com.example.price_analysis_app.Items;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.comments.Comment;

import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable {
    protected String name;
    protected String productCode;
    protected List<Link> linkList=new ArrayList<>();
    protected String imgUrl;
    protected String technicalChar;
    protected List<Comment>commentList;
    private String docId;

    public Item(String name, String productCode, List<Link> linkList,String img,String technicalChar) {
        this.name = name;
        this.productCode = productCode;
        this.linkList=linkList;
        this.imgUrl=img;
        this.technicalChar=technicalChar;
        this.commentList = new ArrayList<>();
    }
    public Item(String name, String productCode, List<Link> linkList,String img,String technicalChar,String documentId) {
        this.name = name;
        this.productCode = productCode;
        this.linkList=linkList;
        this.imgUrl=img;
        this.technicalChar=technicalChar;
        this.commentList = new ArrayList<>();
        this.docId=documentId;
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
    public Item() {
    }
    public Item(Parcel in) {
        name = in.readString();
        productCode = in.readString();
        linkList = new ArrayList<>();
        in.readTypedList(linkList, Link.CREATOR);
        imgUrl = in.readString();
        technicalChar=in.readString();
        commentList = in.createTypedArrayList(Comment.CREATOR);
        docId=in.readString();
    }

    public String getTechnicalChar() {
        return technicalChar;
    }

    public String getImg() {
        return imgUrl;
    }

    public void setImg(String img) {
        this.imgUrl = img;
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
    public void addComment(Comment comment) {
        if (comment != null){
            this.commentList.add(comment);
        }

    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(productCode);
        dest.writeTypedList(linkList);
        dest.writeString(imgUrl);
        dest.writeString(technicalChar);
        dest.writeTypedList(commentList);
        dest.writeString(docId);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", productCode='" + productCode + '\'' +
                ", linkList=" + linkList +
                ", imgUrl='" + imgUrl + '\'' +
                ", technicalChar='" + technicalChar + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}

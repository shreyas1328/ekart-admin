package com.shreyas.ekart.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.shreyas.ekart.Utiles.Convertor;

@Entity(tableName = "details_table")
public class DetailsModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "content_id")
    private int id;

    @ColumnInfo(name = "content_image")
    @TypeConverters(Convertor.class)
    private SaveImage image;

    @NonNull
    @ColumnInfo(name = "content_title")
    private String title;

    @ColumnInfo(name = "content_shortDesc")
    private String shortDesc;

    @ColumnInfo(name = "content_desc")
    private String desciption;

    @NonNull
    @ColumnInfo(name = "content_quantity")
    private String quantity;

    @NonNull
    @ColumnInfo(name = "content_price")
    private String price;

    public DetailsModel() {
    }

//    public DetailsModel(String image, @NonNull String title, String desciption, @NonNull String price, String quantity) {
//        this.image = image;
//        this.title = title;
//        this.desciption = desciption;
//        this.quantity = quantity;
//        this.price = price;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SaveImage getImage() {
        return image;
    }

    public void setImage(SaveImage image) {
        this.image = image;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    @NonNull
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(@NonNull String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}

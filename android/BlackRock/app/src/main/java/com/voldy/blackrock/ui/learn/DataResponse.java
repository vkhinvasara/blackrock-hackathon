package com.voldy.blackrock.ui.learn;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataResponse implements Serializable {
    private Double Category_Food;

    private Double Category_Health;

    private Double Category_Living;

    private Double Category_Miscellaneous;

    private Double Category_Transport;


    public DataResponse() {
    }

    public DataResponse(Double category_Food, Double category_Health, Double category_Living, Double category_Miscellaneous, Double category_Transport) {
        Category_Food = category_Food;
        Category_Health = category_Health;
        Category_Living = category_Living;
        Category_Miscellaneous = category_Miscellaneous;
        Category_Transport = category_Transport;
    }

    public Double getCategory_Food() {
        return Category_Food;
    }

    public void setCategory_Food(Double category_Food) {
        Category_Food = category_Food;
    }

    public Double getCategory_Health() {
        return Category_Health;
    }

    public void setCategory_Health(Double category_Health) {
        Category_Health = category_Health;
    }

    public Double getCategory_Living() {
        return Category_Living;
    }

    public void setCategory_Living(Double category_Living) {
        Category_Living = category_Living;
    }

    public Double getCategory_Miscellaneous() {
        return Category_Miscellaneous;
    }

    public void setCategory_Miscellaneous(Double category_Miscellaneous) {
        Category_Miscellaneous = category_Miscellaneous;
    }

    public Double getCategory_Transport() {
        return Category_Transport;
    }

    public void setCategory_Transport(Double category_Transport) {
        Category_Transport = category_Transport;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "Category_Food=" + Category_Food +
                ", Category_Health=" + Category_Health +
                ", Category_Living=" + Category_Living +
                ", Category_Miscellaneous=" + Category_Miscellaneous +
                ", Category_Transport=" + Category_Transport +
                '}';
    }
}

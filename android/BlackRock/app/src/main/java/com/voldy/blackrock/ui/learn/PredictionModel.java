package com.voldy.blackrock.ui.learn;

public class PredictionModel {
    private int Category_Food;
    private int Category_Health;
    private int Category_Living;
    private int Category_Miscellaneous;
    private int Category_Transport;

    public PredictionModel(int category_Food, int category_Health, int category_Living, int category_Miscellaneous, int category_Transport) {
        Category_Food = category_Food;
        Category_Health = category_Health;
        Category_Living = category_Living;
        Category_Miscellaneous = category_Miscellaneous;
        Category_Transport = category_Transport;
    }

    public PredictionModel() {
    }

    public int getCategory_Food() {
        return Category_Food;
    }

    public void setCategory_Food(int category_Food) {
        Category_Food = category_Food;
    }

    public int getCategory_Health() {
        return Category_Health;
    }

    public void setCategory_Health(int category_Health) {
        Category_Health = category_Health;
    }

    public int getCategory_Living() {
        return Category_Living;
    }

    public void setCategory_Living(int category_Living) {
        Category_Living = category_Living;
    }

    public int getCategory_Miscellaneous() {
        return Category_Miscellaneous;
    }

    public void setCategory_Miscellaneous(int category_Miscellaneous) {
        Category_Miscellaneous = category_Miscellaneous;
    }

    public int getCategory_Transport() {
        return Category_Transport;
    }

    public void setCategory_Transport(int category_Transport) {
        Category_Transport = category_Transport;
    }
}

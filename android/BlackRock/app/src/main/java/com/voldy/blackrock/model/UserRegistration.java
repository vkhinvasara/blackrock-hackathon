package com.voldy.blackrock.model;

public class UserRegistration {
    private String name;
    private String mobileNumber;
    private int salary;
    private int foodExpense;
    private int healthExpense;
    private int livingExpense;
    private int transportExpense;
    private int payables;
    private int miscellaneous;
    private String email;
    private String password;

    // Constructor
    public UserRegistration(String name, String mobileNumber, int salary, int foodExpense, int healthExpense,
                            int livingExpense, int transportExpense, int payables, int miscellaneous,
                            String email, String password) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.salary = salary;
        this.foodExpense = foodExpense;
        this.healthExpense = healthExpense;
        this.livingExpense = livingExpense;
        this.transportExpense = transportExpense;
        this.payables = payables;
        this.miscellaneous = miscellaneous;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getFoodExpense() {
        return foodExpense;
    }

    public void setFoodExpense(int foodExpense) {
        this.foodExpense = foodExpense;
    }

    public int getHealthExpense() {
        return healthExpense;
    }

    public void setHealthExpense(int healthExpense) {
        this.healthExpense = healthExpense;
    }

    public int getLivingExpense() {
        return livingExpense;
    }

    public void setLivingExpense(int livingExpense) {
        this.livingExpense = livingExpense;
    }

    public int getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(int transportExpense) {
        this.transportExpense = transportExpense;
    }

    public int getPayables() {
        return payables;
    }

    public void setPayables(int payables) {
        this.payables = payables;
    }

    public int getMiscellaneous() {
        return miscellaneous;
    }

    public void setMiscellaneous(int miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", salary=" + salary +
                ", foodExpense=" + foodExpense +
                ", healthExpense=" + healthExpense +
                ", livingExpense=" + livingExpense +
                ", transportExpense=" + transportExpense +
                ", payables=" + payables +
                ", miscellaneous=" + miscellaneous +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

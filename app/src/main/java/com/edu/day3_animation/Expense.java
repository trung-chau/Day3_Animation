package com.edu.day3_animation;

public class Expense {

    private int superMarket;
    private int electric;
    private int internet;

    public Expense(int superMarket, int electric, int internet) {
        this.superMarket = superMarket;
        this.electric = electric;
        this.internet = internet;
    }

    public int getSuperMarket() {
        return superMarket;
    }

    public void setSuperMarket(int superMarket) {
        this.superMarket = superMarket;
    }

    public int getElectric() {
        return electric;
    }

    public void setElectric(int electric) {
        this.electric = electric;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public int getTotalExpense() {
        return superMarket + electric + internet;
    }
}

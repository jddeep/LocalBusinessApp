package com.example.android.business_new;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    private String billNumber;
    private String billDate;
    private String goods;
    private double totamt;
    private double dueamt;
    private List<String> pays;
    private List<String> paydates;

    public Bill(){

    }

    public Bill(String billNumber, String billDate, String goods, double totamt, double dueamt, List<String> pays, List<String> paydates) {
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.goods = goods;
        this.totamt = totamt;
        this.dueamt = dueamt;
        this.pays = pays;
        this.paydates = paydates;
    }
    public String getBillNumber() {
        return billNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getGoods() {
        return goods;
    }

    public double getTotamt() {
        return totamt;
    }

    public double getDueamt() {
        return dueamt;
    }

    public List<String> getPays(){
        return pays;
    }

    public List<String> getPaydates(){
        return paydates;
    }


}

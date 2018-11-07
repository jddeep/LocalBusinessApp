package com.example.android.business_new;

public class Bill {

    private String billNumber;
    private String billDate;
    private String goods;
    private double totamt;
    private double dueamt;

    public Bill(){

    }

    public Bill(String billNumber, String billDate, String goods, double totamt, double dueamt) {
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.goods = goods;
        this.totamt = totamt;
        this.dueamt = dueamt;
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


}

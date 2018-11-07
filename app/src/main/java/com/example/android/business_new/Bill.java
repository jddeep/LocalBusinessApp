package com.example.android.business_new;

public class Bill {

    private String BillNumber;
    private String BillDate;
    private String goods;
    private double totamt;
    private double dueamt;

    public Bill(){

    }

    public Bill(String billNumber, String billDate, String goods, double totamt, double dueamt) {
        BillNumber = billNumber;
        BillDate = billDate;
        this.goods = goods;
        this.totamt = totamt;
        this.dueamt = dueamt;
    }
    public String getBillNumber() {
        return BillNumber;
    }

    public String getBillDate() {
        return BillDate;
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

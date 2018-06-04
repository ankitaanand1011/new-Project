package com.drinkdrop.drinkdropowner.modal;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetailORM implements Serializable {

    String orderID;
    String orderStatus;
    AddressORM addressORM;
    ArrayList<OrderItemORM> orderItems;
    String orderDate;
    String grandTotal;
    String paymentMode;


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public AddressORM getAddressORM() {
        return addressORM;
    }

    public void setAddressORM(AddressORM addressORM) {
        this.addressORM = addressORM;
    }

    public ArrayList<OrderItemORM> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItemORM> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}

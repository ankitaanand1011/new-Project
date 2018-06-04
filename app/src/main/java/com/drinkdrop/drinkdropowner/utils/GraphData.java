package com.drinkdrop.drinkdropowner.utils;

import java.io.Serializable;


public class GraphData implements Serializable {

    String receivedDate;
    String totalReceived;

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(String totalReceived) {
        this.totalReceived = totalReceived;
    }
}

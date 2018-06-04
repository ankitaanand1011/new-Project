package com.drinkdrop.drinkdropowner.utils;

import java.io.Serializable;

/**
 * Created by developer on 20/4/18.
 */

public class GraphAccepted implements Serializable {

    String acceptedDate;
    String totalAccepted;

    public String getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(String acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getTotalAccepted() {
        return totalAccepted;
    }

    public void setTotalAccepted(String totalAccepted) {
        this.totalAccepted = totalAccepted;
    }
}

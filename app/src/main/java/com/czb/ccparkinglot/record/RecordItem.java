package com.czb.ccparkinglot.record;

import java.io.Serializable;


public class RecordItem implements Serializable {
    String carName;
    String parkinglotName;
    String status;
    Date startTime;
    Date endTime;

    public RecordItem(String carName, String parkinglotName, String status, Date startTime,
                      Date endTime) {
        this.carName = carName;
        this.parkinglotName = parkinglotName;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCarName() {
        return carName;
    }

    public String getParkinglotName() {
        return parkinglotName;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}

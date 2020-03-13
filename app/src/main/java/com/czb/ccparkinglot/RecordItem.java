package com.czb.ccparkinglot;

public class RecordItem {
    String whichcar;
    String whichparkinglot;
    String whatstatus;
    int whenstart;
    int duration;

    public RecordItem(String whichcar, String whichparkinglot, String whatstatus, int whenstart,
                      int duration) {
        this.whichcar = whichcar;
        this.whichparkinglot = whichparkinglot;
        this.whatstatus = whatstatus;
        this.whenstart = whenstart;
        this.duration = duration;
    }

    public String getWhichcar() {
        return whichcar;
    }

    public String getWhichparkinglot() {
        return whichparkinglot;
    }

    public String getWhatstatus() {
        return whatstatus;
    }

    public int getWhenstart() {
        return whenstart;
    }

    public int getDuration() {
        return duration;
    }
}

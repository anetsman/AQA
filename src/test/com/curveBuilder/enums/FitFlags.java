package com.curveBuilder.enums;

public enum FitFlags {
    F("F"),
    O("O"),
    S("S"),
    I("I");

    private String flag;

    FitFlags(String fitFlag) {
        this.flag = fitFlag;
    }

    public String getFlag() {
        return flag;
    }
}

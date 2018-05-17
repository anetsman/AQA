package com.curveBuilder.prototypes;

import java.util.Comparator;

public class TenorName implements Comparable<TenorName> {

    private final String age;
    private final String instrument;

    public TenorName(String tenorName) {
        this.age = tenorName.split(" ")[0];
        this.instrument = tenorName.split(" ")[1];
    }

    public String getAge() {
        return age;
    }

    public String getInstrument() {
        return instrument;
    }

    @Override
    public String toString() {
        return age + ' ' + instrument;
    }


    @Override
    public int compareTo(TenorName tn) {
        return Integer.valueOf(this.age.split("\\D")[0]) - Integer.valueOf(tn.getAge().split("\\D")[0]);
    }

    public static Comparator<TenorName> TenorNameComparator =
            Comparator.comparing(TenorName::getInstrument);
}

package com.curveBuilder.prototypes;

public class HouseValue {
    private String driverName;
    private String firstWingName;
    private String secondWingName;
    private String newValue;

    private HouseValue(HouseModelBuilder builder) {
        this.driverName = builder.driverName;
        this.firstWingName = builder.firstWingName;
        this.secondWingName = builder.secondWingName;
        this.newValue = builder.newValue;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getFirstWingName() {
        return firstWingName;
    }

    public String getSecondWingName() {
        return secondWingName;
    }

    public String getNewValue() {
        return newValue;
    }

    public static class HouseModelBuilder {
        private String driverName;
        private String firstWingName;
        private String secondWingName;
        private String newValue;

        public HouseModelBuilder() {
        }

        public HouseModelBuilder driverName(String driverName) {
            this.driverName = driverName;
            return this;
        }

        public HouseModelBuilder firstWingName(String firstWingName) {
            this.firstWingName = firstWingName;
            return this;
        }

        public HouseModelBuilder secondWingName(String secondWingName) {
            this.secondWingName = secondWingName;
            return this;
        }

        public HouseModelBuilder newValue(String newValue) {
            this.newValue = newValue;
            return this;
        }

        public HouseValue build() {
            return new HouseValue(this);
        }
    }
}
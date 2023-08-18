package pl.astek.trip;

import java.util.List;

public class AdminConfiguration {
    private double dailyAllowanceRate = 15.0;
    private double mileageRate = 0.3;
    private List<ReceiptType> availableReceiptTypes;
    private double singleReceiptLimit = 100.0;
    private double totalReimbursementLimit = 500.0;
    private double distanceLimit = 100.0;

    public double getDailyAllowanceRate() {
        return dailyAllowanceRate;
    }

    public void setDailyAllowanceRate(double dailyAllowanceRate) {
        this.dailyAllowanceRate = dailyAllowanceRate;
    }

    public double getMileageRate() {
        return mileageRate;
    }

    public void setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
    }

    public List<ReceiptType> getAvailableReceiptTypes() {
        return availableReceiptTypes;
    }

    public void setAvailableReceiptTypes(List<ReceiptType> availableReceiptTypes) {
        this.availableReceiptTypes = availableReceiptTypes;
    }

    public double getSingleReceiptLimit() {
        return singleReceiptLimit;
    }

    public void setSingleReceiptLimit(double singleReceiptLimit) {
        this.singleReceiptLimit = singleReceiptLimit;
    }

    public double getTotalReimbursementLimit() {
        return totalReimbursementLimit;
    }

    public void setTotalReimbursementLimit(double totalReimbursementLimit) {
        this.totalReimbursementLimit = totalReimbursementLimit;
    }

    public double getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(double distanceLimit) {
        this.distanceLimit = distanceLimit;
    }
}
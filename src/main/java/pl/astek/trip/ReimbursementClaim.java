package pl.astek.trip;

import java.util.ArrayList;
import java.util.List;

public class ReimbursementClaim {
    private String tripDate;
    private List<Receipt> receipts = new ArrayList<>();
    private int claimedTripDays;
    private boolean disableDays;
    private double claimedMileage;

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public int getClaimedTripDays() {
        return claimedTripDays;
    }

    public void setClaimedTripDays(int claimedTripDays) {
        this.claimedTripDays = claimedTripDays;
    }

    public boolean isDisableDays() {
        return disableDays;
    }

    public void setDisableDays(boolean disableDays) {
        this.disableDays = disableDays;
    }

    public double getClaimedMileage() {
        return claimedMileage;
    }

    public void setClaimedMileage(double claimedMileage) {
        this.claimedMileage = claimedMileage;
    }

}
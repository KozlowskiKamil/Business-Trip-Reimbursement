package pl.astek.trip;

public class ReimbursementCalculator {
    public static double calculateTotalReimbursement(ReimbursementClaim claim, double dailyAllowanceRate, double mileageRate) {
        double total = 0.0;

        for (Receipt receipt : claim.getReceipts()) {
            total += receipt.getAmount();
        }

        if (claim.getClaimedTripDays() > 0) {
            double dailyAllowance = dailyAllowanceRate * claim.getClaimedTripDays();
            total += dailyAllowance;
        }

        if (claim.getClaimedMileage() > 0) {
            double mileageReimbursement = mileageRate * claim.getClaimedMileage();
            total += mileageReimbursement;
        }

        return total;
    }
}
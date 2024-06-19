package Data;

public class CreditsData {
    private static int creditForCancellationOnSameDay = 50;
    private static int creditForCancellationBefore = 80;

    public static void setCreditForCancellationOnSameDay(int creditForCancellationOnSameDay) {
        CreditsData.creditForCancellationOnSameDay = creditForCancellationOnSameDay;
    }
    public static void setCreditForCancellationBefore(int creditForCancellationBefore) {
        CreditsData.creditForCancellationBefore = creditForCancellationBefore;
    }
    public static int getCreditForCancellationOnSameDay() {
        return creditForCancellationOnSameDay;
    }
    public static int getCreditForCancellationBefore() {
        return creditForCancellationBefore;
    }
}

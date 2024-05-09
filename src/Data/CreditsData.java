package Data;

public class CreditsData {
    static int creditForCancellationOnSameDay = 50;
    static int creditForCancellationBefore = 80;
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
